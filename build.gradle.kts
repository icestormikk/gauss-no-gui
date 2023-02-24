plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "0.9.9.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set(project.name)
    manifest {
        attributes["Implementation-Title"] = "gauss-no-gui"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}