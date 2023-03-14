CREATE SCHEMA IF NOT EXISTS projection;

SET SEARCH_PATH TO projection;

CREATE TABLE IF NOT EXISTS "user"
(
 id            UUID PRIMARY KEY,
 login         TEXT NOT NULL,
 password      TEXT NOT NULL,
 created_at    BIGINT NOT NULL,
 UNIQUE(login)
);

CREATE TABLE IF NOT EXISTS project
(
  id            UUID PRIMARY KEY,
  title         TEXT NOT NULL,
  created_at    BIGINT NOT NULL,
  creator_id    UUID NOT NULL REFERENCES "user"
);

CREATE TABLE IF NOT EXISTS project_member
(
  member_id       UUID NOT NULL REFERENCES "user",
  project_id      UUID NOT NULL REFERENCES project,
  created_at      BIGINT NOT NULL,
  UNIQUE(member_id, project_id)
);

CREATE TABLE IF NOT EXISTS task_status
(
  id                UUID PRIMARY KEY,
  project_id        UUID NOT NULL REFERENCES project,
  name              TEXT NOT NULL,
  created_at        BIGINT NOT NULL,
  UNIQUE(name, project_id)
);

CREATE TABLE IF NOT EXISTS task
(
  id                UUID PRIMARY KEY,
  project_id        UUID NOT NULL REFERENCES project,
  creator_id        UUID NOT NULL REFERENCES "user",
  task_status_id    UUID NOT NULL REFERENCES task_status,
  name              TEXT NOT NULL,
  created_at        BIGINT NOT NULL
);

CREATE INDEX task_task_status_idx ON task(task_status_id);

CREATE TABLE IF NOT EXISTS task_executor
(
  task_id       UUID NOT NULL REFERENCES task,
  executor_id   UUID NOT NULL REFERENCES "user",
  created_at    BIGINT NOT NULL,
  UNIQUE(executor_id, task_id)
);
