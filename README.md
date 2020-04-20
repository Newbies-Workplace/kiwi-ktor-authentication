# kiwi-ktor-authentication

JitPack tutorial:
https://jitpack.io/private

dependencies:
```
repositories {
    maven {
        url 'https://jitpack.io'
        credentials { username authToken }
    }
}
```


`implementation "com.gitlab.team-kiwi:kiwi-ktor-authentication:$kiwi_ktor_authentication"`

`implementation "io.ktor:ktor-auth:$ktor_version"`



usage:
```kotlin
install(Authentication) {
    kiwiServer {
        url = getProp("kiwi.auth.url")

        deserialize { string, type -> jacksonObjectMapper().readValue(string, type.javaObjectType) }
    }
}
```