package ru.quipy.projection.subscriber

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.projection.UserTable
import ru.quipy.security.BCryptPasswordEncoder
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(aggregateClass = UserAggregate::class, subscriberName = "user-event-subscriber")
class UserAggregateEventSubscriber(
    private val encoder: BCryptPasswordEncoder,
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @SubscribeEvent
    fun userCreatedSubscriber(event: UserCreatedEvent) {
        logger.info("User created {}", event.userId)

        transaction {
            UserTable.insert {
                it[id] = event.userId
                it[login] = event.login
                it[password] = event.password
                it[createdAt] = event.createdAt
            }
        }
    }

}