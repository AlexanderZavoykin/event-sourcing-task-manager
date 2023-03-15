package ru.quipy.projection

import java.util.*

interface ProjectionService {

    fun isAuthenticatedUser(login: String, password: String): Boolean

    fun getAllUsersByLoginLike(fragment: String): List<UserDto>

    fun getAllUsersByProjectId(projectId: UUID): List<UserDto>

    fun getAllProjectsByUserId(userId: UUID): List<ProjectShortDto>

    fun getProjectById(projectId: UUID): ProjectFullDto?

    fun getAllTasksByProjectId(projectId: UUID): List<TaskShortDto>

    fun getTaskById(taskId: UUID): TaskFullDto?

    fun getAllTasksStatusesByProjectId(projectId: UUID): List<TaskStatusDto>

}