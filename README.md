# kiwi-ktor-authentication

[![](https://jitpack.io/v/com.gitlab.team-kiwi/kiwi-ktor-authentication.svg)](https://jitpack.io/#com.gitlab.team-kiwi/kiwi-ktor-authentication)

JitPack private library tutorial:
https://jitpack.io/private

Do not forget to update your Gitlab Access Token (api permission) here: https://jitpack.io/w/user


dependencies:
```gradle
repositories {
    maven {
        url 'https://jitpack.io'
        credentials { username authToken }
    }
}

dependencies {
    implementation "com.gitlab.team-kiwi:kiwi-ktor-authentication:$kiwi_ktor_authentication"
    implementation "io.ktor:ktor-auth:$ktor_version"
}
```



usage:
```kotlin
install(Authentication) {
    kiwiServer {
        consulUrl = "http://localhost:8081"

        deserialize { string, type -> jacksonObjectMapper().readValue(string, type.javaObjectType) }
    }
}
```

additional features:
```kotlin
call.authPrincipal() // returns KiwiAuthPrincipal or throws
```

Do not forget to handle `KiwiUnauthorizedException` if needed.

