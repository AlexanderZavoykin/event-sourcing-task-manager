package ru.quipy.validation

import java.util.*

interface TaskStatusValidationService {

    fun checkTaskIsRemovable(taskStatusId: UUID)

}