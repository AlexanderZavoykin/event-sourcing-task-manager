package ru.quipy.validation

/**
 * Checks if user login already exists.
 */
interface LoginValidationService {

    /**
     * Checks if user login already exists.
     * If it does, throws an {@link java.lang.IllegalStateException}, else does not do anything.
     *
     * @param login login to check
     */
    fun throwsIfUserLoginExists(login: String)

}