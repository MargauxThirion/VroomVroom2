plugins {
    id("java")
    id ("io.quarkus") version "1.13.7.Final"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    implementation(platform("io.quarkus:quarkus-bom:1.13.7.Final"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")
    implementation ("io.quarkus:quarkus-arc")
    implementation ("javax.inject:javax.inject:1")
    implementation ("io.quarkus:quarkus-resteasy-reactive")
    testImplementation ("io.quarkus:quarkus-junit5")
    testImplementation ("io.rest-assured:rest-assured")
    //implementation ("io.quarkus:quarkus-resteasy-jsonb")
    implementation ("io.quarkus:quarkus-websockets")
}

tasks.test {
    useJUnitPlatform()
}