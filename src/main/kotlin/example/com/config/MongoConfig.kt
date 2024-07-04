package example.com.config

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object MongoConfig {
    private val client: com.mongodb.reactivestreams.client.MongoClient = KMongo.createClient()
    val db: CoroutineDatabase = client.getDatabase("demo-service").coroutine

    fun getDatabase(): CoroutineDatabase = db
}