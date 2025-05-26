package com.meally.backend.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.meally.backend.users.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

class FirebaseAuthenticationFilter(
    private val publicEndpoints: RequestMatcher,
    private val userService: UserService,
) : OncePerRequestFilter() {

    private val authLogger: Logger = LoggerFactory.getLogger(FirebaseAuthenticationFilter::class.java)

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return publicEndpoints.matches(request)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (!authorizationHeader.isNullOrEmpty() && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)

            try {
                val decodedToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
                val uid = decodedToken.uid
                val authentication = UsernamePasswordAuthenticationToken(uid, null, emptyList())
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication

                userService.findOrCreateUser(
                    externalId = uid,
                    email = decodedToken.email,
                    name = decodedToken.name,
                    pictureUrl = decodedToken.picture,
                )

            } catch (e: FirebaseAuthException) {
                // Handle invalid token case (e.g., log error or return unauthorized response)
                authLogger.info("Error occurred: $e")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
