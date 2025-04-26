# 🛠 Kotlin Migration Project

## Overview
This project is focused on **migrating an existing Java-based server** to **Kotlin**.
The goal is to leverage Kotlin’s concise syntax, enhanced null-safety, and improved maintainability, while enhancing object-oriented design and overall code quality.

## Purpose
- **Modernization**: Improve code readability, reduce boilerplate, and adopt modern language features.
- **Refactoring**: Redesign the system with better object-oriented principles during migration.
- **Performance Improvement**: Review the legacy code and optimize performance where possible.
- **Stability and Reliability**: Strengthen testing coverage to ensure system reliability.
- **Maintainability and Scalability**: Make the system easier to maintain and extend in the future.

## Scope
- Migrate all server-side Java classes to Kotlin.
- Refactor to enhance object-oriented design during migration.
- Review the existing code for potential performance improvements and optimize where needed.
- Increase test coverage across unit, integration, and system tests.
- Maintain or improve compatibility with existing functionalities.

## Migration Principles
- **Behavior Preservation**: System behavior must remain correct after migration.
- **Object-Oriented Refactoring**: Improve the domain model and responsibilities of classes.
- **Kotlin Best Practices**: Use Kotlin idioms safely and appropriately.
- **Incremental Migration**: Migrate module-by-module with validation after each phase.
- **Testing First**: Strengthen tests before, during, and after migration to detect regressions.

## Stack
- **Language**: Kotlin 1.9.25 (JVM Target 21)
- **Framework**: Spring Boot 3.4.4
- **Database**: PostgreSQL, MySQL
- **Build Tool**: Gradle
- **ORM**: Hibernate ORM 6.6.11.Final
- **API Documentation**: SpringDoc OpenAPI
- **Query Building**: QueryDSL 5.0.0
- **Security**: Spring Security, JWT (io.jsonwebtoken)
- **Resilience**: Resilience4j
- **Reactive Support**: Spring WebFlux
- **Batch Processing**: Spring Batch
- **Cloud Integration**: Firebase Admin SDK


## Notes
- This project **focuses on both language migration and architecture improvement**.
- We are **not** simply translating code — we are **rethinking and improving** design decisions.
- Priority is given to system stability and long-term codebase maintainability.

