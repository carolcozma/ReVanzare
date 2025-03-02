# Revânzare - Ads Platform

## Description
Revânzare is a web application that allows users to explore, post, edit, and delete sales ads. Its goal is to provide a simple and efficient solution for managing ads, facilitating product exchange between users.

## Technologies Used
- **Backend:** Spring Boot
- **Database:** SQL Server
- **Frontend:** Thymeleaf

## Features
### Core Features
- Adding new ads
- Editing existing ads
- Deleting ads
- User management (authentication, registration, sessions)

### Additional Features
- Personal account for each user
- Viewing other users' ads
- Uploading and displaying images associated with ads
- Search bar for filtering ads

## Application Structure
### Main Objects
#### User
- **id**: Unique identifier
- **username**: Unique username
- **password**: Encrypted password
- **email**: Unique email address
- **firstName / lastName**: User's first and last name

#### Product
- **id**: Unique identifier
- **date**: Date of publication
- **description**: Product description
- **name**: Product name
- **price**: Price
- **userID**: Reference to the user
- **imagePath**: Path to the product image

### Important Classes and Methods
- **AuthenticationController**: Handles authentication and registration
- **ProductController**: Manages ad operations
- **UserService**: User business logic
- **ProductService**: Product business logic

## Testing
- **API testing via Postman** (authentication, ad management)
- **Database verification** (data integrity)
- **Manual UI testing** (validations, interactions)
- **End-to-end testing** (full user flow)

## Possible Improvements
- Implementing a **shopping cart**
- User notifications
- Optimizing image management (cloud storage)
- **Review and rating system**
- **2FA authentication** for enhanced security
