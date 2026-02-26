# 🎮 Rimu Games

인디게임팀 홍보 및 포트폴리오 사이트입니다.
팀원들이 로그인 후 자신의 프로필(Basic Info, Project, Contact)을 등록하면,
메인 대시보드에서 팀 전체 소개를 확인할 수 있습니다.

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Spring Boot 3.5.10, Java 21, Spring Security, JPA, MyBatis |
| Frontend | Vue 3, Vite, Pinia, Vue Router, Axios |
| Database | MariaDB 10.11 |
| 인증 | JWT (Access Token + Refresh Token Cookie) |
| 인프라 | Docker, docker-compose, ELK Stack, Prometheus, Grafana |

---

## 프로젝트 구조

```
Rimu/
├── backend/                  # Spring Boot 백엔드
│   ├── src/main/java/com/rimugames/
│   │   ├── auth/             # 로그인, 회원가입, 토큰 갱신
│   │   ├── user/             # 사용자 엔티티
│   │   ├── profile/          # 프로필 (Basic Info + Contact)
│   │   ├── project/          # 인디게임 프로젝트
│   │   ├── dashboard/        # 팀 소개 (공개 API)
│   │   ├── config/           # Security, JPA 설정
│   │   ├── security/         # JWT 필터, 핸들러
│   │   ├── exception/        # 전역 예외처리
│   │   └── common/           # 공통 응답 DTO
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-local.yml
│   │   └── mapper/           # MyBatis XML
│   ├── build.gradle
│   ├── .env                  # 환경변수 (DB, JWT) ← 직접 생성 필요
│   └── Dockerfile
├── frontend/                 # Vue 3 프론트엔드
│   ├── src/
│   │   ├── views/            # 페이지 컴포넌트
│   │   ├── stores/           # Pinia 상태관리
│   │   ├── api/              # Axios API 모듈
│   │   ├── router/           # Vue Router
│   │   └── components/       # 공통 컴포넌트
│   ├── package.json
│   ├── vite.config.js
│   ├── nginx.conf
│   └── Dockerfile
├── sql/
│   └── rimu_games_ddl.sql    # DB 스키마
├── docker/
│   └── prometheus/
│       └── prometheus.yml
└── docker-compose.yml
```

---

## 페이지 구성

| 경로 | 설명 | 인증 필요 |
|------|------|-----------|
| `/login` | 로그인 | X |
| `/signup` | 회원가입 | X |
| `/` | 대시보드 (팀 소개) | O |
| `/profile` | 마이페이지 (Basic Info / Project / Contact) | O |

---

## API 목록

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/auth/signup` | 회원가입 | X |
| POST | `/api/auth/login` | 로그인 | X |
| POST | `/api/auth/logout` | 로그아웃 | O |
| POST | `/api/auth/refresh` | 토큰 갱신 | X |
| GET | `/api/profile/me` | 내 프로필 조회 | O |
| PUT | `/api/profile/me` | 내 프로필 수정 | O |
| GET | `/api/projects/my` | 내 프로젝트 목록 | O |
| POST | `/api/projects` | 프로젝트 등록 | O |
| PUT | `/api/projects/{id}` | 프로젝트 수정 | O |
| DELETE | `/api/projects/{id}` | 프로젝트 삭제 | O |
| GET | `/api/dashboard/team` | 팀원 목록 (공개) | X |

---

## 로컬 개발 환경 설정

### 1. DB 설정

#### MariaDB 설치 확인
- 버전: MariaDB 10.11
- 포트: 3306

#### DB 및 계정 생성 (root로 접속 후 실행)
```sql
CREATE DATABASE IF NOT EXISTS rimu_games
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'rimu'@'localhost' IDENTIFIED BY 'yesyesme';
GRANT ALL PRIVILEGES ON rimu_games.* TO 'rimu'@'localhost';
FLUSH PRIVILEGES;
```

#### 테이블 생성
```bash
mysql -u root -p rimu_games < sql/rimu_games_ddl.sql
```

#### DB 접속 방법 (CLI)
```bash
# Windows
"C:\Program Files\MariaDB 10.11\bin\mysql.exe" -u rimu -pyesyesme rimu_games

# 접속 후 유용한 명령어
SHOW TABLES;
DESC users;
SELECT * FROM users;
EXIT;
```

#### DB 접속 정보 (GUI 툴 사용 시)
| 항목 | 값 |
|------|----|
| Host | localhost |
| Port | 3306 |
| Database | rimu_games |
| Username | rimu |
| Password | yesyesme |

---

### 2. 백엔드 설정

#### 환경변수 파일 생성
`backend/.env` 파일을 생성합니다. (`backend/.env.example` 참고)

```env
DB_URL=jdbc:mariadb://localhost:3306/rimu_games?useUnicode=true&characterEncoding=utf8
DB_USERNAME=rimu
DB_PASSWORD=yesyesme
JWT_SECRET=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
```

#### 실행
```bash
cd backend
./gradlew bootRun
```

백엔드: http://localhost:8080

#### 헬스체크 확인
```bash
curl http://localhost:8080/actuator/health
# 정상: {"status":"UP","components":{"db":{"status":"UP",...}}}
```

---

### 3. 프론트엔드 설정

```bash
cd frontend
npm install
npm run dev
```

프론트엔드: http://localhost:5173

> Vite 개발 서버가 `/api` 요청을 자동으로 `http://localhost:8080`으로 프록시합니다.

---

## Docker로 전체 실행

```bash
# backend/.env 파일이 있어야 합니다
docker-compose up --build
```

| 서비스 | 포트 |
|--------|------|
| Frontend (Nginx) | http://localhost:80 |
| Backend (Spring Boot) | http://localhost:8080 |
| Kibana (로그) | http://localhost:5601 |
| Grafana (메트릭) | http://localhost:3000 |
| Prometheus | http://localhost:9090 |

---

## 참고사항

- Logstash 연결 WARN 메시지는 ELK 스택 미실행 시 정상 출력되며 기능에 영향 없음
- 프로필은 upsert 방식으로 동작 (없으면 생성, 있으면 수정)
- 대시보드 팀원 목록은 로그인 없이도 접근 가능한 공개 API
