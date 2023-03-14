package ru.quipy.projection.subscriber

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.api.ProjectMemberAddedEvent
import ru.quipy.api.ProjectTitleChangedEvent
import ru.quipy.api.TaskStatusCreatedEvent
import ru.quipy.api.TaskStatusRemovedEvent
import ru.quipy.projection.ProjectMemberTable
import ru.quipy.projection.ProjectTable
import ru.quipy.projection.TaskStatusTable
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(aggregateClass = ProjectAggregate::class, subscriberName = "project-event-subscriber")
class ProjectAggregateEventSubscriber {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @SubscribeEvent
    fun onProjectCreated(event: ProjectCreatedEvent) {
        logger.info("Project created: {}", event.projectId)

        transaction {
            ProjectTable.insert {
                it[id] = event.projectId
                it[creatorId] = event.creatorId
                it[title] = event.title
                it[createdAt] = event.createdAt
            }

            ProjectMemberTable.insert {
                it[projectId] = event.projectId
                it[memberId] = event.creatorId
                it[createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun onProjectTitleChanged(event: ProjectTitleChangedEvent) {
        logger.info("Title changed for project {}", event.projectId)

        transaction {
            ProjectTable.update({ ProjectTable.id eq event.projectId }) {
                it[title] = event.title
            }
        }
    }

    @SubscribeEvent
    fun onProjectMemberAdded(event: ProjectMemberAddedEvent) {
        logger.info("Project member {} added to project {}", event.memberId, event.projectId)

        transaction {
            ProjectMemberTable.insert {
                it[projectId] = event.projectId
                it[memberId] = event.memberId
                it[createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun onTaskStatusCreated(event: TaskStatusCreatedEvent) {
        logger.info("Task status created: {}", event.projectId)

        transaction {
            TaskStatusTable.insert {
                it[projectId] = event.projectId
                it[name] = event.taskStatusName
                it[ProjectMemberTable.createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun onTaskStatusRemoved(event: TaskStatusRemovedEvent) {
        logger.info("Task status {} removed from project {}", event.taskStatusId, event.projectId)

        transaction {
            TaskStatusTable.deleteWhere { id eq event.taskStatusId }
        }
    }

}