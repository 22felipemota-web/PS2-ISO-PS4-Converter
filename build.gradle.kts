plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "br.com.ps2ps4.converter"
    compileSdk = 35
    defaultConfig {
        applicationId = "br.com.ps2ps4.converter"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
}
dependencies {
    implementation("androidx.activity:activity-ktx:1.10.0")
}
