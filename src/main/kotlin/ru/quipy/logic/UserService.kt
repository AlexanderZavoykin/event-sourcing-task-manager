package ru.quipy.logic

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.springframework.stereotype.Service
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.transaction.TransactionManager
import ru.quipy.validation.LoginValidationService
import java.util.*

@Service
class UserService(
    private val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    private val loginValidationService: LoginValidationService,
    private val txManager: TransactionManager,
) {

    fun createUser(login: String, password: String): UserCreatedEvent =
        try {
            txManager.transaction {
                loginValidationService.checkUserNotExistsByLogin(login)

                return@transaction userEsService.create {
                    it.create(login = login, password = password)
                }
            }
        } catch (e: ExposedSQLException) {
            throw IllegalStateException("Login already exists")
        }

    fun getUser(userId: UUID): UserAggregateState? = userEsService.getState(userId)

}