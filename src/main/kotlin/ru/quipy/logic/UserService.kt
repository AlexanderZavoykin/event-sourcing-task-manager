package ru.quipy.logic

import org.springframework.stereotype.Service
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.exception.NotFoundException
import ru.quipy.projection.UserProjectionService
import ru.quipy.security.PasswordEncoder
import ru.quipy.transaction.TransactionManager
import ru.quipy.validation.LoginValidationService
import java.util.UUID

@Service
class UserService(
    private val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    private val userProjectionService: UserProjectionService,
    private val loginValidationService: LoginValidationService,
    private val txManager: TransactionManager,
    private val encoder: PasswordEncoder,
) {

    fun registerUser(login: String, password: String): UserCreatedEvent =
        txManager.transaction {
            loginValidationService.throwsIfUserLoginExists(login)

            return@transaction userEsService.create {
                it.create(login = login, password = encoder.encode(password))
            }
        }

    fun getUser(userId: UUID): UserAggregateState =
        userEsService.getState(userId)
            ?: throw NotFoundException("No such user $userId")

    fun isAuthenticatedUser(login: String, password: String): Boolean {
        val userId = userProjectionService.getUserIdByLogin(login)
        val user = userEsService.getState(userId)
            ?: throw NotFoundException("No such user $userId")

        return encoder.verify(password, user.password)
    }

}