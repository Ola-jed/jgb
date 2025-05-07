plugins {
    id("java")
    id("me.champeau.jmh").version("0.7.3")
}

group = "com.ola"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
    jmh("org.openjdk.jmh:jmh-core:0.9")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:0.9")
    jmh("org.openjdk.jmh:jmh-generator-bytecode:0.9")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

jmh {
    resultFormat = "TEXT"
    threads = 8
    verbosity = "NORMAL"
}