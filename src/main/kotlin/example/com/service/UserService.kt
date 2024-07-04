package example.com.service

import example.com.model.CombinedData
import example.com.model.Comment
import example.com.model.Post
import example.com.model.User
import example.com.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserService(private val userRepository: UserRepository) {
    suspend fun getUserData(id: Int?): CombinedData {
        val userId = id ?: fetchUserIdFromAPI()
        // Attempt to fetch from cache first

        val cachedData = getCachedData(userId)
        if (cachedData != null) {
            return cachedData
        }
        val user = userRepository.getUserById(userId) ?: fetchUserFromAPI(userId)

        val post = fetchPost(userId)
        val comments = fetchComments(post.id)

        val combinedData = CombinedData(user, post, comments)

        userRepository.save(user)
        cacheUserData(userId, combinedData)


        return combinedData
    }
    private fun getCachedData(userId: Int): CombinedData? {
        val jedis = RedisConfig.getJedisClient()
        val cachedData = jedis.get("user:$userId")
        return cachedData?.let { Json.decodeFromString<CombinedData>(it) }
    }

    private fun cacheUserData(userId: Int, combinedData: CombinedData) {
        val jedis = RedisConfig.getJedisClient()
        jedis.set("user:$userId", Json.encodeToString(combinedData))
    }

    private suspend fun fetchUserIdFromAPI(): Int {
        val client = HttpClient()
        val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/users")

        val userData: List<User> = Json.decodeFromString(response.bodyAsText())

        client.close()

        return userData.firstOrNull()?.id ?: throw IllegalStateException("User not found")
    }

    suspend fun fetchUserFromAPI(userId: Int): User {
        val client = HttpClient()
        val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/users/$userId")

        val userData: User = Json.decodeFromString(response.bodyAsText())

        client.close()

        return userData
    }

    suspend fun fetchPost(userId: Int): Post {
        val client = HttpClient()
        val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/posts?userId=$userId")

        val postData: List<Post> = Json.decodeFromString(response.bodyAsText())

        client.close()

        return postData.firstOrNull() ?: throw IllegalStateException("Post not found")
    }

    suspend fun fetchComments(postId: Int): List<Comment> {
        val client = HttpClient()
        val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/comments?postId=$postId")

        val commentsData: List<Comment> = Json.decodeFromString(response.bodyAsText())

        client.close()

        return commentsData
    }
}