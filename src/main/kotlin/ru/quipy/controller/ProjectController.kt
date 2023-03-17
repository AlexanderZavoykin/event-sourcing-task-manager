package ru.quipy.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.ExecutorAssignedToTaskEvent
import ru.quipy.api.ExecutorRetractedFromTaskEvent
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.api.ProjectMemberAddedEvent
import ru.quipy.api.ProjectTitleChangedEvent
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.api.TaskStatusAssignedToTaskEvent
import ru.quipy.api.TaskStatusCreatedEvent
import ru.quipy.api.TaskStatusRemovedEvent
import ru.quipy.logic.ProjectService
import ru.quipy.projection.ProjectFullDto
import ru.quipy.projection.ProjectShortDto
import ru.quipy.projection.TaskProjectProjectionService
import ru.quipy.projection.TaskFullDto
import ru.quipy.projection.TaskShortDto
import ru.quipy.projection.TaskStatusDto
import ru.quipy.projection.UserDto
import java.util.*

@RestController
@RequestMapping("/projects")
@Tag(name = "Work with projects")
class ProjectController(
    private val projectService: ProjectService,
    private val taskProjectProjectionService: TaskProjectProjectionService,
) {

    @GetMapping
    @Operation(summary = "Get projects a user is participating (is a member of)")
    fun getProjectsByProjectMember(@RequestParam userId: UUID): List<ProjectShortDto> =
        taskProjectProjectionService.getProjectsByProjectMember(userId)

    @PostMapping("/{projectTitle}")
    @Operation(summary = "Create new project")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: UUID): ProjectCreatedEvent =
        projectService.createProject(projectTitle, creatorId)

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by its ID")
    fun getProject(@PathVariable projectId: UUID): ProjectFullDto =
        taskProjectProjectionService.getProjectById(projectId)

    @PatchMapping("/{projectId}")
    @Operation(summary = "Change title of a project")
    fun changeProjectTitle(@PathVariable projectId: UUID, @RequestParam title: String): ProjectTitleChangedEvent =
        projectService.changeProjectTitle(projectId, title)

    @PostMapping("/{projectId}/members")
    @Operation(summary = "Add a member to a project")
    fun addProjectMember(@PathVariable projectId: UUID, @RequestParam memberId: UUID): ProjectMemberAddedEvent =
        projectService.addProjectMember(projectId, memberId)

    @GetMapping("/{projectId}/members")
    @Operation(summary = "Get members of a project")
    fun getProjectMembers(@PathVariable projectId: UUID): List<UserDto> =
        taskProjectProjectionService.getProjectMembers(projectId)

    @GetMapping("/{projectId}/taskStatuses")
    @Operation(summary = "Get task statuses of a project")
    fun getProjectTaskStatuses(@PathVariable projectId: UUID): List<TaskStatusDto> =
        taskProjectProjectionService.getProjectTaskStatuses(projectId)

    @PostMapping("/{projectId}/taskStatuses")
    @Operation(summary = "Create new task status in a project")
    fun addTaskStatus(@PathVariable projectId: UUID, @RequestParam name: String): TaskStatusCreatedEvent =
        projectService.addTaskStatus(projectId, name)

    @DeleteMapping("/{projectId}/taskStatuses/{taskStatusId}")
    @Operation(summary = "Remove a task status from a project")
    fun removeTaskStatus(@PathVariable projectId: UUID, @PathVariable taskStatusId: UUID): TaskStatusRemovedEvent =
        projectService.removeTaskStatus(projectId, taskStatusId)

    @GetMapping("/{projectId}/tasks/")
    @Operation(summary = "Get tasks of a project")
    fun getAllProjectsTasks(@PathVariable projectId: UUID): List<TaskShortDto> =
        taskProjectProjectionService.getAllProjectsTasks(projectId)

    @PostMapping("/{projectId}/tasks/")
    @Operation(summary = "Create a new task in a project")
    fun createTask(
        @PathVariable projectId: UUID,
        @RequestParam taskName: String,
        @RequestParam creatorId: UUID,
    ): TaskCreatedEvent = projectService.createTask(projectId, taskName, creatorId)

    @GetMapping("/{projectId}/tasks/{taskId}")
    @Operation(summary = "Get task of a project")
    fun getProjectTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID): TaskFullDto =
        taskProjectProjectionService.getProjectTask(taskId)

    @PatchMapping("/{projectId}/tasks/{taskId}")
    @Operation(summary = "Assign a new task status to a task of a project")
    fun assignTaskStatus(
        @PathVariable projectId: UUID,
        @PathVariable taskId: UUID,
        @RequestParam taskStatusId: UUID,
    ): TaskStatusAssignedToTaskEvent = projectService.assignTaskStatus(projectId, taskId, taskStatusId)

    @PostMapping("/{projectId}/tasks/{taskId}/executors")
    @Operation(summary = "Assign a new executor to a task of a project")
    fun assignExecutor(
        @PathVariable projectId: UUID,
        @PathVariable taskId: UUID,
        @RequestParam executorId: UUID,
    ): ExecutorAssignedToTaskEvent = projectService.assignExecutor(projectId, taskId, executorId)

    @DeleteMapping("/{projectId}/tasks/{taskId}/executors")
    @Operation(summary = "Retract an executor from a task of a project")
    fun retractExecutor(
        @PathVariable projectId: UUID,
        @PathVariable taskId: UUID,
        @RequestParam executorId: UUID,
    ): ExecutorRetractedFromTaskEvent = projectService.retractExecutor(projectId, taskId, executorId)

}