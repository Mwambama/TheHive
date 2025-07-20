# MK_1_8

***


# 🐝 The Hive – Job Swiping App  
> A CS 309 Project by Team `MK_1_8`

## 📘 Project Description

**The Hive** is a mobile-first job swiping platform – think **Tinder for internships and jobs**. Our goal is to streamline the application process for students and simplify candidate management for recruiters and companies.

The app includes:
- Swipe-based job matching
- Resume & profile management
- In-app messaging
- Company job posting
- Admin moderation
- Weekly goal tracking

---

## 🚀 Features

### For Students
- ✅ Swipe to apply or skip job listings
- ✅ Upload resumes, edit profile
- ✅ Chat with matched employers
- ✅ Weekly job application goal tracking
- ✅ Advanced job search and filters

### For Employers
- ✅ Post jobs & manage listings
- ✅ View applicants & match stats
- ✅ Chat with students
- ✅ Analytics dashboard

### For Admins
- ✅ Manage user accounts (CRUD)
- ✅ View platform-wide analytics

---

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Usage
Students sign up and create their profile.

Begin swiping through curated job listings.

Matches lead to in-app chat with employers.

Employers manage listings and view applicants.

Admin oversees all data and activity on the platform.

## Support
📧 Contact any team member via GitLab issues or direct message

🛠 Post issues in the repository issue tracker


## Roadmap
ideas for releases in the future.

Push notifications for chat

 Resume parsing for smart suggestions

 OAuth login (Google, LinkedIn)

 User reporting and moderation


## Contributing
We welcome contributions!
Please:
Fork the repository

Create a feature branch

Submit a merge request

Include tests and a clear commit message. Follow Java/Android best practices.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.


## 🧠 Architecture Diagram

See the image above for the full system overview.

- **Frontend:** Android app using Volley & WebSocket
- **Backend:** Spring Boot REST APIs + WebSocket
- **Database:** Entities like User, Application, Employer, Company, Chat
- **API Layer:** StudentApi, EmployerApi, InvitationApi, ApplicationsApi, CompanyApi
- **Controllers:** Handle CRUDL operations and authentication
- **Services & Repositories:** Bridge data persistence

---

## 🛠 Tech Stack

| Layer         | Tech Used                    |
|---------------|------------------------------|
| Frontend      | Android Studio (Java)        |
| Backend       | Spring Boot (Java)           |
| Database      | (e.g. MySQL)   |
| Networking    | Volley, WebSockets           |
| CI/CD         | GitLab Runners               |
| Version Control | Git & GitLab               |

---

## 👤 User Roles

| Role     | Capabilities |
|----------|--------------|
| Student  | Swipe jobs, Apply, Chat, Track |
| Employer | Post/manage jobs, Chat, Analytics |
| Admin    | Manage users, Analytics, Moderation |

---

## 🧑‍💻 Getting Started

### 🧾 Clone the Repository

cd mk_1_8
cd Frontend
# Open this folder in Android Studio
# Run on emulator or connected device

cd Backend
./mvnw spring-boot:run

# Push Existing Repo (if needed)

cd existing_repo

git remote add origin <repo>

git branch -M main

git push -u origin main

