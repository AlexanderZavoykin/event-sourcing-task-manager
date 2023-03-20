CREATE SCHEMA IF NOT EXISTS validation;

SET SEARCH_PATH TO validation;

CREATE TABLE IF NOT EXISTS login
(
 login    TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS task_status
(
  task_id           UUID PRIMARY KEY,
  task_status_id    UUID NOT NULL
);