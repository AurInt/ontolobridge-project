# Ontolobridge

## Purpose
Upgrade to working condition

## Source of app-build
Spring Boot + Spring Security + JWT + MySQL + React Full Stack Polling App: 

[Part 1](https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-1/)
[Part 2](https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/)

[GitHub](https://github.com/callicoder/spring-security-react-ant-design-polls-app)

### Required modules
- REST / Spring Web
  - / (__IndexController__)
  - /auth (__AuthController__)  https://datatracker.ietf.org/doc/html/rfc6749#section-1.5
    - /checkJWTToken 
    - /getAllDetails
    - /login
    - /register
    - /retrieveMissingDetails
    - /verify
  - /retrieve (__RetrieveController__)
    - /Requests
    - /ontologies
  - /requests (__RequestController__)
    - /RequestAnnotationProperty
    - /RequestDataProperty
    - /RequestObjectProperty
    - /RequestStatus
    - /RequestTerm
    - /RequestsSetStatus
    - /UpdateRequest
- Security 
  - OAuth2
- Emails
  - Notifications
- Database
  - Logins / Tokens
  - Requests

## Components
### Root
- OntolobridgeApplication/@Main
- OntologyManagerService/@Service

### BaseController
- AuthController
  - Likely will have to include both Jwttoken login and OAuth2 login
- RequestController
- RetrieveController
- UserController
  - enacts $OntoloUserDetailsServiceImpl as part of Security subframe to deal with user credentials, originating from $AuthenticationManager
    - Provides Jwt token if authenticated by $AuthenticationManager (within AuthController)

### Jwt token
- utilities/JwtProvider
  - generateJwtToken
- utilities/JWTRefreshInterceptor: 
  - JWTBuilder
  - JWT refresher

### Scheduler
- NotifierService/@Scheduled(cron)
- OntologyManagerService/@Scheduled(cron):
  - checkNoParentLabel: Check in SQL for missing parents (/5sec)
  - checkNewNotifications: Check notifications (/1sec)

### EmailService

### NotificationLibrary
- Uses GithubProperties to notify GitHub user of request

### JdbcTemplate

### AppProperties
- App properties are saved in resources/application.properties