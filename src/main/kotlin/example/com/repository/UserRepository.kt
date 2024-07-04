package example.com.repository


import example.com.model.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserRepository(database: CoroutineDatabase) {
    private val collection = database.getCollection<User>()

    suspend fun getUserById(id: Int): User? {
        return collection.findOne(User::id eq id)
    }

    suspend fun save(user: User) {
        collection.insertOne(user)
    }
}