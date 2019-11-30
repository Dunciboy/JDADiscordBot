package be.duncanc.discordmodbot.data.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.RequestEntity
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

@Configuration
class OAuth2Config {
    private companion object {
        private const val DISCORD_BOT_USER_AGENT = "DiscordBot"

        fun withUserAgent(request: RequestEntity<*>): RequestEntity<*> {
            val headers = HttpHeaders()
            headers.putAll(request.headers)
            headers.add(HttpHeaders.USER_AGENT, DISCORD_BOT_USER_AGENT)
            return RequestEntity<Any?>(request.body, headers, request.method, request.url)
        }
    }


    @Bean
    fun accessTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        val client = DefaultAuthorizationCodeTokenResponseClient()
        client.setRequestEntityConverter(object : OAuth2AuthorizationCodeGrantRequestEntityConverter() {
            override fun convert(oauth2Request: OAuth2AuthorizationCodeGrantRequest): RequestEntity<*>? {
                return withUserAgent(super.convert(oauth2Request)!!)
            }
        })
        return client
    }

    @Bean
    fun userService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        val service = DefaultOAuth2UserService()
        service.setRequestEntityConverter(object : OAuth2UserRequestEntityConverter() {
            override fun convert(userRequest: OAuth2UserRequest): RequestEntity<*>? {
                return withUserAgent(super.convert(userRequest)!!)
            }
        })
        return service
    }
}
