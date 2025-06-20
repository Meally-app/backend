package com.meally.backend.config

import com.google.firebase.auth.FirebaseAuth
import com.meally.backend.users.UserService
import org.springframework.cglib.core.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
class SecurityConfig(
    private val userService: UserService
) {

    private val publicRoutes: RequestMatcher = AntPathRequestMatcher("/public/**")
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(publicRoutes).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(FirebaseAuthenticationFilter(publicRoutes, userService), UsernamePasswordAuthenticationFilter::class.java)
            .build()
}