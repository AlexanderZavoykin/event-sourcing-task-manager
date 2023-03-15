package ru.quipy.projection

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

const val PROJECTION_SCHEMA = "projection"

object UserTable : UUIDTable(name = "$PROJECTION_SCHEMA.user") {
    val login = text("login")
    val password = text("password")
    val createdAt = long("created_at")
}

object ProjectTable : UUIDTable(name = "$PROJECTION_SCHEMA.project") {
    val title = text("title")
    val createdAt = long("created_at")
    val creatorId = reference("creator_id", UserTable.id)
}

object ProjectMemberTable : Table(name = "$PROJECTION_SCHEMA.project_member") {
    val memberId = reference("member_id", UserTable.id)
    val projectId = reference("project_id", ProjectTable.id)
    val createdAt = long("created_at")
}

object TaskStatusTable : UUIDTable(name = "$PROJECTION_SCHEMA.task_status") {
    val projectId = reference("project_id", ProjectTable.id)
    val name = text("name")
    val createdAt = long("created_at")
}

object TaskTable : UUIDTable(name = "$PROJECTION_SCHEMA.task") {
    val projectId = reference("project_id", ProjectTable.id)
    val creatorId = reference("creator_id", UserTable.id)
    val taskStatusId = reference("task_status_id", TaskStatusTable.id)
    val name = text("name")
    val createdAt = long("created_at")
}

object TaskExecutorTable : Table(name = "$PROJECTION_SCHEMA.task_executor") {
    val taskId = reference("task_id", TaskTable.id)
    val executorId = reference("executor_id", UserTable.id)
    val createdAt = long("created_at")
}