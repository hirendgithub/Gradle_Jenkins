import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.jfrog.artifactory") version "4.25.2"
	id("maven-publish")
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.axis.investment"
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
			username = System.getenv("ARTIFACT_CREDS_USR")
			password = System.getenv("ARTIFACT_CREDS_PSW")
		}
		url = uri("https://artifactory.axisb.com/artifactory/pfm_gradle_virtual/")
	}
	mavenCentral()
}
task<Exec>("installMPINVerficationJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/MPINVerfication.jar", "-DgroupId=com.axismbservice.service", "-DartifactId=MPINVerfication", "-Dversion=1", "-Dpackaging=jar")
}
task<Exec>("installmfkycJar"){
	workingDir(rootProject.getProjectDir().getAbsolutePath())
	commandLine("mvn", "install:install-file", "-Dfile=src/main/resources/lib/mfkyc-1.0.7.jar", "-DgroupId=com.axis", "-DartifactId=mfkyc", "-Dversion=1.0.7", "-Dpackaging=jar")
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
			setUsername(System.getenv("ARTIFACT_CREDS_USR"))
			setPassword(System.getenv("ARTIFACT_CREDS_PSW"))
			setMaven(true)
		}
		repository {
			setRepoKey("pfm_gradle_remote")
			setUsername(System.getenv("ARTIFACT_CREDS_USR"))
			setPassword(System.getenv("ARTIFACT_CREDS_PSW"))
			setMaven(true)
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
	implementation("com.fasterxml.woodstox:woodstox-core:6.4.0")
	implementation("org.springframework.boot:spring-boot-starter-log4j2:2.3.4.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.oracle.database.jdbc:ojdbc8:19.7.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jsoup:jsoup:1.15.3")
	implementation("javax.xml.soap:javax.xml.soap-api:1.4.0")
	implementation("org.springframework.boot:spring-boot-starter-freemarker")
	implementation("commons-codec:commons-codec:1.15")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.json:json:20190722")
	implementation("com.axis:mfkyc:1.0.7")
	implementation("javax.xml.bind:jaxb-api:2.3.0")
	implementation("commons-codec:commons-codec:1.15")
//	implementation("io.springfox:springfox-swagger2:2.7.0")
//	implementation("io.springfox:springfox-swagger-ui:2.7.0")
	implementation(project(":common"))
	implementation(project(":wrapper"))
	implementation(project(":auth"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

}

ext {
	set("snakeyaml.version","1.32")
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
