## Security-demo-project

Реализован функционал для регистрации пользователей с отправкой письма на указанный
email для его подтверждения и активации аккаунта. Используются jwt-refresh токены.
Ниже приведена схема реализации jwt аутентификации в приложении.

![spring-security-jwt-workflow-impl.png](diagrams%2Fspring-security-jwt-workflow-impl.png)

Так же используется CQRS архитектура - при которой код, изменяющий состояние(Command),
отделяется от кода, который просто читает состояние (Query).

### Highlights
- Java mail sender для отправки email
- Security построено на основе jwt токенов
- Приложение построено cqrs паттерна (Command and Query Responsibility Segregation)

### Technology
- Spring Boot 3.0.0 и Java 17
- Spring Data JPA + Hibernate + H2-database
- Spring Security 6 + jwt
- Spring Mail - для подтверждения email
- Spring Validation для валидации rest запросов
- JUnit 5 + AssertJ + Mockito для тестирования
- Mail-dev используется для отправки и получения сообщений

### Getting started
Требуется Java 17 или выше

./gradlew bootRun

Для тестирования работоспособности приложения в браузере http://localhost:8080/api/tags.  

### Mail-dev

Убедитесь что Docker engine запущени командой `docker --version`. Если Docker
не установлен, то [вот гайд по установке Docker](https://github.com/ModiconMe/docker/blob/main/installation.md).

Для запуска Mail-dev в докер контейнере в корне проекта запускаем команду
для построения докер образа.

`docker run -p 1080:1080 -p 1025:1025 maildev/maildev`

