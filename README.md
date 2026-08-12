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

# ✨ Key Features

## 🔐 Authentication & Security

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

# 👨‍💼 Admin Module

Administrators have access to the complete academic management system.

### Dashboard

- Total students
- Total faculty
- Total departments
- Total courses
- Attendance records
- Marks records

### Department Management

- Create departments
- View departments
- Update departments
- Delete departments using soft delete

### Student Management

- Create students
- View students
- Update students
- Delete students
- Search students
- Filter students
- Pagination
- Student profile information

### Faculty Management

- Create faculty
- View faculty
- Update faculty
- Delete faculty
- Search and filter faculty
- Faculty profile information

### Course Management

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

### Student Enrollment

- Enroll students into courses
- View course enrollments
- Remove course enrollment
- Maintain student-course relationships

### Attendance

- View attendance records
- View attendance by course and date
- Monitor attendance status

### Marks

- View marks records
- View marks by course
- Monitor student academic performance

### Analytics

- Student reports
- Topper information
- Pass percentage
- Average marks
- Course average marks
- Attendance percentage
- SGPA/CGPA information

---

# 👨‍🏫 Faculty Module

Faculty members have access to their assigned academic activities.

### Faculty Dashboard

- Faculty profile information
- Employee ID
- Department
- Designation
- Email
- Assigned courses
- Students associated with assigned courses
- Attendance records
- Marks uploaded

### Course Management

- View assigned courses
- View students enrolled in assigned courses

### Attendance Management

- View course students
- Mark attendance
- Bulk attendance marking
- Update attendance
- View attendance records

### Marks Management

- View students for assigned courses
- Enter academic marks
- Manage IA1, IA2 and ESA marks
- Calculate total marks and grades
- Update marks
- View marks records

---

# 👨‍🎓 Student Module

Students can access their own academic information.

### Student Dashboard

- Student profile information
- Department
- Semester
- Academic information
- Enrolled courses

### My Courses

- View enrolled courses
- Course code
- Course name
- Credits
- Semester
- Academic year
- Faculty information

### My Attendance

- View attendance records
- Course-wise attendance records
- Attendance dates
- Present/Absent status
- Attendance remarks
- Overall attendance percentage

### My Marks

- View marks for enrolled courses
- IA1 marks
- IA2 marks
- ESA marks
- Total marks
- Grade
- Grade point

### Results

- Semester-wise SGPA
- Overall CGPA
- Total credits
- Semester credit information
- Academic performance by course

---

# 📊 Academic Result Calculation

CampusHub calculates SGPA and CGPA using a credit-weighted approach.

For semester GPA:

```text
SGPA = Σ(Credit × Grade Point) / Σ(Credits)
