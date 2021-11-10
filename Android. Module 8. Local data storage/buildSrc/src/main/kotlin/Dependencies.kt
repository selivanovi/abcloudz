object Dependencies {

    object Android {
        const val coreKtx = "androidx.core:core-ktx:1.6.0"
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.1"
    }

    object Test {
        const val jUnit = "junit:junit:4.+"
        const val androidJUnit = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
    }

    object NavigationUI {
        private const val navVersion = "2.3.5"
        const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navVersion"
        const val navigationUIKtx = "androidx.navigation:navigation-ui-ktx:$navVersion"
    }

    object Biometric {
        const val biometricKtx = "androidx.biometric:biometric-ktx:1.2.0-alpha03"
    }

    object Crypto {
        const val crypto = "androidx.security:security-crypto:1.1.0-alpha03"
    }

    object GSON {
        const val gson = "com.google.code.gson:gson:2.8.9"
    }
}