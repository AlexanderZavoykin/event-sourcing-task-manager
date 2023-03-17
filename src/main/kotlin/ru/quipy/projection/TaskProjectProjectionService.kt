package ru.quipy.projection

import java.util.UUID

interface TaskProjectProjectionService {

    fun getProjectMembers(projectId: UUID): List<UserDto>

    fun getProjectsByProjectMember(userId: UUID): List<ProjectShortDto>

    fun getProjectById(projectId: UUID): ProjectFullDto

    fun getAllProjectsTasks(projectId: UUID): List<TaskShortDto>

    fun getProjectTask(taskId: UUID): TaskFullDto

    fun getProjectTaskStatuses(projectId: UUID): List<TaskStatusDto>

}