package ru.quipy.projection

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import ru.quipy.exception.NotFoundException
import ru.quipy.security.PasswordEncoder
import java.util.UUID

@Service
class SqlUserProjectionService(
    private val encoder: PasswordEncoder,
) : UserProjectionService {

    override fun getUser(userId: UUID): UserInfo =
        transaction {
            UserTable
                .slice(UserTable.id, UserTable.login)
                .select { UserTable.id eq userId }
                .map { UserInfo(it[UserTable.id].value, it[UserTable.login]) }
                .firstOrNull()
                ?: throw NotFoundException("No such user $userId")
        }

    override fun getUserIdByLogin(login: String): UUID =
        transaction {
            UserTable.slice(UserTable.id)
                .select { UserTable.login.eq(login) }
                .map { it[UserTable.id].value }
                .firstOrNull()
                ?: throw NotFoundException("No such user with login $login")
        }

    override fun getUsersByLoginFragment(fragment: String): List<UserInfo> =
        transaction {
            UserTable
                .slice(UserTable.id, UserTable.login)
                .select { UserTable.login like "%$fragment%" }
                .map { UserInfo(it[UserTable.id].value, it[UserTable.login]) }
        }

}