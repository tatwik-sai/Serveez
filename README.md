# Serveez - Local Services Marketplace Platform



## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security](#security)
- [Configuration](#configuration)
- [Team](#team)

## Overview

**Serveez** is a comprehensive local services marketplace platform designed to bridge the gap between service providers and customers. The platform facilitates seamless discovery, booking, and management of local services while providing robust communication tools and analytics capabilities.

### Key Highlights

- **Multi-role Architecture**: Supports three distinct user roles (Customer, Provider, Admin) with role-based access control
- **Real-time Communication**: Integrated messaging system for direct provider-customer interaction
- **Smart Booking System**: Complete booking lifecycle management with status tracking
- **Review & Rating System**: Transparent feedback mechanism with aggregate ratings
- **Analytics Dashboard**: Comprehensive analytics for business insights
- **File Management**: Secure photo upload and storage for service listings
- **Notification System**: Real-time notifications for important events

## Features

### For Customers (USER Role)

#### Service Discovery
- Browse comprehensive service listings with detailed descriptions
- Filter services by category for targeted search
- View service photos, pricing, and estimated duration
- Check provider profiles with ratings and reviews

#### Booking Management
- Create new bookings with scheduling preferences
- View all personal booking history
- Track booking status (Pending, Confirmed, Completed, Cancelled)
- Cancel bookings with optional reason
- Add notes and special requests to bookings

#### Communication & Feedback
- Direct messaging with service providers
- Leave reviews and ratings (1-5 stars) after service completion
- Receive real-time notifications for booking updates
- View conversation history

### For Service Providers (PROVIDER Role)

#### Profile Management
- Create and maintain comprehensive provider profiles
- Add location details (city, area, coordinates)
- Display contact information and bio
- Track average ratings and total reviews automatically

#### Service Listing Management
- Create detailed service listings with descriptions
- Upload multiple photos with captions
- Set pricing and estimated service duration
- Specify service location and category
- Manage listing visibility (active/inactive)
- Update or delete existing listings

#### Booking Operations
- View all incoming booking requests
- Confirm or reject booking requests
- Mark bookings as completed
- Track service history and customer interactions
- Manage booking schedules

#### Business Insights
- View total earnings
- Track booking statistics
- Monitor service performance
- Analyze customer feedback

###  For Administrators (ADMIN Role)

#### User Management
- View all users across the platform
- Manage user accounts and permissions
- Monitor user activity
- Handle user-related issues

#### Platform Management
- Create and manage service categories
- Monitor all bookings across the platform
- Cancel bookings on behalf of users (with reasons)
- Moderate service listings
- Update listing status

#### Analytics & Monitoring
- Platform-wide analytics dashboard
- Booking trends over time
- Top-performing categories
- Top-rated providers
- Revenue tracking
- User growth metrics

## Tech Stack

### Backend Framework
- **Spring Boot 3.3.13**: Modern Java framework for building production-ready applications
- **Java 21**: Latest LTS version with modern language features

### Database
- **MongoDB Atlas**: Cloud-hosted NoSQL database for flexible document storage
- **Spring Data MongoDB**: Simplified data access with repository pattern

### Security
- **Spring Security**: Comprehensive security framework
- **JWT (JSON Web Tokens)**: Stateless authentication mechanism
- **JJWT 0.12.5**: JWT creation and parsing library
- **BCrypt**: Password hashing algorithm

### Additional Libraries
- **Lombok**: Reduces boilerplate code with annotations
- **Jakarta Validation**: Bean validation for request DTOs
- **Spring Boot Starter Web**: RESTful web services
- **Spring Boot Starter Test**: Testing framework with JUnit

### Build & Development Tools
- **Maven**: Dependency management and build automation
- **Maven Wrapper**: Embedded Maven for consistent builds

## Architecture

### Project Structure

```
serveez/
├── src/
│   ├── main/
│   │   ├── java/com/example/serveez/
│   │   │   ├── controller/           # REST API Controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AnalyticsController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── MessageController.java
│   │   │   │   ├── NotificationController.java
│   │   │   │   ├── ProviderProfileController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   └── ServiceListingController.java
│   │   │   │
│   │   │   ├── service/              # Business Logic Layer
│   │   │   │   ├── AnalyticsService.java
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── FileStorageService.java
│   │   │   │   ├── MessageService.java
│   │   │   │   ├── NotificationService.java
│   │   │   │   ├── ProviderProfileService.java
│   │   │   │   ├── ReviewService.java
│   │   │   │   └── ServiceListingService.java
│   │   │   │
│   │   │   ├── repository/           # Data Access Layer
│   │   │   │   ├── BookingRepository.java
│   │   │   │   ├── MessageRepository.java
│   │   │   │   ├── NotificationRepository.java
│   │   │   │   ├── ProviderProfileRepository.java
│   │   │   │   ├── ReviewRepository.java
│   │   │   │   ├── ServiceCategoryRepository.java
│   │   │   │   ├── ServiceListingRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   │
│   │   │   ├── model/                # Domain Entities
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Message.java
│   │   │   │   ├── Notification.java
│   │   │   │   ├── ProviderProfile.java
│   │   │   │   ├── Review.java
│   │   │   │   ├── ServiceCategory.java
│   │   │   │   ├── ServiceListing.java
│   │   │   │   └── User.java
│   │   │   │
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   │   ├── request/          # Request DTOs
│   │   │   │   │   ├── BookingRequest.java
│   │   │   │   │   ├── CategoryRequest.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── MessageRequest.java
│   │   │   │   │   ├── NotificationRequest.java
│   │   │   │   │   ├── ProviderProfileRequest.java
│   │   │   │   │   ├── ReviewRequest.java
│   │   │   │   │   ├── ServiceListingRequest.java
│   │   │   │   │   └── SignupRequest.java
│   │   │   │   └── response/         # Response DTOs
│   │   │   │       ├── AnalyticsSummaryResponse.java
│   │   │   │       ├── ApiResponse.java
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── BookingResponse.java
│   │   │   │       ├── BookingsOverTimeResponse.java
│   │   │   │       ├── CategoryResponse.java
│   │   │   │       ├── MessageResponse.java
│   │   │   │       ├── NotificationResponse.java
│   │   │   │       ├── ProviderProfileResponse.java
│   │   │   │       ├── ReviewResponse.java
│   │   │   │       ├── ServiceListingResponse.java
│   │   │   │       ├── TopCategoryResponse.java
│   │   │   │       ├── TopProviderResponse.java
│   │   │   │       └── UserResponse.java
│   │   │   │
│   │   │   ├── security/             # Security Configuration
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── UserPrincipal.java
│   │   │   │
│   │   │   ├── exception/            # Exception Handling
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   │
│   │   │   └── ServeezApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties    # Application Configuration
│   │
│   └── test/
│       └── java/com/example/serveez/
│           └── ServeezApplicationTests.java
│
├── uploads/                          # File Storage Directory
│   └── listings/                     # Service Listing Photos
│
├── postman/
│   └── local-services-finder.postman_collection.json
│
├── pom.xml                          # Maven Configuration
├── mvnw                             # Maven Wrapper (Unix)
├── mvnw.cmd                         # Maven Wrapper (Windows)
└── README.md
```

## Getting Started

### Prerequisites

Ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
  ```powershell
  java -version
  ```

- **Maven 3.8+** (or use included Maven Wrapper)
  ```powershell
  mvn -version
  ```

- **MongoDB Atlas Account** (or local MongoDB instance)
  - Create a free cluster at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
  - Obtain connection string

- **Git** (for version control)
  ```powershell
  git --version
  ```

### Installation

1. **Clone the repository**
   ```powershell
   git clone https://github.com/tatwik-sai/Serveez.git
   cd Serveez
   ```

2. **Configure Database**
   
   Update `src/main/resources/application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/serveez
   ```

3. **Set Environment Variables (Optional)**
   ```powershell
   $env:JWT_SECRET="your-secure-jwt-secret-key-here"
   $env:ADMIN_EMAIL="admin@yourcompany.com"
   $env:ADMIN_PASSWORD="YourSecurePassword123!"
   $env:FILE_STORAGE_PATH="./uploads"
   ```

4. **Build the project**
   ```powershell
   .\mvnw.cmd clean install
   ```

### Running the Application

#### Option 1: Using Maven Wrapper (Recommended)
```powershell
.\mvnw.cmd spring-boot:run
```

#### Option 2: Using Maven
```powershell
mvn spring-boot:run
```

#### Option 3: Run as JAR
```powershell
.\mvnw.cmd clean package
java -jar target/serveez-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

### Verify Installation

Check application health:
```powershell
curl http://localhost:8080/api/auth/login
```

You should receive a response indicating the endpoint is accessible.

## API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/signup` | Register new user or provider | Public |
| POST | `/api/auth/login` | User/Provider login | Public |
| POST | `/api/auth/admin/login` | Admin login | Public |
| POST | `/api/auth/logout` | Logout current user | Authenticated |

**Signup Request Example:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "role": "USER"
}
```

**Login Request Example:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!"
}
```

### Service Listing Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/listings` | Get all active listings | Public |
| GET | `/api/listings/{id}` | Get listing by ID | Public |
| POST | `/api/providers/listings` | Create new listing | Provider |
| GET | `/api/providers/listings` | Get own listings | Provider |
| PUT | `/api/providers/listings/{id}` | Update listing | Provider |
| DELETE | `/api/providers/listings/{id}` | Delete listing | Provider |
| POST | `/api/providers/listings/{id}/photos` | Upload photo | Provider |
| DELETE | `/api/providers/listings/{id}/photos/{photoId}` | Delete photo | Provider |
| GET | `/api/admin/listings` | Get all listings | Admin |
| PATCH | `/api/admin/listings/{id}/status` | Update listing status | Admin |

**Create Listing Request:**
```json
{
  "categoryId": "cat123",
  "title": "Professional Home Cleaning",
  "description": "Expert cleaning services for your home",
  "price": 50.00,
  "location": "Downtown Area",
  "estimatedDuration": 120
}
```

### Booking Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/bookings` | Create new booking | User |
| GET | `/api/bookings/my` | Get user's bookings | User |
| GET | `/api/bookings/{id}` | Get booking details | User/Provider/Admin |
| POST | `/api/bookings/{id}/cancel` | Cancel booking | User |
| GET | `/api/provider/bookings` | Get provider bookings | Provider |
| POST | `/api/bookings/{id}/confirm` | Confirm booking | Provider |
| POST | `/api/bookings/{id}/complete` | Complete booking | Provider |
| GET | `/api/admin/bookings` | Get all bookings | Admin |
| POST | `/api/admin/bookings/{id}/cancel` | Admin cancel booking | Admin |

**Create Booking Request:**
```json
{
  "serviceListingId": "listing123",
  "scheduledAt": "2025-12-15T10:00:00",
  "notes": "Please bring cleaning supplies"
}
```

### Review Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/reviews` | Create review | User |
| GET | `/api/reviews/listing/{listingId}` | Get listing reviews | Public |
| GET | `/api/reviews/provider/{providerId}` | Get provider reviews | Public |
| PUT | `/api/reviews/{id}` | Update review | User |
| DELETE | `/api/reviews/{id}` | Delete review | User |
| GET | `/api/admin/reviews` | Get all reviews | Admin |
| DELETE | `/api/admin/reviews/{id}` | Admin delete review | Admin |

**Create Review Request:**
```json
{
  "bookingId": "booking123",
  "rating": 5,
  "comment": "Excellent service! Very professional and thorough."
}
```

### Message Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/messages` | Send message | User/Provider |
| GET | `/api/messages/conversation/{otherUserId}` | Get conversation | User/Provider |
| GET | `/api/messages/conversations` | Get all conversations | User/Provider |
| GET | `/api/messages/{id}` | Get message by ID | User/Provider |

### Provider Profile Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/providers/profile` | Create profile | Provider |
| GET | `/api/providers/profile` | Get own profile | Provider |
| PUT | `/api/providers/profile` | Update profile | Provider |
| GET | `/api/providers/{id}` | Get provider by ID | Public |
| GET | `/api/providers` | Get all providers | Public |

### Category Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/categories` | Get all categories | Public |
| GET | `/api/categories/{id}` | Get category by ID | Public |
| POST | `/api/admin/categories` | Create category | Admin |
| PUT | `/api/admin/categories/{id}` | Update category | Admin |
| DELETE | `/api/admin/categories/{id}` | Delete category | Admin |

### Analytics Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/analytics/summary` | Get analytics summary | Admin |
| GET | `/api/analytics/bookings-over-time` | Get booking trends | Admin |
| GET | `/api/analytics/top-categories` | Get top categories | Admin |
| GET | `/api/analytics/top-providers` | Get top providers | Admin |
| GET | `/api/provider/analytics` | Get provider analytics | Provider |

### Notification Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/notifications` | Get user notifications | Authenticated |
| PATCH | `/api/notifications/{id}/read` | Mark as read | Authenticated |
| DELETE | `/api/notifications/{id}` | Delete notification | Authenticated |

## Database Schema

### User Collection
```javascript
{
  _id: ObjectId,
  email: String (unique, indexed),
  passwordHash: String,
  role: Enum ["ADMIN", "USER", "PROVIDER"],
  isActive: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### ServiceListing Collection
```javascript
{
  _id: ObjectId,
  providerId: String (indexed),
  categoryId: String (indexed),
  title: String,
  description: String,
  price: Double,
  location: String,
  estimatedDuration: Integer, // minutes
  photos: [{
    id: String,
    url: String,
    caption: String,
    uploadedAt: DateTime
  }],
  isActive: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### Booking Collection
```javascript
{
  _id: ObjectId,
  userId: String (indexed),
  providerId: String (indexed),
  serviceListingId: String (indexed),
  status: Enum ["PENDING", "CONFIRMED", "COMPLETED", "CANCELLED"] (indexed),
  scheduledAt: DateTime,
  notes: String,
  cancellationReason: String,
  priceAtBooking: Double,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### Review Collection
```javascript
{
  _id: ObjectId,
  bookingId: String (unique, indexed),
  userId: String (indexed),
  providerId: String (indexed),
  serviceListingId: String (indexed),
  rating: Integer, // 1-5
  comment: String,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### ProviderProfile Collection
```javascript
{
  _id: ObjectId,
  userId: String (unique, indexed),
  displayName: String,
  bio: String,
  location: {
    city: String,
    area: String,
    latitude: Double,
    longitude: Double
  },
  phone: String,
  additionalContacts: String,
  averageRating: Double,
  totalReviews: Integer,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### Message Collection
```javascript
{
  _id: ObjectId,
  senderId: String (indexed),
  receiverId: String (indexed),
  content: String,
  isRead: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### Notification Collection
```javascript
{
  _id: ObjectId,
  userId: String (indexed),
  type: String,
  message: String,
  relatedEntityId: String,
  isRead: Boolean,
  createdAt: DateTime
}
```

### ServiceCategory Collection
```javascript
{
  _id: ObjectId,
  name: String (unique, indexed),
  description: String,
  icon: String,
  isActive: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

## Security

### Authentication Flow

1. **User Registration**: Passwords are hashed using BCrypt before storage
2. **Login**: Credentials validated against database
3. **JWT Generation**: Token issued with user ID, email, and role
4. **Token Usage**: Client includes JWT in Authorization header
5. **Request Validation**: JwtAuthenticationFilter validates token on each request

### Authorization

Role-based access control using Spring Security:

- **@PreAuthorize("hasRole('USER')")**: User-only endpoints
- **@PreAuthorize("hasRole('PROVIDER')")**: Provider-only endpoints
- **@PreAuthorize("hasRole('ADMIN')")**: Admin-only endpoints
- **@PreAuthorize("hasAnyRole('USER', 'PROVIDER')")**: Multiple roles

### Security Headers

- JWT token in `Authorization: Bearer <token>` header
- Session cookie: `lsf_session` (24-hour expiration)
- CORS configured for cross-origin requests

### Password Requirements

- Minimum length enforced through validation
- BCrypt hashing with automatic salt generation
- No plain text password storage

## Configuration

### Application Properties

```properties
# Server Configuration
server.port=8080

# MongoDB Configuration
spring.data.mongodb.uri=mongodb+srv://user:pass@cluster.mongodb.net/serveez
spring.data.mongodb.auto-index-creation=true

# JWT Configuration
jwt.secret=${JWT_SECRET:default-secret-key}
jwt.expiration=86400000  # 24 hours in milliseconds

# Session Cookie
session.cookie.name=lsf_session
session.cookie.max-age=86400  # 24 hours in seconds

# Admin Credentials
admin.email=${ADMIN_EMAIL:admin@localservices.com}
admin.password=${ADMIN_PASSWORD:AdminPassword123!}

# File Upload
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.storage.path=${FILE_STORAGE_PATH:./uploads}

# Logging Levels
logging.level.com.example.serveez=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=INFO
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `JWT_SECRET` | Secret key for JWT signing | Auto-generated default |
| `ADMIN_EMAIL` | Admin account email | admin@localservices.com |
| `ADMIN_PASSWORD` | Admin account password | AdminPassword123! |
| `FILE_STORAGE_PATH` | Photo storage directory | ./uploads |

## Team

| Name | Contribution |
|------|-------------|
| **Tatwik Sai** | Backend Architecture, Authentication & Security, Database Design, JWT Implementation, Global Exception Handling |
| **Shiva Reddy** | Booking Management System, Admin Operations, File Upload & Photo Management, Booking Lifecycle |
| **Bhavesh** | Real-time Messaging System, Notification Service, WebSocket Integration, Communication Features |
| **Farhan** | Analytics Dashboard, Auth Endpoints, Statistical Reports, Data Aggregation |
| **Dhanush** | Service Category Management, Review & Rating System, Feedback Mechanism |
| **Saketh** | Provider Profile Management, Service Listing CRUD, Provider Operations, Search Features |

