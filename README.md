# StudyBuddy

StudyBuddy is a microservice-based time management application designed to help students and young professionals organize their schedules effectively. It provides personalized recommendations and smart scheduling features to optimize productivity and reduce stress.

## Features

- **Personalized Scheduling:**
  - Enter your weekly schedule and receive AI-driven recommendations.
  
- **Intelligent Chatbot:**
  - Chat with an AI assistant to manage tasks and get suggestions.

- **Microservices Architecture:**
  - Scalable and flexible backend powered by Spring Boot.

- **Cloud Deployment:**
  - Hosted on AWS for high availability and reliability.

- **Monitoring and Performance Tracking:**
  - Integrated with Prometheus and Grafana to track system health.

## Architecture Overview

StudyBuddy is built using a microservices architecture, ensuring modularity and scalability. Key components include:

- **Authentication Service:** Manages user authentication and security.
- **Calendar Service:** Handles scheduling and event management.
- **Note-Taking Service:** Provides users with tools to organize their notes.
- **Chatbot Service:** Offers personalized task recommendations.

## Tech Stack

- **Backend:** Spring Boot (Java)
- **Frontend:** React.js
- **Containerization:** Docker
- **Orchestration:** Kubernetes (K8s)
- **Deployment:** ArgoCD (GitOps approach)
- **Cloud Provider:** AWS
- **Monitoring:** Prometheus & Grafana

## Installation

To run StudyBuddy locally, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/Ilyes-Kasdallah/studybuddy-app.git
   cd studybuddy-app
   ```

## Deployment

StudyBuddy is deployed using Kubernetes and ArgoCD. Steps include:

1. Define microservices using Kubernetes YAML files.
2. Automate deployment with ArgoCD.
3. Monitor with Prometheus and visualize with Grafana.

## Contributors

- **Ilyes Kasdallah**
- **Yassine Jedai**
---

For more details, refer to the project report or contact the contributors.

