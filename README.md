# 🏬 Restaurant-ms-manager

## Overview

This is the Restaurant Management Service in the Redck Restaurant Microservices platform — a backend microservice responsible for handling all data and logic related to restaurant metadata.

It provides RESTful endpoints for creating, updating, and retrieving restaurant information such as name, location, chairs and tables position, operating hours, and other key metadata.

## 🧩 Responsibilities

 - Manage restaurant data

 - Provide API endpoints for other services and the BFF

 - Communicate with MongoDB for persistent, flexible data storage

## 📦 Tech Stack

 - Java (Spring Boot)

 - MongoDB (primary data storage)

 - Gradle (build tool)

 - Docker

## 🚀 How to Build and Run

### 🔨 Step 1: Build the Application JAR

Make sure you have Gradle installed or use the Gradle wrapper.

From the root directory of the BFF project, run:

    ./gradlew build

This will generate the *.jar file in the build/libs/ directory, which the Dockerfile will use.

> **_NOTE:_**  Make sure the Dockerfile references the correct path to the built .jar (typically something like build/libs/*.jar).

### 🐳 Step 2: Build the Docker Image

Once the build is complete, build the Docker image using:

    docker build -t redck-restaurant-ms-manager.jar .

### 🧱 Step 3: Start the Containers

Navigate to the directory containing the docker-compose.yaml file and run:

    docker compose up -d

This will start the restaurant-ms-manager container along with any defined dependencies (e.g., MongoDB).
