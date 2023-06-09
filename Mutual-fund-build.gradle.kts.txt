import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.jfrog.artifactory") version "4.25.2"
	id("maven-publish")
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}
task<Exec>("installMPINVerficationJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/MPINVerfication.jar", "-DgroupId=com.axismbservice.service", "-DartifactId=MPINVerfication", "-Dversion=1", "-Dpackaging=jar")
}
task<Exec>("installmfkycJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/mfkyc-1.0.7.jar", "-DgroupId=com.axis", "-DartifactId=mfkyc", "-Dversion=1.0.7", "-Dpackaging=jar")
}
task<Exec>("installFolderExistenceJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/FolderExistence.jar", "-DgroupId=com.axis", "-DartifactId=folderexistence", "-Dversion=1", "-Dpackaging=jar", "-DgeneratePom=true")
}
task<Exec>("installDocumentServiceJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/DocumentService.jar", "-DgroupId=com.axis", "-DartifactId=documentservice", "-Dversion=1", "-Dpackaging=jar", "-DgeneratePom=true")
}
task<Exec>("installDocListJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/DocList.jar", "-DgroupId=com.axis", "-DartifactId=doclist", "-Dversion=1", "-Dpackaging=jar", "-DgeneratePom=true")
}
task<Exec>("installConnectionJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/Connection.jar", "-DgroupId=com.axis", "-DartifactId=connection", "-Dversion=1", "-Dpackaging=jar", "-DgeneratePom=true")
}
task<Exec>("installOkraServiceJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/OkraService.jar", "-DgroupId=com.axis", "-DartifactId=okraservice", "-Dversion=1", "-Dpackaging=jar", "-DgeneratePom=true")
}
task<Exec>("installPanServiceJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/PanService.jar", "-DgroupId=com.axis", "-DartifactId=panservice", "-Dversion=1", "-Dpackaging=jar", "-DgeneratePom=true")
}

group = "com.axis.pfm"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	all {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	}
}

repositories {
	//maven(url="https://jitpack.io")
	mavenLocal()
	maven {
		credentials {
			//username = "tp00214377"
			//password = "Welcome@123"
			username = System.getenv("ARTIFACT_CREDS_USR")
			password = System.getenv("ARTIFACT_CREDS_PSW")
		}
		url = uri("https://artifactory.axisb.com/artifactory/pfm_gradle_virtual/")
	}
	mavenCentral()
}

artifactory{
	setContextUrl("https://artifactory.axisb.com/artifactory") //The base Artifactory URL if not overridden by the publisher/resolver
	publish {
		repository {
			setRepoKey("pfm_gradle_local")
			setUsername(System.getenv("ARTIFACT_CREDS_USR"))
			setPassword(System.getenv("ARTIFACT_CREDS_PSW"))
			setMavenCompatible(true)

		}
		defaults {
			publications("mavenJava")
			setPublishArtifacts(true)
			setPublishPom(true)
		}
	}
	resolve {
		repository {
			setRepoKey("pfm_gradle_virtual")
			setUsername("ARTIFACT_CREDS_USR")
			setPassword("ARTIFACT_CREDS_PSW")
			setMaven(true)

		}
		repository {
			setRepoKey("pfm_gradle_remote")
			setUsername("ARTIFACT_CREDS_USR")
			setPassword("ARTIFACT_CREDS_PSW")
			setMaven(true)

		}
	}
}
val kotestVersion = "4.3.2"
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
	implementation("commons-beanutils:commons-beanutils:1.9.4")
	implementation("com.fasterxml.woodstox:woodstox-core:6.4.0")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.boot:spring-boot-starter-log4j2:2.3.4.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation(project(":common"))
	implementation(project(":wrapper"))
	implementation(project(":product-service"))
	implementation(project(":auth"))
	implementation(project(":customer-service"))
	implementation(project(":transaction-service"))
	implementation(project(":registration-service"))
	//testImplementation("org.springframework.boot:spring-boot-starter-test")
	//testImplementation("io.projectreactor:reactor-test")
	implementation(project(":notification-service"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")
//	implementation("io.springfox:springfox-swagger2:2.7.0")
//	implementation("io.springfox:springfox-swagger-ui:2.7.0")
	implementation("org.json:json:20190722")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.google.code.gson:gson")

	testImplementation("io.mockk:mockk:1.10.0")
	testImplementation("io.projectreactor:reactor-test:3.3.4.RELEASE")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.6.0")
	testImplementation("com.squareup.okhttp3:okhttp:4.6.0")
	testImplementation("com.github.tomakehurst:wiremock-jre8:2.27.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

ext {
	set("snakeyaml.version","1.32")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

java {
	disableAutoTargetJvm()
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}
