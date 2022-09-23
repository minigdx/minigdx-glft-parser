@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.minigdx.mpp)
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

dependencies {
    this.commonMainApi(libs.bundles.serialization.common)
    this.jsMainApi(libs.bundles.serialization.js)
    this.jvmMainApi(libs.bundles.serialization.jvm)
}
