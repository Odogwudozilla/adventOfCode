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

// Task to run a specific puzzle (Day 18 of 2024)
// Usage: ./gradlew runDay18
// Add more tasks for specific days as needed using the same pattern
tasks.register<JavaExec>("runDay18") {
    group = "application"
    description = "Run Advent of Code 2024 Day 18 solution"
    mainClass.set("odogwudozilla.year2024.day18.RAMRunAOC2024Day18")
    classpath = sourceSets["main"].runtimeClasspath
}

