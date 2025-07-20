# MK_1_8



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://git.las.iastate.edu/cs309/2024fall/mk_1_8.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://git.las.iastate.edu/cs309/2024fall/mk_1_8/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Set auto-merge](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing (SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

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
ideas for releases in the futur. Push notifications for chat

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

git clone https://git.las.iastate.edu/cs309/2024fall/mk_1_8.git

cd mk_1_8
cd Frontend
# Open this folder in Android Studio
# Run on emulator or connected device

cd Backend
./mvnw spring-boot:run

# Push Existing Repo (if needed)
cd existing_repo
git remote add origin https://git.las.iastate.edu/cs309/2024fall/mk_1_8.git
git branch -M main
git push -uf origin main
