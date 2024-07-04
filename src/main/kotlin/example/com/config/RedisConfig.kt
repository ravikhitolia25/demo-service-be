import redis.clients.jedis.Jedis

object RedisConfig {
    private val jedis: Jedis by lazy {
        Jedis("localhost", 6379)
    }

    fun getJedisClient(): Jedis {
        return jedis
    }
}