package ru.quipy.projection

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(aggregateClass = ProjectAggregate::class, subscriberName = "project-event-subscriber")
class ProjectEventSubscriber {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @SubscribeEvent
    fun projectCreatedSubscriber(event: ProjectCreatedEvent) {
        logger.info("Project created {}", event)

        transaction {
            ProjectTable.insert {
                it[id] = event.projectId
                it[creatorId] = event.creatorId
                it[title] = event.title
                it[createdAt] = event.createdAt
            }
        }
    }

}