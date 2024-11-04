plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.6"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "io.spring.dependency-management")

    val versionFromProperty = "${project.property("version")}"
    val versionFromEnv: String? = System.getenv("VERSION")

    version = versionFromEnv ?: versionFromProperty
    group = "${project.property("group")}"

    val targetJavaVersion = (project.property("jdk_version") as String).toInt()
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)

    java {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        if (JavaVersion.current() < javaVersion) {
            toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
        }
        withJavadocJar()
        withSourcesJar()
    }

    repositories {
        mavenCentral()
        maven(url = "https://nexus.sibmaks.ru/repository/maven-snapshots/")
        maven(url = "https://nexus.sibmaks.ru/repository/maven-releases/")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${project.property("lib_springboot_version")}")
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        // ensure that the encoding is set to UTF-8, no matter what the system default is
        // this fixes some edge cases with special characters not displaying correctly
        // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
        // If Javadoc is generated, this must be specified in that task too.
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
            options.release = targetJavaVersion
        }
    }

}
