package org.fitverse.domain.fakes

import org.fitverse.domain.models.user.ClassType
import org.fitverse.domain.models.user.Genero
import org.fitverse.domain.models.user.User
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

val fakeUser = User(
    uid         = "uid-test",
    name        = "Test",
    lastname    = "User",
    email       = "test@fit.com",
    username    = "testuser",
    birthDate   = "01/01/1990",
    weight      = 75.0,
    height      = 175,
    genero      = Genero.MASCULINO,
    classType   = ClassType.TITAN,
    goals       = "Lose weight",
    experienceLevel = "Beginner",
    createdAt   = 0L,
    updatedAt   = 0L,
)

class FakeUserRepository(
    private val user: User = fakeUser
) : UserRepository {
    override suspend fun createUser(user: User) {}
    override fun observeUser(userId: String): Flow<User?> = flowOf(user)
    override suspend fun getUser(userId: String): User = user
    override suspend fun updateUser(user: User): User = user
    override suspend fun deleteUser(userId: String) {}
    override suspend fun syncOfflineData(user: User) {}
}
