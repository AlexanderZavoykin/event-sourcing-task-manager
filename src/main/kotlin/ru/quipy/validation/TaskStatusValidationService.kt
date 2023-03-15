package ru.quipy.validation

import java.util.*

interface TaskStatusValidationService {

    fun checkNoTaskHasTaskStatus(taskStatusId: UUID)

}