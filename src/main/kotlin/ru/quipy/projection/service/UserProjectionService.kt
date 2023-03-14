package ru.quipy.projection.service

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import ru.quipy.projection.ProjectTable
import ru.quipy.projection.UserResponse
import ru.quipy.projection.UserTable
import ru.quipy.security.BCryptPasswordEncoder
import java.util.*

@Service
class UserProjectionService(
    private val encoder: BCryptPasswordEncoder,
) {

    fun isAuthenticatedUser(login: String, password: String): Boolean {
        val encodedPassword = transaction {
            UserTable
                .slice(UserTable.password)
                .select { UserTable.login.eq(login) }
                .map { it[UserTable.password] }
                .first()
        }
        return encoder.verify(password, encodedPassword)
    }

    fun getAllByLoginLike(fragment: String): UserResponse =
        transaction {
            UserTable
                .slice(UserTable.id, UserTable.login)
                .select { UserTable.login like "%$fragment%" }
                .map { UserResponse(it[UserTable.id].value, it[UserTable.login]) }
                .first()
        }

    fun getAllByProjectId(projectId: UUID): List<UserResponse> =
        transaction {
            (UserTable innerJoin ProjectTable)
                .slice(UserTable.id, UserTable.login)
                .select { ProjectTable.id eq projectId }
                .map { UserResponse(it[UserTable.id].value, it[UserTable.login]) }
        }

}