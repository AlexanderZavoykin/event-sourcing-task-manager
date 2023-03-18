package ru.quipy.validation

import java.util.UUID

/**
 * Checks if any assignment of a task status to any task exists by
 * keeping and updating assignments in a store (it could be any store such as database, file, etc.)
 */
interface StoredTaskStatusValidationService {

    /**
     * Checks if task status assigned to some task.
     * If it is, throws an {@link java.lang.IllegalStateException}, else does not do anything.
     *
     * @param taskStatusId ID of task status to check
     */
    fun throwIfTaskStatusAssigned(taskStatusId: UUID)

    /**
     * Updates task status assignment to a task in a store maintainable by this instance.
     *
     * @param taskId ID of a task to which task status is assigned
     * @param taskStatusId ID of task status assigned to a task
     */
    fun updateTaskStatusAssignment(taskId: UUID, taskStatusId: UUID)

}