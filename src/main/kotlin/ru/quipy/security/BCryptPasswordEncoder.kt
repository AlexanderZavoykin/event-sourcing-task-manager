package ru.quipy.security

import at.favre.lib.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class BCryptPasswordEncoder(

): PasswordEncoder {

    private val hasher: BCrypt.Hasher = BCrypt.withDefaults()
    private val verifier: BCrypt.Verifyer = BCrypt.verifyer()

    override fun encode(password: String): String =
        hasher.hashToString(PWD_COST, password.toCharArray())

    override fun verify(tryPassword: String, passwordHash: String): Boolean =
        verifier.verify(tryPassword.toCharArray(), passwordHash.toCharArray()).verified

    companion object {
        const val PWD_COST = 12
    }

}