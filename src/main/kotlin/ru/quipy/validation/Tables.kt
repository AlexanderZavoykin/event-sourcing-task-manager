package ru.quipy.validation

import org.jetbrains.exposed.sql.Table

const val VALIDATION_SCHEMA = "validation"

object LoginTable : Table(name = "$VALIDATION_SCHEMA.login") {
    val login = text("login")
}

object TaskStatusTable : Table(name = "$VALIDATION_SCHEMA.task_status") {
    val taskId = uuid("task_id")
    val taskStatusId = uuid("task_status_id")
}