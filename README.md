# FrameFest — Short Film Contest Submission & Scoring System

## Requirements
- Java 17+
- MySQL 8+
- Git (optional)

## Backend setup
1. Create a MySQL database named `shortfilm_db`.
2. Copy `src/main/resources/application-example.properties` to `src/main/resources/application.properties`.
3. Replace `YOUR_MYSQL_PASSWORD` with your local MySQL password. Do not commit `application.properties`.
4. From the project root, start the backend:
   - Windows PowerShell: `.\mvnw.cmd spring-boot:run`
   - macOS/Linux: `./mvnw spring-boot:run`
5. Backend API: `http://localhost:8080/api`

## Frontend setup
From `project-frontend`, run `py -m http.server 5500` (or use VS Code Live Server), then open `http://localhost:5500`.
The backend CORS configuration allows both `http://localhost:5500` and `http://127.0.0.1:5500`.

## Main API endpoints
- `GET/POST /api/entries`
- `GET/POST /api/judges`
- `GET/POST /api/criteria`
- `GET/POST /api/scores`
- `GET /api/results/leaderboard`

## Important
`application.properties` contains local database credentials and is intentionally excluded from Git. Use the example file as a template.
