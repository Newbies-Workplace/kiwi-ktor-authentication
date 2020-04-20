package pl.teamkiwi.kiwi_ktor_authentication.exception

class KiwiUnauthorizedException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}