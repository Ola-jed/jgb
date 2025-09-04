group = "io.github.ola-jed.jgb"
version = "0.0.3"

plugins {
    id("java")
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    coordinates(group.toString(), name, version.toString())

    pom {
        name.set("jgb")
        description.set("A symbolic computation library with a focus on Gröbner bases")
        url.set("https://github.com/Ola-jed/jgb")
        inceptionYear.set("2025")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/license/mit")
            }
        }

        developers {
            developer {
                id.set("ola")
                name.set("Ola-jed")
                email.set("olabijed@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/Ola-jed/jgb.git")
            developerConnection.set("scm:git:ssh://github.com/Ola-jed/jgb.git")
            url.set("https://github.com/Ola-jed/jgb")
        }
    }
}