package ru.quipy.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.UserCreatedEvent
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.UserService
import ru.quipy.projection.UserResponse
import ru.quipy.projection.service.UserProjectionService
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userProjectionService: UserProjectionService,
) {

    @PostMapping
    fun createUser(@RequestParam login: String, @RequestParam password: String): UserCreatedEvent =
        userService.createUser(login, password)

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID): UserAggregateState? = userService.getUser(userId)

    @GetMapping("/login")
    fun authenticateUser(@RequestParam login: String, @RequestParam password: String): ResponseEntity<Void> {
        val status = if (userProjectionService.isAuthenticatedUser(login, password)) HttpStatus.OK
        else HttpStatus.UNAUTHORIZED

        return ResponseEntity(status)
    }

    @GetMapping
    fun getAllByLoginFragment(@RequestParam login: String): UserResponse =
        userProjectionService.getAllByLoginLike(login)

}
