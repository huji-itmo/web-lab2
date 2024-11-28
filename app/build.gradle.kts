plugins {
    id("java")
    id("war")
    id("io.freefair.lombok") version "8.10"

}

tasks.war {
    webAppDirectory.set(file("src/main/webapp"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    implementation("com.google.code.gson:gson:2.11.0");

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        
    }
}

tasks.create("deploy_local") {

    dependsOn("war")

    doLast {
        exec {
            workingDir(".")
            commandLine("bash", "scripts/deploy_local.sh")
        }
    }
}

tasks.create("deploy_helios") {

    dependsOn("war")

    doLast {
        exec {
            workingDir(".")
            commandLine("bash", "scripts/deploy_helios.sh")
        }
    }
}