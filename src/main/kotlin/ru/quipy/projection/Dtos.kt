package ru.quipy.projection

import java.util.*

data class UserDto(
    val id: UUID,
    val login: String,
)

data class ProjectShortDto(
    val id: UUID,
    val title: String,
    val createdAt: Long,
)

data class ProjectFullDto(
    val id: UUID,
    val title: String,
    val createdAt: Long,
    val creatorId: UUID,
    val creatorLogin: String,
)

data class TaskShortDto(
    val id: UUID,
    val name: String,
    val taskStatusName: String,
)

data class TaskFullDto(
    val id: UUID,
    val projectId: UUID,
    val name: String,
    val createdAt: Long,
    val creatorId: UUID,
    val creatorLogin: String,
    val taskStatusId: UUID,
    val taskStatusName: String,
    val executors: List<UserDto>,
)

data class TaskStatusDto(
    val id: UUID,
    val name: String,
    val createdAt: Long,
)