package ru.quipy.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.UserCreatedEvent
import ru.quipy.logic.UserService
import ru.quipy.projection.UserInfo
import ru.quipy.projection.UserProjectionService
import java.util.*

@RestController
@RequestMapping("/users")
@Tag(name = "Work with users")
class UserController(
    private val userService: UserService,
    private val userProjectionService: UserProjectionService,
) {

    @PostMapping
    @Operation(summary = "Register a new user")
    fun registerUser(@RequestParam login: String, @RequestParam password: String): UserCreatedEvent =
        userService.registerUser(login, password)

    @GetMapping("/{userId}")
    @Operation(summary = "Get a user")
    fun getUser(@PathVariable userId: UUID): UserInfo = userProjectionService.getUser(userId)

    @GetMapping("/login")
    @Operation(summary = "Authenticate a user")
    fun authenticateUser(@RequestParam login: String, @RequestParam password: String): ResponseEntity<Void> {
        val status = if (userProjectionService.isAuthenticatedUser(login, password)) HttpStatus.OK
        else HttpStatus.UNAUTHORIZED

        return ResponseEntity(status)
    }

    @GetMapping
    @Operation(summary = "Get users with logins partly coincided with a given login fragment")
    fun getUsersByLoginFragment(@RequestParam login: String): List<UserInfo> =
        userProjectionService.getUsersByLoginFragment(login)

}
