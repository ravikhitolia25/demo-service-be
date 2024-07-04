package example.com.model

import kotlinx.serialization.Serializable

@Serializable
data class CombinedData(
    val user: User,
    val post: Post,
    val comments: List<Comment>
)
