plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Database connectors
    runtimeOnly("mysql:mysql-connector-java:8.0.28") // MySQL
    runtimeOnly("org.postgresql:postgresql:42.6.0") // PostgreSQL (if you're using it)

    // Hibernate Core for JPA
    implementation("org.hibernate:hibernate-core:5.6.12.Final")

    // Jakarta Persistence API
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")

    // Lombok (for getter/setter generation)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
