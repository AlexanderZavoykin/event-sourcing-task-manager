package ru.quipy.projection

import java.util.UUID

interface TaskProjectProjectionService {

    fun getProjectMembers(projectId: UUID): List<UserInfo>

    fun getProjectsByProjectMember(userId: UUID): List<ProjectHeader>

    fun getProjectById(projectId: UUID): ProjectDetailInfo

    fun getAllProjectsTasks(projectId: UUID): List<TaskHeader>

    fun getProjectTask(taskId: UUID): TaskDetailInfo

    fun getProjectTaskStatuses(projectId: UUID): List<TaskStatusInfo>

}