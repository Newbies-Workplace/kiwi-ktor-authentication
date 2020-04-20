package pl.teamkiwi.kiwi_ktor_authentication.model

import io.ktor.auth.Principal

data class KiwiAuthPrincipal(
    val userId: String
) : Principal