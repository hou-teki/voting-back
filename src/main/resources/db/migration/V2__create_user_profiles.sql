-- 5) USER_PROFILE
CREATE TABLE user_profiles (
  id       BIGINT PRIMARY KEY,
  age_range VARCHAR(20) DEFAULT 'UNKNOWN',
  gender VARCHAR(20) DEFAULT 'UNKNOWN',
  department   VARCHAR(100),

  CONSTRAINT fk_user_profiles_users
        FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO user_profiles (id)
SELECT id FROM users WHERE id NOT IN (SELECT id FROM user_profiles);