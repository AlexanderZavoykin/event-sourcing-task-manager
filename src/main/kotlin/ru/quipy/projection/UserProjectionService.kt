package ru.quipy.projection

import java.util.UUID

interface UserProjectionService {

    fun getUser(userId: UUID): UserInfo

    fun isAuthenticatedUser(login: String, password: String): Boolean

    fun getUsersByLoginFragment(fragment: String): List<UserInfo>

}