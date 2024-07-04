package example.com.serviceTest

import example.com.model.*
import example.com.repository.UserRepository
import example.com.service.UserService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserServiceTest {

    private val userRepository: UserRepository = mockk()
    private val userService: UserService = UserService(userRepository)

    @BeforeEach
    fun setup() {
    }

    @Test
    fun `getUserData should fetch user data from API and cache it`() = runBlocking {
        val userId = 1
        val user = User(id = userId, name = "Leanne Graham", username = "Bret", email = "Sincere@april.biz", address = Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", Geo("-37.3159", "81.1496")), phone = "1-770-736-8031 x56442", website = "hildegard.org", company = Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"))
        coEvery { userRepository.getUserById(userId) } returns user
        coEvery { userRepository.save(user) } returns Unit

        val result = userService.getUserData(userId)

        assertEquals(user, result.user)
    }

}
