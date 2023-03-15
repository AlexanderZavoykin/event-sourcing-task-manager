package ru.quipy.validation

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service
import java.util.*

@Service
class SqlTaskStatusValidationService : TaskStatusValidationService {

    override fun checkNoTaskHasTaskStatus(taskStatusId: UUID) {
        val taskRow = TaskStatusTable
            .select { TaskStatusTable.taskStatusId eq taskStatusId }
            .firstOrNull()

        if (taskRow != null) {
            throw IllegalStateException("There is a task with task status $taskStatusId")
        }
    }

    fun upsertTaskStatus(taskId: UUID, taskStatusId: UUID) {
        transaction {
            val result = TaskStatusTable.update({ TaskStatusTable.taskId eq taskId }) { it[this.taskStatusId] = taskStatusId }

            if (result == 0) {
                TaskStatusTable.insert {
                    it[this.taskId] = taskId
                    it[this.taskStatusId] = taskStatusId
                }
            }
        }
    }

}