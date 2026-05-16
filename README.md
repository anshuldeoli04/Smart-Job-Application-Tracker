# 🎯 Smart Job Application Tracker

> A full-stack web application that helps job seekers manage applications, track interview progress, and improve resume-job fit using Google Gemini AI — built with Spring Boot + React.js.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-brightgreen?style=flat-square&logo=springboot)
![React](https://img.shields.io/badge/React-18-61DAFB?style=flat-square&logo=react)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql)
![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-3.0-38B2AC?style=flat-square&logo=tailwindcss)
![Gemini AI](https://img.shields.io/badge/Gemini_AI-Integrated-8E75B2?style=flat-square&logo=google)
![JWT](https://img.shields.io/badge/JWT-Secured-black?style=flat-square&logo=jsonwebtokens)

---

## 📌 Problem Statement

When applying to multiple companies, job seekers lose track of:
- Which companies they applied to and when
- What stage each application is at
- How well their resume matches a job description
- Interview feedback and notes

**This app solves all of that in one place.**

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔐 JWT Authentication | Secure register/login with BCrypt hashing |
| 📋 Application Tracking | Add, update, delete, filter by status or company |
| 📊 Dashboard Analytics | Total applied, interview rate, offer rate at a glance |
| 🗒️ Interview Notes | Log round-wise feedback per application |
| 🤖 AI Resume Match | Upload PDF → Gemini AI scores match, lists missing skills & suggestions |
| 🛡️ Stateless Security | JWT-based sessions, no server-side state |

---

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|-----------|---------|
| Java 17 | Core language |
| Spring Boot 4.x | REST API framework |
| Spring Security | Auth & access control |
| JWT (jjwt 0.11.5) | Token-based authentication |
| Spring Data JPA | Database ORM layer |
| MySQL 8 | Relational database |
| Google Gemini API | AI-powered resume analysis |

### Frontend
| Technology | Purpose |
|-----------|---------|
| React.js 18 | Component-based UI |
| Tailwind CSS 3 | Utility-first styling |
| Axios | HTTP client with JWT interceptor |
| React Router v6 | Protected client-side routing |
| pdfjs-dist | PDF text extraction in browser |

---

## 🗄️ Database Schema

```
┌─────────────────────────────┐
│           users             │
├─────────────────────────────┤
│ id · name · email           │
│ password · created_at       │
└─────────────┬───────────────┘
              │ 1:N
┌─────────────▼───────────────┐
│       job_applications      │
├─────────────────────────────┤
│ id · user_id (FK)           │
│ company_name · role         │
│ job_url · salary_range      │
│ status (ENUM) · applied_date│
│ job_description             │
│ created_at · updated_at     │
└──────┬──────────────┬───────┘
       │ 1:N          │ 1:N
┌──────▼──────┐ ┌─────▼──────────────┐
│  interview  │ │  ai_match_results  │
│   _notes    │ ├────────────────────┤
├─────────────┤ │ id · application_id│
│ id          │ │ match_score        │
│ application │ │ missing_skills     │
│ _id (FK)    │ │ suggestions        │
│ round_name  │ │ resume_text        │
│ feedback    │ │ created_at         │
└─────────────┘ └────────────────────┘
```

---

## 🚀 API Endpoints

### Auth — `/api/auth`
```
POST /register    → Register new user
POST /login       → Login, returns JWT token
```

### Jobs — `/api/jobs`
```
GET    /              → Get all applications
GET    /?status=X     → Filter by status
GET    /?company=X    → Search by company
POST   /              → Add new application
PUT    /{id}          → Update application
DELETE /{id}          → Delete application
```

### Notes — `/api/jobs/{id}/notes`
```
POST   /    → Add interview note
GET    /    → Get notes for application
```

### AI — `/api/ai`
```
POST /match    → Analyze resume vs job description
```

### Dashboard — `/api/dashboard`
```
GET /    → Stats: total, interview rate, offer rate
```

---

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- MySQL 8
- Node.js 18+
- Gemini API Key → [aistudio.google.com](https://aistudio.google.com)

### 1. Clone the repo
```bash
git clone https://github.com/anshuldeoli04/Smart-Job-Application-Tracker.git
cd Smart-Job-Application-Tracker
```

### 2. MySQL setup
```sql
CREATE DATABASE job_tracker;
```

### 3. Configure backend
Create `src/main/resources/application.properties`:
```properties
spring.application.name=Job_Application_Tracker
spring.datasource.url=jdbc:mysql://localhost:3306/job_tracker?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
server.port=8080
gemini.api.key=YOUR_GEMINI_KEY
app.jwt.secret=YOUR_JWT_SECRET_MIN_32_CHARS
```

### 4. Run backend
```bash
./mvnw spring-boot:run
```
✅ Backend starts at `http://localhost:8080`

### 5. Run frontend
```bash
cd job-tracker-frontend
npm install
npm start
```
✅ Frontend starts at `http://localhost:3000`

---

## 📁 Project Structure

```
Smart-Job-Application-Tracker/
│
├── src/                                  # Spring Boot Backend
│   └── main/java/.../Job_Application_Tracker/
│       ├── config/          # SecurityConfig, JwtFilter
│       ├── controller/      # Auth, Jobs, Notes, AI, Dashboard
│       ├── dto/             # Request/Response DTOs
│       ├── model/           # Entities
│       ├── repository/      # JPA Repositories
│       ├── service/         # Business Logic
│       └── util/            # JwtUtil
│
├── job-tracker-frontend/                 # React Frontend
│   └── src/
│       ├── pages/
│       │   ├── Login.jsx
│       │   ├── Register.jsx
│       │   ├── Dashboard.jsx
│       │   ├── AddJob.jsx
│       │   └── AiMatch.jsx
│       └── services/
│           └── Api.js
│
├── pom.xml
└── README.md
```

---

## 🤖 AI Feature — How It Works

```
User uploads PDF resume
        ↓
pdfjs extracts text in browser
        ↓
Text + Job Description → Spring Boot
        ↓
Spring Boot → Google Gemini API
        ↓
Gemini returns:
{
  "score": 72,
  "missingSkills": ["Docker", "Kafka"],
  "suggestions": ["Learn Docker basics"]
}
        ↓
Result saved to MySQL + shown to user
```

---

## 🔒 Security

- ✅ BCrypt password hashing
- ✅ JWT stateless authentication
- ✅ Protected routes (frontend + backend)
- ✅ CORS configured for localhost:3000
- ✅ `application.properties` excluded from Git

---

## 👤 Author

**Anshul Deoli**

[![GitHub](https://img.shields.io/badge/GitHub-anshuldeoli04-black?style=flat-square&logo=github)](https://github.com/anshuldeoli04)

---

## 📄 License

MIT License — feel free to use and modify.

---

> ⭐ If this project helped you, consider giving it a star!
