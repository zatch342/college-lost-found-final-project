# Campus Lost & Found

A small university final-project MVP built for speed: Spring Boot, Spring Security, JPA, PostgreSQL, Thymeleaf, CRUD, search, and image upload

## Phase 1 - Project Idea

Top 3 easiest ideas:

1. **Campus Lost & Found Board** - Best choice. Natural CRUD, image upload, search, auth, and a useful demo story.
2. **Mini Marketplace** - Similar features, but needs price/category logic and more validation.
3. **Student Event Board** - Easy CRUD/search, but image upload feels less essential.

Selected idea: **Campus Lost & Found Board**.

## Phase 2 - Architecture

```text
Browser
  -> Thymeleaf pages
  -> Spring MVC controllers
  -> Service layer
  -> Spring Data JPA repositories
  -> PostgreSQL database
```

Main folders:

```text
src/main/java/com/example/lostfound
  config       Security and uploaded-file web config
  controller   Auth and item routes
  model        JPA entities
  repository   JPA repositories
  service      Business logic and image storage

src/main/resources
  templates    Thymeleaf pages
  static/css   App styling
```

Database tables:

- `app_users`: stores registered users with BCrypt passwords.
- `items`: stores lost/found posts, status, image path, and owner.

## Phase 3 - Code and Features

Implemented:

- Sign up
- Login
- Logout
- PostgreSQL-ready JPA entities
- CRUD for lost/found items
- Image upload with saved image path
- Display uploaded image
- Keyword search across title, description, and location
- Clean responsive UI

Local run:

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

The default local database is H2 so the app can run quickly without setup. For PostgreSQL, set:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/lostfound
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=your_password
export SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
mvn spring-boot:run
```

## Phase 4 - Deployment Instructions

### GitHub

```bash
git init
git add .
git commit -m "Build campus lost and found final project"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/campus-lost-found.git
git push -u origin main
```

### Render

1. Create a PostgreSQL database on Render.
2. Create a new Blueprint or Web Service from the GitHub repository.
3. Use these settings:
   - Runtime: Docker
   - Dockerfile path: `./Dockerfile`
4. Add environment variables:
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PORT/DATABASE`
   - `SPRING_DATASOURCE_USERNAME=YOUR_DB_USER`
   - `SPRING_DATASOURCE_PASSWORD=YOUR_DB_PASSWORD`
   - `SPRING_DATASOURCE_DRIVER=org.postgresql.Driver`
   - `UPLOAD_DIR=uploads`
5. Deploy and open the Render URL.

Important: Render's PostgreSQL internal URL may start with `postgres://`. Spring Boot expects `jdbc:postgresql://`, so use the JDBC format shown above.

## Phase 5 - Final Report Template

### Project Title

Campus Lost & Found Web Application

### Objective

The objective of this project is to provide a simple web platform where university students can post lost or found items, upload item photos, search posts, and contact the post owner.

### Technology Stack

- Backend: Spring Boot
- Security: Spring Security
- Database: PostgreSQL
- ORM: Spring Data JPA
- Frontend: Thymeleaf, HTML, CSS
- Deployment: GitHub and Render

### Main Features

- User registration and login
- Secure logout
- Create, view, update, and delete lost/found posts
- Upload and display item images
- Search items by keyword
- Responsive web design

### Database Design

The system uses two main tables. The `app_users` table stores account information, and the `items` table stores item posts. Each item belongs to one user.

### Conclusion

The project successfully implements a full-stack web application using Spring Boot, Spring Security, JPA, PostgreSQL, and Thymeleaf. It satisfies the required authentication, CRUD, image upload, search, database, design, and deployment requirements.

###Copyright
