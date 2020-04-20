package pl.teamkiwi.kiwi_ktor_authentication.util

import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import pl.teamkiwi.kiwi_ktor_authentication.exception.KiwiUnauthorizedException
import pl.teamkiwi.kiwi_ktor_authentication.model.KiwiAuthPrincipal

fun ApplicationCall.authPrincipal(): KiwiAuthPrincipal =
    authentication.principal() ?: throw KiwiUnauthorizedException("AuthPrincipal was not found in request (probably our fault).")
