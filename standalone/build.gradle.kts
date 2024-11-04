plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.3.3"
}

apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":web-app"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
