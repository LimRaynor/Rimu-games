# CLAUDE.md — Rimu Games Project Context
> This file is auto-read by Claude Code at session start. For humans, see README.md.

## What This Project Is
Indie game team promotional site. Team members log in, fill in their profile (Basic Info / Projects / Contact),
and the public dashboard shows the whole team as a card grid. No CMS — the site IS the profile editor.

## Tech Stack
| Layer     | Stack |
|-----------|-------|
| Backend   | Spring Boot 3.5.10 · Java 21 · Gradle |
| Persistence | JPA (writes/commands) + MyBatis (reads/queries) — CQRS pattern |
| Auth      | JWT access token (Authorization header) + refresh token (HttpOnly cookie) |
| Frontend  | Vue 3 · Vite · Pinia · Vue Router · Axios |
| DB        | MariaDB 10.11, Windows service, port 3306 |
| Infra     | Docker + docker-compose · ELK Stack · Prometheus + Grafana |

## Local Credentials
| Item | Value |
|------|-------|
| DB name | `rimu_games` |
| DB user | `rimu` / `yesyesme` |
| DB root | `root` / `mariadb` |
| MariaDB CLI | `"C:\Program Files\MariaDB 10.11\bin\mysql.exe" -u rimu -pyesyesme rimu_games` |
| Backend port | 8080 |
| Frontend port | 5173 (dev) / 80 (prod/docker) |

## How to Run
```bash
# Backend
cd backend && ./gradlew bootRun

# Frontend
cd frontend && npm run dev

# Health check
curl http://localhost:8080/actuator/health

# Full stack (Docker)
docker-compose up --build
```

## Package Structure (CQRS)
```
com.rimugames/
  auth/command/            signup · login · logout · refresh
  user/command/            User entity + JPA repo
  profile/command/         Profile upsert (BasicInfo + Contact)
  project/command/         Project CRUD
  dashboard/query/         MyBatis — public team list
  security/                JwtTokenProvider · JwtAuthFilter · CustomUserDetailsService
  config/                  SecurityConfig · JpaConfig
  common/dto/              ApiResponse<T>
  exception/               BusinessException · ErrorCode · GlobalExceptionHandler
```

## Frontend Structure
```
src/
  api/       auth.js · profile.js · project.js · dashboard.js · index.js (axios instance)
  stores/    auth.js · profile.js · project.js (Pinia)
  router/    index.js
  views/     auth/LoginView · auth/SignupView · dashboard/DashboardView · profile/ProfileView
  components/layout/AppHeader.vue
```

## API Surface
| Method | Path | Auth |
|--------|------|------|
| POST | `/api/auth/signup` | No |
| POST | `/api/auth/login` | No |
| POST | `/api/auth/logout` | Yes |
| POST | `/api/auth/refresh` | Cookie |
| GET/PUT | `/api/profile/me` | Yes |
| GET | `/api/projects/my` | Yes |
| POST/PUT/DELETE | `/api/projects/{id}` | Yes |
| GET | `/api/dashboard/team` | No (permitAll) |

## DB Tables
- `users` — email · password · nickname · role (MEMBER/ADMIN)
- `refresh_tokens` — refresh token store
- `profiles` — 1:1 with users (BasicInfo + Contact)
- `projects` — indie game projects (FK → users)

## Architecture Decisions (don't change without reason)
- **CQRS**: command packages use JPA, query packages use MyBatis
- **Profile = upsert**: no separate "create" step, PUT creates-or-updates
- **ApiResponse<T>** wrapper on every endpoint for consistent shape
- **Dashboard is public**: `GET /api/dashboard/team` has no auth guard — intentional
- **JWT**: access token in `Authorization: Bearer` header, refresh in HttpOnly cookie only

## Known Warnings (safe to ignore)
- `Logstash connection refused` → ELK not running locally, harmless
- `MariaDBDialect` WARN in logs → explicitly declared in yml, works fine

## Current Progress (update as work is done)
### Completed
- [x] Full backend scaffold (auth · user · profile · project · dashboard)
- [x] Full frontend scaffold (views · stores · api · router)
- [x] Docker / docker-compose / Nginx / Prometheus config
- [x] SQL DDL (`sql/rimu_games_ddl.sql`)
- [x] `backend/.env` configured

### Not Yet Done (likely next)
- [ ] E2E integration test (run both servers, test all flows manually)
- [ ] Image upload (profile avatar + project thumbnail)
- [ ] Admin panel (ADMIN role already in enum)
- [ ] Project detail public page (`/projects/:id`)
- [ ] UI polish — design system, responsive layout, toast notifications
- [ ] ELK / Grafana live integration test
- [ ] Production deployment

## Reference Project
`C:/SWCAMP/lws/be22-4st-team3-project/` — follow its patterns when in doubt.

## Files to Know
| File | Purpose |
|------|---------|
| `backend/.env` | Local secrets (never commit) |
| `backend/.env.example` | Template for new devs |
| `sql/rimu_games_ddl.sql` | Full schema |
| `backend/src/main/resources/application.yml` | Main config |
| `backend/src/main/resources/application-local.yml` | Local overrides |
| `backend/src/main/resources/mapper/DashboardMapper.xml` | MyBatis SQL |
| `docker-compose.yml` | All services |
