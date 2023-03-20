package ru.quipy.projection

import java.util.UUID

interface UserProjectionService {

    fun getUser(userId: UUID): UserInfo

    fun getUserIdByLogin(login: String): UUID

    fun getUsersByLoginFragment(fragment: String): List<UserInfo>

}