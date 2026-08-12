# 🎓 CampusHub

> A full-stack Smart Campus Management Platform built with React, Spring Boot, Spring Security, JWT, and PostgreSQL.

CampusHub is a role-based campus management system designed to bring common academic and administrative workflows into a single platform.

The system provides separate experiences for **Administrators, Faculty members, and Students**, with secure authentication and role-based access to academic data and operations.

---

## 📌 Overview

CampusHub provides a centralized platform for managing:

- Departments
- Students
- Faculty
- Courses
- Student-course enrollment
- Attendance
- Marks
- Grades
- Academic results
- SGPA and CGPA
- Student reports
- Academic analytics

The application follows a layered backend architecture with a React-based frontend communicating with Spring Boot REST APIs.

---

## ✨ Key Features

### 🔐 Authentication & Security

- JWT-based authentication
- Role-Based Access Control (RBAC)
- Roles:
  - `ADMIN`
  - `FACULTY`
  - `STUDENT`
- Password encryption using Spring Security
- Stateless authentication
- Protected REST APIs
- Method-level authorization using `@PreAuthorize`
- Automatic handling of expired/invalid JWT sessions
- Input validation
- Global exception handling

---

## 👨‍💼 Admin Module

Administrators have access to the complete academic management system.

**Dashboard**
- Total students
- Total faculty
- Total departments
- Total courses
- Attendance records
- Marks records

**Department Management**
- Create departments
- View departments
- Update departments
- Delete departments using soft delete

**Student Management**
- Create students
- View students
- Update students
- Delete students
- Search students
- Filter students
- Pagination
- Student profile information

**Faculty Management**
- Create faculty
- View faculty
- Update faculty
- Delete faculty
- Search and filter faculty
- Faculty profile information

**Course Management**
- Create courses
- View courses
- Update courses
- Delete courses
- Search courses
- Filter courses
- Pagination
- Assign faculty to courses
- Associate courses with departments
- Maintain semester and academic year information

**Student Enrollment**
- Enroll students into courses
- View course enrollments
- Remove course enrollment
- Maintain student-course relationships

**Attendance**
- View attendance records
- View attendance by course and date
- Monitor attendance status

**Marks**
- View marks records
- View marks by course
- Monitor student academic performance

**Analytics**
- Student reports
- Topper information
- Pass percentage
- Average marks
- Course average marks
- Attendance percentage
- SGPA/CGPA information

---

## 👨‍🏫 Faculty Module

Faculty members have access to their assigned academic activities.

**Faculty Dashboard**
- Faculty profile information
- Employee ID
- Department
- Designation
- Email
- Assigned courses
- Students associated with assigned courses
- Attendance records
- Marks uploaded

**Course Management**
- View assigned courses
- View students enrolled in assigned courses

**Attendance Management**
- View course students
- Mark attendance
- Bulk attendance marking
- Update attendance
- View attendance records

**Marks Management**
- View students for assigned courses
- Enter academic marks
- Manage IA1, IA2 and ESA marks
- Calculate total marks and grades
- Update marks
- View marks records

---

## 👨‍🎓 Student Module

Students can access their own academic information.

**Student Dashboard**
- Student profile information
- Department
- Semester
- Academic information
- Enrolled courses

**My Courses**
- View enrolled courses
- Course code
- Course name
- Credits
- Semester
- Academic year
- Faculty information

**My Attendance**
- View attendance records
- Course-wise attendance records
- Attendance dates
- Present/Absent status
- Attendance remarks
- Overall attendance percentage

**My Marks**
- View marks for enrolled courses
- IA1 marks
- IA2 marks
- ESA marks
- Total marks
- Grade
- Grade point

**Results**
- Semester-wise SGPA
- Overall CGPA
- Total credits
- Semester credit information
- Academic performance by course

---

## 📊 Academic Result Calculation

CampusHub calculates SGPA and CGPA using a credit-weighted approach.

For semester GPA:

```
SGPA = Σ(Credit × Grade Point) / Σ(Credits)
```

The system groups marks according to the semester associated with each course and calculates the corresponding SGPA.

Overall CGPA is calculated using the credit-weighted grade points across the student's available academic records.

---

## 🏗️ Architecture

CampusHub follows a layered architecture on the backend.

```
                    React Frontend
                          │
                          ▼
                   Axios REST Client
                          │
                          ▼
                Spring Boot REST APIs
                          │
                          ▼
                Spring Security + JWT
                          │
                          ▼
                    Controllers
                          │
                          ▼
                     Services
                          │
                          ▼
                   Repositories
                          │
                          ▼
                    JPA / Hibernate
                          │
                          ▼
                    PostgreSQL
```

**Backend Layers**

```
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
PostgreSQL
```

DTOs are used for API request and response handling, keeping API models separate from persistence entities.

---

## 🛠️ Technology Stack

**Backend**
- Java 21
- Spring Boot 4.1.0
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- Spring Validation
- Spring Mail
- Maven
- Lombok

**Frontend**
- React 19
- JavaScript
- React Router
- Axios
- Bootstrap 5
- Vite

**Database**
- PostgreSQL

**API Documentation**
- Swagger / OpenAPI
- Springdoc OpenAPI

**Development Tools**
- Git
- GitHub
- Postman
- Visual Studio Code
- Maven

---

## 📁 Project Structure

```
CampusHub
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/preeti/campushub/
│       │       ├── common/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── enums/
│       │       ├── exception/
│       │       ├── repository/
│       │       ├── security/
│       │       ├── service/
│       │       └── util/
│       │
│       └── resources/
│           └── application.properties
│
├── campushub-frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── routes/
│   │   └── services/
│   │
│   ├── package.json
│   └── vite.config.js
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## 🗄️ Database Model

The main entities are:

```
User
 │
 ├── Student
 └── Faculty

Department
 │
 ├── Student
 ├── Faculty
 └── Course

Faculty
 │
 └── Course

Student
 │
 └── StudentCourse
          │
          └── Course

Student
 │
 ├── Attendance
 └── Marks

Course
 │
 ├── Attendance
 └── Marks
```

**Main Database Entities**
- users
- students
- faculties
- departments
- courses
- student_courses
- attendance
- marks

---

## 🔄 Application Workflow

A typical academic workflow is:

```
Admin
  │
  ├── Creates Department
  │
  ├── Creates Faculty
  │
  ├── Creates Students
  │
  ├── Creates Courses
  │
  └── Enrolls Students into Courses
             │
             ▼
          Faculty
             │
             ├── Views Assigned Courses
             ├── Views Students
             ├── Marks Attendance
             └── Uploads Marks
                     │
                     ▼
                   Student
                     │
                     ├── Views Courses
                     ├── Views Attendance
                     ├── Views Marks
                     └── Views SGPA / CGPA / Results
```

---

## 🔌 REST API

The backend exposes REST APIs under:

```
/api
```

Examples include:

```
/api/auth
/api/students
/api/faculties
/api/departments
/api/courses
/api/student-courses
/api/attendance
/api/marks
/api/analytics
/api/dashboard
/api/faculty/dashboard
```

Swagger/OpenAPI documentation is available through the Springdoc configuration.

When running locally:

```
http://localhost:8081/swagger-ui.html
```

---

## 🚀 Getting Started

### Prerequisites

Make sure the following are installed:

- Java 21
- Maven
- Node.js and npm
- PostgreSQL
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/CampusHub.git
cd CampusHub
```

### 2. Configure PostgreSQL

Create a PostgreSQL database named:

```
campushub
```

Example:

```sql
CREATE DATABASE campushub;
```

### 3. Configure Environment Variables

CampusHub uses environment variables for database and email credentials.

Configure:

```
DB_USERNAME
DB_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
```

> Do not commit `.env` files or credentials to GitHub.

### 4. Start the Backend

From the project root:

**Windows**
```bash
mvn spring-boot:run
```

The backend runs on:

```
http://localhost:8081
```

### 5. Start the Frontend

Open a new terminal:

```bash
cd campushub-frontend
npm install
npm run dev
```

The frontend runs on:

```
http://localhost:5173
```

---

## 🔑 Authentication Flow

1. User logs in through the React frontend.
2. Credentials are sent to the Spring Boot authentication API.
3. Spring Security authenticates the user.
4. A JWT token is generated.
5. The frontend stores the token.
6. Axios attaches the JWT to protected API requests.
7. Spring Security validates the token.
8. Role-based authorization determines whether the user can access the requested operation.

```
Login
  ↓
Authentication
  ↓
JWT Token
  ↓
Frontend
  ↓
Authorization Header
  ↓
JWT Filter
  ↓
Spring Security
  ↓
Role-Based Access
```

---

## 🛡️ Validation & Error Handling

The backend includes:

- Jakarta Bean Validation
- Duplicate email validation
- Duplicate USN validation
- Duplicate course code validation
- Duplicate employee ID validation
- Duplicate attendance prevention
- Duplicate marks prevention
- Resource-not-found handling
- Global exception handling
- Standardized API responses

---

## 🗑️ Soft Delete

Major academic records use an active flag for soft deletion.

Instead of immediately removing a record from the database, the system can mark it inactive. This helps preserve existing academic data while preventing inactive records from appearing in normal active-record queries.

---

## 📸 Screenshots

The application includes dedicated interfaces for:

- Admin Dashboard
- Faculty Dashboard
- Student Dashboard
- Department Management
- Student Management
- Faculty Management
- Course Management
- Student Enrollment
- Attendance Management
- Marks Management
- Student Attendance
- Student Marks
- Student Results
- SGPA / CGPA

---

## 🎯 Project Objectives

The main objectives of CampusHub were to gain practical experience in:

- Full-stack application development
- REST API development
- Spring Boot backend architecture
- Authentication and authorization
- Database design and relationships
- JPA/Hibernate
- React application development
- Role-based workflows
- Academic business logic
- API integration
- Validation and exception handling
- Building a multi-module application

---

## 📚 What I Learned

Building CampusHub helped strengthen my understanding of:

- Designing backend layers using Controller-Service-Repository architecture
- Creating RESTful APIs
- Implementing JWT authentication
- Implementing role-based authorization
- Designing relational database relationships
- Working with JPA and Hibernate
- Connecting React applications with REST APIs
- Handling API errors and validation
- Implementing academic business logic
- Managing state and routing in React
- Integrating frontend and backend systems

---

## 🔮 Possible Future Enhancements

The current version focuses on the core campus management workflow.

Possible future enhancements include:

- Course-wise and semester-wise attendance analytics
- More advanced academic history tracking
- Notifications and alerts
- CSV/PDF report exports
- Advanced charts and visual analytics
- Profile management improvements
- Cloud deployment
- Production database configuration
- Additional automated tests
- More granular academic workflows

---

## 👩‍💻 Author

**Preeti Rajkumar Kotabagi**

GitHub: [https://github.com/preetikotabagi](https://github.com/preetikotabagi)

---

## ⭐ Project

If you find CampusHub interesting, feel free to explore the repository and share feedback.

Built as a hands-on full-stack project to understand how a multi-role academic management system can be designed and implemented.
