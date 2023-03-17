package ru.quipy.projection

import java.util.UUID

interface UserProjectionService {

    fun getUser(userId: UUID): UserDto

    fun isAuthenticatedUser(login: String, password: String): Boolean

    fun getUsersByLoginFragment(fragment: String): List<UserDto>

}