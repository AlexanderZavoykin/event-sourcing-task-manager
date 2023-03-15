package ru.quipy.validation

interface LoginValidationService {

    fun checkUserNotExistsByLogin(login: String)

}