import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.jooq.codegen.*

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.jooq"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath ("org.jooq:jooq-codegen:3.18.0")
        classpath ("com.h2database:h2:1.3.148")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2:1.3.148")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.jooq:jooq")
    compileOnly("org.jooq:jooq-codegen:3.18.0")
}

tasks.register("generate") {
    GenerationTool.generate(Configuration()
        .withJdbc(
            Jdbc()
            .withDriver("org.h2.Driver")
            .withUrl("jdbc:h2:mem:drone_db"))
        .withGenerator(
            org.jooq.meta.jaxb.Generator()
            .withDatabase(Database().withInputSchema("public"))
            .withGenerate(Generate())
            .withTarget(Target()
                .withPackageName("com.jooq.demo")
                .withDirectory("${projectDir}/src/generated/jooq"))))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
