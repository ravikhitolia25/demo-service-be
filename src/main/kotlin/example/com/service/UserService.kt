package example.com.service

import example.com.model.CombinedData
import example.com.model.Comment
import example.com.model.Post
import example.com.model.User
import example.com.repository.UserRepository
import example.com.utils.PostNotFoundException
import example.com.utils.UserNotFoundException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserService(private val userRepository: UserRepository) {
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    suspend fun getUserData(id: Int?): CombinedData {
        val userId = id ?: fetchUserIdFromAPI()
        logger.info("Fetching data for userId: $userId")
        // Attempt to fetch from cache first
        val cachedData = getCachedData(userId)
        if (cachedData != null) {
            return cachedData
        }
        logger.info("Cache miss for userId: $userId")
        val user = userRepository.getUserById(userId) ?: fetchUserFromAPI(userId)

        val post = fetchPost(userId)
        val comments = fetchComments(post.id)

        val combinedData = CombinedData(user, post, comments)

        userRepository.save(user)
        cacheUserData(userId, combinedData)
        logger.info("Data successfully fetched and cached for userId: $userId")
        return combinedData
    }

    private fun getCachedData(userId: Int): CombinedData? {
        val jedis = RedisConfig.getJedisClient()
        val cachedData = jedis.get("user:$userId")
        return cachedData?.let {
            logger.info("Decoding cached data for userId: $userId")
            Json.decodeFromString<CombinedData>(it)
        }
    }

    private fun cacheUserData(userId: Int, combinedData: CombinedData) {
        val jedis = RedisConfig.getJedisClient()
        logger.info("Caching data for userId: $userId")
        jedis.set("user:$userId", Json.encodeToString(combinedData))
    }

    private suspend fun fetchUserIdFromAPI(): Int {
        val client = HttpClient()
        try {
            val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/users")

            val userData: List<User> = Json.decodeFromString(response.bodyAsText())

            return userData.firstOrNull()?.id ?: throw UserNotFoundException("User not found")
        } finally {
            client.close()
        }
    }

    suspend fun fetchUserFromAPI(userId: Int): User {
        return withContext(Dispatchers.IO) {

            val client = HttpClient()
            try {

                val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/users/$userId")

                if (!response.status.isSuccess()) {
                    logger.error("Failed to fetch user details for userId: $userId, Status: ${response.status}")
                    throw UserNotFoundException("Failed to fetch user details. Status: ${response.status}")
                }
                val userJson = response.bodyAsText()
                Json.decodeFromString(userJson)
            } finally {
                client.close()
            }
        }
    }

    suspend fun fetchPost(userId: Int): Post {
        val client = HttpClient()
        try {
            logger.info("Fetching post for userId: $userId")
            val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/posts?userId=$userId")
            val postData: List<Post> = Json.decodeFromString(response.bodyAsText())
            return postData.firstOrNull() ?: throw PostNotFoundException("Post not found for userId: $userId")
        } finally {
            client.close()
        }
    }

    suspend fun fetchComments(postId: Int): List<Comment> {
        val client = HttpClient()
        try {
            logger.info("Fetching comments for postId: $postId")
            val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/comments?postId=$postId")
            return Json.decodeFromString(response.bodyAsText())
        } finally {
            client.close()
        }
    }
}
