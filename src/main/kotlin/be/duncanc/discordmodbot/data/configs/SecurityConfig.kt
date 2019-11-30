package be.duncanc.discordmodbot.data.configs

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User


@Configuration
class SecurityConfig(
        private val accessTokenResponseClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>,
        private val userService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
        oAuth2ClientProperties: OAuth2ClientProperties
) : WebSecurityConfigurerAdapter() {
    private val loginProcessingUrl: String = oAuth2ClientProperties.registration["discord"]
            ?.redirectUri?.removePrefix("{baseUrl}").toString()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .oauth2Login {
                    it.tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient)
                    it.userInfoEndpoint().userService(userService)
                }
                .authorizeRequests {
                    it.antMatchers("/login/oauth2/code/*").permitAll()
                    it.anyRequest().authenticated()
                }

    }
}
