plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.12.0"
    id("jacoco")
    id("pl.droidsonroids.jacoco.testkit") version "1.0.7"
}

repositories {
    jcenter()
}

group = "garden.ephemeral.gradle.plugins"
version = "0.1.0"

dependencies {
    testImplementation(gradleTestKit())

    val guavaVersion = "30.1-jre"
    testImplementation("com.google.guava:guava:${guavaVersion}")

    val junitVersion = "5.7.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    val hamcrestVersion = "2.2"
    testImplementation("org.hamcrest:hamcrest:${hamcrestVersion}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    val whiteSource by plugins.creating {
        id = "garden.ephemeral.whitesource"
        implementationClass = "garden.ephemeral.gradle.plugins.whitesource.WhiteSourcePlugin"
    }
}

pluginBundle {
    website = "https://github.com/ephemeral-laboratories/whitesource-gradle-plugin"
    vcsUrl = "https://github.com/ephemeral-laboratories/whitesource-gradle-plugin"
    description = "Plugin to integrate the WhiteSource unified agent with Gradle"
    tags = setOf("whiteSource")

    val whiteSourcePlugin by plugins.creating {
        displayName = "Gradle WhiteSource plugin"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = BigDecimal.ONE
                }
            }
        }
    }
    check {
        dependsOn(jacocoTestCoverageVerification)
    }
}
