plugins {
    id("java")
    id("application")
}

group = "odogwudozilla"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JetBrains annotations for null safety and IDE support
    implementation("org.jetbrains:annotations:24.0.1")

    // Jackson ObjectMapper for JSON serialization/deserialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // JUnit testing framework for unit tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

// Configure the application plugin to set up the main class and application name
// The applicationName is used in the puzzle command wrapper script
application {
    mainClass.set("odogwudozilla.Main")
    applicationName = "puzzle"
}

// Task to select a random unsolved puzzle
// Usage: ./gradlew randomPuzzle
tasks.register<JavaExec>("randomPuzzle") {
    group = "application"
    description = "Selects a random unsolved Advent of Code puzzle"
    mainClass.set("odogwudozilla.PuzzleRandomizer")
    classpath = sourceSets["main"].runtimeClasspath
}

// Task to run the enhancement features example
// Usage: ./gradlew runEnhancementExample
tasks.register<JavaExec>("runEnhancementExample") {
    group = "application"
    description = "Run the Enhancement Features Example"
    mainClass.set("odogwudozilla.examples.EnhancementFeaturesExample")
    classpath = sourceSets["main"].runtimeClasspath
}

