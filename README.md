# Kiraflow (Agile Management Tool)

## Overview
Kiraflow is a lightweight Jira-like project management tool with:
- **Backend**: Spring Boot + MySQL + JWT
- **Frontend**: React (planned)
- **Deployment**: Cloud (AWS)

---

## System Architecture
Below is the entity-relationship diagram (ERD):

![Kiraflow ERD](Documentation/Untitled%20diagram%20_%20Mermaid%20Chart-2025-09-12-164923.svg)

---

## Tech Stack
- **Backend**: Java 17, Spring Boot, Hibernate, Spring Security, JWT  
- **Database**: MySQL 8.x  
- **Frontend**: React + Tailwind/Material UI (planned)  
- **Cloud Deployment**: AWS (EC2 / ECS / RDS / S3 for attachments)  

---

## Backend Documentation
See [Backend Reference](./docs/README.md) for:
- API endpoints
- Database schema
- Entity relationships
- Auth & permissions
- Postman tests

---

## Frontend (React)
- Framework: **React + Vite** (or CRA)  
- State management: **Redux Toolkit** (optional, or React Query)  
- Auth: store JWT in `localStorage` or `HttpOnly cookie`  
- API integration: Axios or Fetch hitting `/api/...` endpoints  
- Planned UI:  
  - Organization / Project switcher  
  - Board view (columns & tasks as drag/drop)  
  - Epic & sprint overview  
  - Task details (labels, comments, attachments)  



## 🚀 Backend Deployment (AWS)

Deployed on **AWS** with:

* **AMI** (Spring Boot JAR + JDK 17)
* **Auto Scaling Group (ASG)** for scaling
* **Application Load Balancer (ALB)** for traffic
* **RDS (MySQL)** for database
* **Secrets Manager** for DB creds & JWT

**CI/CD flow:** build JAR → bake AMI → refresh ASG → serve via ALB.

All resources were later deleted to avoid charges.

---



## Future Enhancements
- Sprint burndown charts
- Real-time updates via WebSockets
- Notifications system
- Role-based permissions per project
