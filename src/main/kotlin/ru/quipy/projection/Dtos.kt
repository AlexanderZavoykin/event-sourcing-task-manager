package ru.quipy.projection

import java.util.*

data class UserInfo(
    val id: UUID,
    val login: String,
)

data class ProjectHeader(
    val id: UUID,
    val title: String,
    val createdAt: Long,
)

data class ProjectDetailInfo(
    val id: UUID,
    val title: String,
    val createdAt: Long,
    val creatorId: UUID,
    val creatorLogin: String,
)

data class TaskHeader(
    val id: UUID,
    val name: String,
    val taskStatusName: String,
)

data class TaskDetailInfo(
    val id: UUID,
    val projectId: UUID,
    val name: String,
    val createdAt: Long,
    val creatorId: UUID,
    val creatorLogin: String,
    val taskStatusId: UUID,
    val taskStatusName: String,
    val executors: List<UserInfo>,
)

data class TaskStatusInfo(
    val id: UUID,
    val name: String,
    val createdAt: Long,
)