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
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("odogwudozilla.Main")
}

tasks.register<JavaExec>("randomPuzzle") {
    group = "application"
    description = "Selects a random unsolved Advent of Code puzzle"
    mainClass.set("odogwudozilla.PuzzleRandomizer")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runDay18") {
    group = "application"
    description = "Run Advent of Code 2024 Day 18 solution"
    mainClass.set("odogwudozilla.year2024.day18.RAMRunAOC2024Day18")
    classpath = sourceSets["main"].runtimeClasspath
}

