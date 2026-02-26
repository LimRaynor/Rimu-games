CREATE DATABASE IF NOT EXISTS rimu_games
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE rimu_games;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    nickname    VARCHAR(50)     NOT NULL,
    role        VARCHAR(20)     NOT NULL DEFAULT 'USER',
    created_at  DATETIME(6)     NOT NULL,
    updated_at  DATETIME(6)     NOT NULL,
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 리프레시 토큰 테이블
CREATE TABLE IF NOT EXISTS refresh_tokens (
    user_email  VARCHAR(100)    NOT NULL PRIMARY KEY,
    token       VARCHAR(512)    NOT NULL,
    expiry_date DATETIME        NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 프로필 테이블 (Basic Info + Contact)
CREATE TABLE IF NOT EXISTS profiles (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT          NOT NULL UNIQUE,
    name        VARCHAR(100),
    role        VARCHAR(100),
    bio         TEXT,
    avatar_url  VARCHAR(255),
    email       VARCHAR(100),
    github_url  VARCHAR(255),
    website_url VARCHAR(255),
    discord_tag VARCHAR(100),
    created_at  DATETIME(6)     NOT NULL,
    updated_at  DATETIME(6)     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_profiles_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 프로젝트 테이블
CREATE TABLE IF NOT EXISTS projects (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    title           VARCHAR(200)    NOT NULL,
    description     TEXT,
    thumbnail_url   VARCHAR(255),
    project_url     VARCHAR(255),
    repo_url        VARCHAR(255),
    tech_stack      VARCHAR(200),
    status          VARCHAR(20),
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_projects_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
