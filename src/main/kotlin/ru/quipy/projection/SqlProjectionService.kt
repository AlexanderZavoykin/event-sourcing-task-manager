package ru.quipy.projection

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import ru.quipy.security.PasswordEncoder
import java.util.*

@Service
class SqlProjectionService(
    private val encoder: PasswordEncoder,
) : ProjectionService {

    override fun isAuthenticatedUser(login: String, password: String): Boolean {
        val encodedPassword = transaction {
            UserTable
                .slice(UserTable.password)
                .select { UserTable.login.eq(login) }
                .map { it[UserTable.password] }
                .first()
        }
        return encoder.verify(password, encodedPassword)
    }

    override fun getAllUsersByLoginLike(fragment: String): List<UserDto> =
        transaction {
            UserTable
                .slice(UserTable.id, UserTable.login)
                .select { UserTable.login like "%$fragment%" }
                .map { UserDto(it[UserTable.id].value, it[UserTable.login]) }
        }

    override fun getAllUsersByProjectId(projectId: UUID): List<UserDto> =
        transaction {
            (UserTable innerJoin ProjectTable)
                .slice(UserTable.id, UserTable.login)
                .select { ProjectTable.id eq projectId }
                .map { UserDto(it[UserTable.id].value, it[UserTable.login]) }
        }

    override fun getAllProjectsByUserId(userId: UUID): List<ProjectShortDto> =
        transaction {
            (ProjectTable innerJoin ProjectMemberTable)
                .slice(ProjectTable.id, ProjectTable.title, ProjectTable.createdAt)
                .select { ProjectMemberTable.memberId eq userId }
                .withDistinct()
                .map {
                    ProjectShortDto(
                        id = it[ProjectTable.id].value,
                        title = it[ProjectTable.title],
                        createdAt = it[ProjectTable.createdAt],
                    )
                }
        }

    override fun getProjectById(projectId: UUID): ProjectFullDto? =
        transaction {
            (ProjectTable innerJoin UserTable)
                .slice(ProjectTable.id, ProjectTable.title, ProjectTable.createdAt, ProjectTable.creatorId, UserTable.login)
                .select { ProjectTable.id eq projectId }
                .map {
                    ProjectFullDto(
                        id = it[ProjectTable.id].value,
                        title = it[ProjectTable.title],
                        createdAt = it[ProjectTable.createdAt],
                        creatorId = it[ProjectTable.creatorId].value,
                        creatorLogin = it[UserTable.login],
                    )
                }
                .firstOrNull()
        }

    override fun getAllTasksByProjectId(projectId: UUID): List<TaskShortDto> =
        transaction {
            (TaskTable innerJoin TaskStatusTable)
                .slice(TaskTable.id, TaskTable.name, TaskStatusTable.name)
                .select(TaskTable.projectId eq projectId)
                .map {
                    TaskShortDto(
                        id = it[TaskTable.id].value,
                        name = it[TaskTable.name],
                        taskStatusName = it[TaskStatusTable.name],
                    )
                }
        }

    override fun getTaskById(taskId: UUID): TaskFullDto? =
        transaction {
            val executors = getAllExecutorsByTaskId(taskId)

            (TaskTable innerJoin TaskStatusTable innerJoin UserTable)
                .slice(
                    TaskTable.name,
                    TaskTable.projectId,
                    TaskTable.createdAt,
                    TaskTable.creatorId,
                    UserTable.login,
                    TaskTable.taskStatusId,
                    TaskStatusTable.name,
                )
                .select(TaskTable.id eq taskId)
                .map {
                    TaskFullDto(
                        id = taskId,
                        projectId = it[TaskTable.projectId].value,
                        name = it[TaskTable.name],
                        createdAt = it[TaskTable.createdAt],
                        creatorId = it[TaskTable.creatorId].value,
                        creatorLogin = it[UserTable.login],
                        taskStatusId = it[TaskTable.taskStatusId].value,
                        taskStatusName = it[TaskStatusTable.name],
                        executors = executors,
                    )
                }
                .firstOrNull()
        }

    override fun getAllTasksStatusesByProjectId(projectId: UUID): List<TaskStatusDto> =
        transaction {
            TaskStatusTable
                .slice(TaskStatusTable.id, TaskStatusTable.name, TaskStatusTable.createdAt)
                .select { TaskStatusTable.projectId eq projectId }
                .map {
                    TaskStatusDto(
                        id = it[TaskStatusTable.id].value,
                        name = it[TaskStatusTable.name],
                        createdAt = it[TaskStatusTable.createdAt],
                    )
                }
        }

    private fun getAllExecutorsByTaskId(taskId: UUID): List<UserDto> =
        (UserTable innerJoin TaskExecutorTable)
            .slice(UserTable.id, UserTable.login)
            .select { TaskExecutorTable.taskId eq taskId }
            .map { UserDto(it[UserTable.id].value, it[UserTable.login]) }

}