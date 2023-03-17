package ru.quipy.validation

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Service

@Service
class SqlLoginValidationService : LoginValidationService {

    override fun checkUserNotExistsByLogin(login: String) {
        try {
            LoginTable.insert {
                it[this.login] = login
            }
        } catch (e: ExposedSQLException) {
            throw IllegalStateException("Login already exists")
        }
    }

}