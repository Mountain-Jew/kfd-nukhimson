plugins {
    application
    alias(libs.plugins.kotlin.jvm)
}

application {
    mainClass.set("kfd.MainKt")
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
