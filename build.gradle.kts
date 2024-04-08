plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.redck"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	//implementation("org.springframework.boot:spring-boot-starter")
	//implementation("io.springfox:springfox-boot-starter:3.0.0")
	//implementation("io.springfox:springfox-swagger-ui:3.0.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.data:spring-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	//implementation("org.springframework.boot:spring-boot-docker-compose")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
}

tasks.withType<Test> {
	useJUnitPlatform()
}