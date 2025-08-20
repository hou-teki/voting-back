-- 1) USER
CREATE TABLE users (
  id       BIGSERIAL PRIMARY KEY,
  username VARCHAR(50)  NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL
);

-- 2) VOTE
CREATE TABLE votes (
  id           BIGSERIAL PRIMARY KEY,
  title        VARCHAR(100) NOT NULL,
  description  TEXT,
  creator_id   BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  start_date   DATE         NOT NULL,
  end_date     DATE
);

-- 3) OPTION
CREATE TABLE options (
  id       BIGSERIAL PRIMARY KEY,
  vote_id  BIGINT        NOT NULL REFERENCES votes(id) ON DELETE CASCADE,
  label    VARCHAR(100)  NOT NULL
);

-- 4) RECORD
CREATE TABLE records (
  id        BIGSERIAL PRIMARY KEY,
  user_id   BIGINT   NOT NULL REFERENCES users(id)   ON DELETE CASCADE,
  vote_id   BIGINT   NOT NULL REFERENCES votes(id)   ON DELETE CASCADE,
  option_id BIGINT   NOT NULL REFERENCES options(id) ON DELETE CASCADE,

  CONSTRAINT uq_records_user_vote UNIQUE (user_id, vote_id)
);