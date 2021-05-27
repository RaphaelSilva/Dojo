import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    application
}

group = "me.engra"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

val linVersion: String by project
val exposedVersion: String by project
dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation( "io.javalin:javalin:$linVersion")
    implementation("org.xerial:sqlite-jdbc:3.34.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")
    testImplementation("com.konghq:unirest-java:3.11.09")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.9.3")

}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {

}