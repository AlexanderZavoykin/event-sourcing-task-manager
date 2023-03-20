package ru.quipy.security

interface PasswordEncoder {

    fun encode(password: String): String

    fun verify(tryPassword: String, passwordHash: String): Boolean

}