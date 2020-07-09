package pl.teamkiwi.kiwi_ktor_authentication

import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import net.paslavsky.ktor.consul.ConsulClientFeature
import net.paslavsky.ktor.consul.roundRobin
import pl.teamkiwi.kiwi_ktor_authentication.exception.KiwiUnauthorizedException
import pl.teamkiwi.kiwi_ktor_authentication.model.KiwiAuthPrincipal
import kotlin.reflect.KClass

class ServerAuthenticationProvider internal constructor(
        configuration: Configuration
) : AuthenticationProvider(configuration) {

    internal var consulUrl = configuration.consulUrl
    internal var deserializeBlock: (String, KClass<*>) -> Any = configuration.deserializeBlock

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {

        internal lateinit var deserializeBlock: (String, KClass<*>) -> Any
        lateinit var consulUrl: String

        /**
         * @param [block] used for message body deserialization.
         */
        fun deserialize(block: (String, KClass<*>) -> Any) {
            deserializeBlock = block
        }
    }
}

/**
 * Installs Server Authentication mechanism
 */
fun Authentication.Configuration.kiwiServer(
        name: String? = null,
        configure: ServerAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = ServerAuthenticationProvider(ServerAuthenticationProvider.Configuration(name).apply(configure))
    val kiwiConsulUrl = provider.consulUrl
    val deserializer = provider.deserializeBlock

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        val token = call.request.headers[AUTHORIZATION_KEY]
                ?: throw KiwiUnauthorizedException("Token not present at '$AUTHORIZATION_KEY' Header.")

        val principal = runCatching {
            val client = HttpClient(Apache) {
                install(ConsulClientFeature) {
                    consulUrl = kiwiConsulUrl
                    serviceName = "KiwiAuthService"
                    loadBalancer(roundRobin())
                }
            }

            val response = client.get<String> {
                header(AUTHORIZATION_KEY, token)
            }

            deserializer.invoke(response, KiwiAuthPrincipal::class) as KiwiAuthPrincipal
        }.getOrElse {
            throw KiwiUnauthorizedException("Error while validating token.", it)
        }

        context.principal(principal)
    }

    register(provider)
}

const val AUTHORIZATION_KEY = "Authorization"
