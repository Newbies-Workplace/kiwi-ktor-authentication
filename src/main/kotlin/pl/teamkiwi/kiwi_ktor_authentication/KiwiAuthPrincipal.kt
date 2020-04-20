package pl.teamkiwi.kiwi_ktor_authentication

import io.ktor.auth.Principal

data class KiwiAuthPrincipal(
    val userId: String
) : Principal