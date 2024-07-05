package example.com.serviceTest

import example.com.model.*
import example.com.repository.UserRepository
import example.com.service.UserService
import example.com.utils.UserNotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

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

    @Test
    fun `getUserData should fetch user data from API when not cached`() = runBlocking {
        val userId = 2
        val user = User(
            id = userId,
            name = "Ervin Howell",
            username = "Antonette",
            email = "Shanna@melissa.tv",
            address = Address(
                "Victor Plains",
                "Suite 879",
                "Wisokyburgh",
                "90566-7771",
                Geo("-43.9509", "-34.4618")
            ),
            phone = "010-692-6593 x09125",
            website = "anastasia.net",
            company = Company(
                "Deckow-Crist",
                "Proactive didactic contingency",
                "synergize scalable supply-chains"
            )
        )
        coEvery { userRepository.getUserById(userId) } returns null
        coEvery { userRepository.save(any()) } returns Unit

        val result = userService.getUserData(userId)

        assertEquals(user, result.user)
    }

    @Test
    fun `getUserData should handle fetching when user ID is null`() = runBlocking {
        val userId: Int? = null
        val user = User(
            id = 1,
            name = "Leanne Graham",
            username = "Bret",
            email = "Sincere@april.biz",
            address = Address(
                "Kulas Light",
                "Apt. 556",
                "Gwenborough",
                "92998-3874",
                Geo("-37.3159", "81.1496")
            ),
            phone = "1-770-736-8031 x56442",
            website = "hildegard.org",
            company = Company(
                "Romaguera-Crona",
                "Multi-layered client-server neural-net",
                "harness real-time e-markets"
            )
        )
        coEvery { userRepository.getUserById(any()) } returns user
        coEvery { userRepository.save(user) } returns Unit

        val result = userService.getUserData(userId)

        assertEquals(user, result.user)
    }

    @Test
    fun `getUserData should handle case when user is not found`() = runBlocking {
        val userId = 999 // User ID that doesn't exist
        coEvery { userRepository.getUserById(userId) } throws UserNotFoundException("Failed to fetch user details. Status: 404 Not Found")

        val exception = assertFailsWith<UserNotFoundException> {
            userService.getUserData(userId)
        }
        assertEquals("Failed to fetch user details. Status: 404 Not Found", exception.message)
    }


}
