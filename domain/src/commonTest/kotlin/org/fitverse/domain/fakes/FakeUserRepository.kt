package org.fitverse.domain.fakes

import org.fitverse.domain.models.user.Genero
import org.fitverse.domain.models.user.User
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// 01/01/1990 em epoch millis
private const val BIRTH_01_01_1990 = 631_152_000_000L

val fakeUser = User(
    uid       = "uid-test",
    name      = "Test",
    lastname  = "User",
    email     = "test@fit.com",
    username  = "testuser",
    birthDate = BIRTH_01_01_1990,
    weight    = 75.0,
    height    = 175,
    genero    = Genero.MASCULINO,
    createdAt = 0L,
    updatedAt = 0L,
)

class FakeUserRepository(
    private val user: User = fakeUser,
) : UserRepository {
    override suspend fun createUser(user: User) {}
    override fun observeUser(userId: String): Flow<User?> = flowOf(user)
    override suspend fun getUser(userId: String): User = user
    override suspend fun updateUser(user: User): User = user
    override suspend fun deleteUser(userId: String) {}
    override suspend fun syncOfflineData(user: User) {}
}
