package ru.quipy.validation

import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Service

@Service
class SqlLoginValidationService : LoginValidationService {

    override fun checkUserNotExistsByLogin(login: String) {
        LoginTable.insert {
            it[this.login] = login
        }
    }

}