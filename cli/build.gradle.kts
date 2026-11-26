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
    implementation(project(":core"))
}

