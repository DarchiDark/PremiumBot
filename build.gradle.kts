plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta13"
}

group = "org.avangard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.telegram:telegrambots:6.8.0")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
tasks.shadowJar {
    relocate("org.telegram", "org.avangard.shaded.telegram")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.avangard.Main"
    }

}
