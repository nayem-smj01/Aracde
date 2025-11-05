# 🎮 ArcadeDB Spring Boot API

A production-ready REST API built with Spring Boot and ArcadeDB, following SOLID principles and clean code practices.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![ArcadeDB](https://img.shields.io/badge/ArcadeDB-25.9.1-blue.svg)](https://arcadedb.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [API Endpoints](#-api-endpoints)
- [Usage Examples](#-usage-examples)
- [Project Structure](#-project-structure)
- [SOLID Principles](#-solid-principles)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

## ✨ Features

- 🚀 **RESTful API** - Clean REST endpoints for database management
- 🎯 **SOLID Principles** - Well-architected, maintainable codebase
- 📚 **Swagger/OpenAPI** - Interactive API documentation
- ✅ **Input Validation** - Jakarta Bean Validation
- 🔧 **Externalized Configuration** - Environment-specific settings
- 🛡️ **Global Exception Handling** - Consistent error responses
- 📊 **Logging** - Comprehensive logging with SLF4J
- 🏗️ **Layered Architecture** - Separation of concerns (API, Domain, Infrastructure)
- 🔌 **Connection Management** - Proper resource lifecycle handling
- 🧪 **Test-Ready** - Easy to unit test and mock

## 🏛️ Architecture

```
┌─────────────────────────────────────────────┐
│         Presentation Layer (API)            │
│         - DatabaseController                │
│         - DTOs, Exception Handlers          │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          Domain Layer (Business)            │
│          - DatabaseService Interface        │
│          - Domain Models                    │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│      Infrastructure Layer (Technical)       │
│      - ArcadeDbService Implementation       │
│      - RemoteServer Client                  │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│           ArcadeDB Server                   │
│           (External Database)               │
└─────────────────────────────────────────────┘
```

## 🔧 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Gradle 8.5+** (included via wrapper)
- **ArcadeDB Server** running on `localhost:2480` ([Download](https://arcadedb.com/))
- **curl** or **Postman** for API testing (optional)

### Installing ArcadeDB

**Option 1: Docker (Recommended)**
```bash
docker run -d --name arcadedb \
  -p 2480:2480 \
  -e ARCADEDB_ROOT_PASSWORD=arcadedb \
  arcadedata/arcadedb:latest
```

**Option 2: Manual Installation**
1. Download from [arcadedb.com](https://arcadedb.com/)
2. Extract and run:
   ```bash
   cd arcadedb-25.9.1
   bin/server.sh    # Linux/Mac
   bin\server.bat   # Windows
   ```

Verify ArcadeDB is running: http://localhost:2480

## 📥 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/arcadedb-spring-api.git
cd arcadedb-spring-api
```

### 2. Make Gradle Wrapper Executable (Linux/Mac)

```bash
chmod +x gradlew
```

### 3. Build the Project

```bash
./gradlew clean build
```

## ⚙️ Configuration

### Application Configuration

Create or edit `src/main/resources/application.yml`:

```yaml
# ArcadeDB Configuration
arcadedb:
  host: localhost
  port: 2480
  username: root
  password: arcadedb  # Change this!
  connection:
    timeout: 5000
    pool:
      max-size: 10

# Server Configuration
server:
  port: 8086

# Swagger/OpenAPI Configuration
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true

# Logging Configuration
logging:
  level:
    com.example: DEBUG
    com.arcadedb: INFO
    org.springframework: INFO
```

### Environment-Specific Configuration

For different environments, create:
- `application-dev.yml` (Development)
- `application-prod.yml` (Production)

Run with specific profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Environment Variables

You can also use environment variables:

```bash
export ARCADEDB_HOST=localhost
export ARCADEDB_PORT=2480
export ARCADEDB_USERNAME=root
export ARCADEDB_PASSWORD=your_password
```

## 🚀 Running the Application

### Development Mode

```bash
./gradlew bootRun
```

The application will start on http://localhost:8086

### Production Mode

Build executable JAR:
```bash
./gradlew bootJar
```

Run the JAR:
```bash
java -jar build/libs/dungeon-1.0-SNAPSHOT.jar
```

### With Custom Configuration

```bash
java -jar build/libs/dungeon-1.0-SNAPSHOT.jar \
  --arcadedb.host=production-db \
  --arcadedb.port=2480 \
  --server.port=8081
```

## 📚 API Documentation

### Swagger UI (Interactive)

Once the application is running, visit:

**Swagger UI:** http://localhost:8086/swagger-ui.html

Here you can:
- View all endpoints
- Test APIs directly in browser
- See request/response schemas
- Download OpenAPI specification

### OpenAPI JSON

**OpenAPI Spec:** http://localhost:8086/api-docs

Import this into Postman or other API tools.

## 🔌 API Endpoints

### Base URL
```
http://localhost:8086/api/v1
```

### Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/databases` | Get all databases |
| GET | `/databases/{name}/exists` | Check if database exists |
| POST | `/databases` | Create new database |

### Detailed Endpoints

#### 1. Get All Databases

```http
GET /api/v1/databases
```

**Response (200 OK):**
```json
{
  "databases": [
    {"name": "mydb"},
    {"name": "testdb"}
  ],
  "count": 2,
  "timestamp": "2025-11-05T10:30:00"
}
```

#### 2. Check Database Exists

```http
GET /api/v1/databases/{name}/exists
```

**Response (200 OK):**
```json
true
```

#### 3. Create Database

```http
POST /api/v1/databases
Content-Type: application/json

{
  "name": "mydb"
}
```

**Response (201 Created):**
```json
{
  "name": "mydb"
}
```

**Error Response (409 Conflict):**
```json
{
  "message": "Database 'mydb' already exists",
  "error": "DATABASE_ALREADY_EXISTS",
  "status": 409,
  "timestamp": "2025-11-05T10:35:00"
}
```

## 💡 Usage Examples

### Using cURL

**Get all databases:**
```bash
curl http://localhost:8086/api/v1/databases
```

**Check if database exists:**
```bash
curl http://localhost:8086/api/v1/databases/mydb/exists
```

**Create a database:**
```bash
curl -X POST http://localhost:8086/api/v1/databases \
  -H "Content-Type: application/json" \
  -d '{"name": "mydb"}'
```

### Using HTTPie

```bash
# Install: pip install httpie

http GET localhost:8080