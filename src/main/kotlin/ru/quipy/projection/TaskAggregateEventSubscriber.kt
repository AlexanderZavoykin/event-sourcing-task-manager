package ru.quipy.projection

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.ExecutorAssignedToTaskEvent
import ru.quipy.api.ExecutorRetractedFromTaskEvent
import ru.quipy.api.TaskAggregate
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.api.TaskStatusAssignedToTaskEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(aggregateClass = TaskAggregate::class, subscriberName = "task-event-subscriber")
class TaskAggregateEventSubscriber {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @SubscribeEvent
    fun onTaskCreated(event: TaskCreatedEvent) {
        logger.info("Task created: {}", event.taskId)

        transaction {
            TaskTable.insert {
                it[id] = event.taskId
                it[projectId] = event.projectId
                it[name] = event.taskName
                it[taskStatusId] = event.defaultTaskStatusId
                it[creatorId] = event.creatorId
                it[createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun onTaskStatusAssignedToTask(event: TaskStatusAssignedToTaskEvent) {
        logger.info("Task status {} assigned to task {}", event.taskStatusId, event.taskId)

        transaction {
            TaskTable.update({ TaskTable.id eq event.taskId }) {
                it[taskStatusId] = event.taskStatusId
            }
        }
    }

    @SubscribeEvent
    fun onExecutorAssignedToTask(event: ExecutorAssignedToTaskEvent) {
        logger.info("Executor {} assigned to task {}", event.executorId, event.taskId)

        transaction {
            TaskExecutorTable.insert {
                it[taskId] = event.taskId
                it[executorId] = event.executorId
                it[createdAt] = event.createdAt
            }
        }
    }

    @SubscribeEvent
    fun onExecutorRetractedFromTask(event: ExecutorRetractedFromTaskEvent) {
        logger.info("Executor {} retracted from task {}", event.executorId, event.taskId)

        transaction {
            TaskExecutorTable.deleteWhere {
                (taskId eq event.taskId)
                    .and(executorId eq event.executorId)
            }
        }
    }

}