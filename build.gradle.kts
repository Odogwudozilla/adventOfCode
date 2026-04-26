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

    // Playwright for browser automation (puzzle fetching and answer submission)
    implementation("com.microsoft.playwright:playwright:1.44.0")

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

// Task to install Playwright browser binaries
// Usage: ./gradlew installPlaywright  (run once after adding Playwright dependency)
tasks.register<JavaExec>("installPlaywright") {
    group = "application"
    description = "Installs Playwright browser binaries required for automation"
    mainClass.set("com.microsoft.playwright.CLI")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("install")
}

// Task to run the full automation pipeline
// Usage: ./gradlew autoSolve
tasks.register<JavaExec>("autoSolve") {
    group = "application"
    description = "Runs the full automated AoC puzzle pipeline: fetch, generate, solve, submit, document, commit"
    mainClass.set("odogwudozilla.AutomationOrchestrator")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
}

// Task to run the enhancement features example
// Usage: ./gradlew runEnhancementExample
tasks.register<JavaExec>("runEnhancementExample") {
    group = "application"
    description = "Run the Enhancement Features Example"
    mainClass.set("odogwudozilla.examples.EnhancementFeaturesExample")
    classpath = sourceSets["main"].runtimeClasspath
}

