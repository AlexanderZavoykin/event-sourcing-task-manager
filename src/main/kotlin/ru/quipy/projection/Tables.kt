package ru.quipy.projection

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

const val SCHEMA = "projection"

object UserTable : UUIDTable(name = "$SCHEMA.user") {
    val login = text("login")
    val password = text("password")
    val createdAt = long("created_at")
    val loginUniqueIndex = uniqueIndex(columns = arrayOf(login))
}

object ProjectTable : UUIDTable(name = "$SCHEMA.project") {
    val title = text("title")
    val createdAt = long("created_at")
    val creatorId = reference("creator_id", UserTable.id)
}

object ProjectMemberTable : Table(name = "$SCHEMA.project_member") {
    val memberId = ProjectTable.reference("member_id", UserTable.id)
    val projectId = ProjectTable.reference("project_id", ProjectTable.id)
    val createdAt = UserTable.long("created_at")
    val memberIdProjectIdUniqueIndex = uniqueIndex(columns = arrayOf(memberId, projectId))
}

object TaskStatusTable : UUIDTable(name = "$SCHEMA.task_status") {
    val projectId = ProjectTable.reference("project_id", ProjectTable.id)
    val name = text("name")
    val createdAt = long("created_at")
    val nameProjectIdUniqueIndex = uniqueIndex(columns = arrayOf(name, projectId))
}

object TaskTable : UUIDTable(name = "$SCHEMA.task") {
    val projectId = reference("project_id", ProjectTable.id)
    val creatorId = reference("creator_id", UserTable.id)
    val taskStatusId = reference("task_status_id", TaskStatusTable.id)
    val name = text("name")
    val createdAt = long("created_at")
    val taskStatusIndex = index(customIndexName = "task_task_status_idx", columns = arrayOf(taskStatusId))
}

object TaskExecutorTable : UUIDTable(name = "$SCHEMA.task_executor") {
    val taskId = reference("task_id", TaskTable.id)
    val executorId = reference("executor_id", UserTable.id)
    val createdAt = long("created_at")
    val executorIdTaskIdUniqueIndex = uniqueIndex(columns = arrayOf(executorId, taskId))
}