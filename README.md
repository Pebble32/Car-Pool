# CarPool Backend

## Table of Contents

- [Introduction](#introduction)
- [Vision and Scope](#vision-and-scope)
  - [Vision Statement](#vision-statement)
  - [Business Opportunity](#business-opportunity)
  - [Major Features](#major-features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
    - [Running with Docker](#running-with-docker)
    - [Running Locally](#running-locally)
- [API Documentation](#api-documentation)
- [Real-Time Notifications](#real-time-notifications)
- [License](#license)

## Introduction

Welcome to the **CarPool Backend** repository—the server-side component of the CarPool application designed to facilitate sustainable and convenient commuting for students and staff at **Háskóli Íslands**. This backend service manages user authentication, ride offers, ride requests, real-time notifications, and more, ensuring a seamless and efficient carpooling experience.

## Vision and Scope

### Vision Statement

For students and staff at Háskóli Íslands who are seeking a more sustainable and convenient way to commute, the university carpooling service offers a platform to easily share rides to campus. This service not only provides a cost-effective and flexible commuting option but also fosters a sense of community and contributes to reducing traffic congestion and pollution in the greater Reykjavík area.

### Business Opportunity

The university carpooling service provides a smart and eco-friendly alternative to driving alone. By enabling students and staff to share rides, we help reduce transportation costs, alleviate parking shortages, and minimize environmental impact. Our platform makes it easy to find and offer rides, enhancing campus connectivity and promoting a greener community.

### Major Features

- **User Accounts**: Secure registration and login using HÍ email addresses, ensuring a university-exclusive platform.
- **Ride Offers**: Users can create ride offers specifying start location, departure time, available seats, and other relevant details.
- **Browse and Join Rides**: Users can browse available ride offers and send ride requests to join rides.
- **Notifications and Updates**: Users receive notifications for new ride requests and ride request acceptances or rejections via email and in-app notifications.
- **Profile Management**: Users can manage their profiles, including profile pictures, contact information, and preferences.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Hibernate**
- **Docker**
- **Maven**
- **STOMP and SockJS** (for real-time notifications)
- **Swagger** (for API documentation)

## Architecture

The CarPool Backend follows a **RESTful API** architecture built with Spring Boot, managing all server-side logic, database interactions, and real-time communications. It leverages WebSockets with the STOMP protocol to facilitate real-time notifications to users.

Please note that the frontend of the CarPool application is developed separately and is available in its own repository.

## Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- **Java 17 SDK**
- **Maven**
- **Docker**
- **PostgreSQL**

### Installation

#### Running with Docker

The repository includes a `docker-compose.yml` file for easy setup.

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/carpool-backend.git
   cd carpool-backend
   ```

2. **Run Docker Compose**

  ```bash
  docker-compose up -d
  ```
  This command will start the PostgreSQL database in Docker container. You can skip the `-d` part if you want to see logs of starting the container.

3. **Start Spring Boot Application**

Start spring boot application in any way you want. For example:
```bash
mvn clean install
mvn spring-boot:run
```

The repository includes `application-dev.yml` file and profile is set to `dev` by default to run it locally.

## API Documentation
Comprehensive API documentation is available via Swagger UI.

Once the backend server is running, navigate to:

```bash
http://localhost:8088/swagger-ui.html
```
This interface provides detailed information about all available endpoints, request/response models, and allows you to interact with the API directly. That is also used in to UI that is done with React

## Real-Time Notifications

The CarPool Backend uses WebSockets with STOMP to deliver real-time notifications. This ensures users receive instant updates about ride requests, acceptances, rejections, and ride offer changes.

- WebSocket Configuration: The backend handles WebSocket connections at the /ws endpoint using SockJS for fallback options.
- User Identification: Based on email stored in the HTTP session for targeted notifications.
- Notification Triggers: Actions like creating ride requests, accepting/rejecting requests, and modifying ride offers send relevant notifications via pop-up or email to the users.

## License
This project is licensed under the [MIT License](LICENSE).

© 2024 CarPool Backend. All rights reserved.






