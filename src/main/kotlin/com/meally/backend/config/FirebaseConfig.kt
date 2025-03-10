package com.meally.backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.FileInputStream



@Configuration
class FirebaseConfig {

    @Value("\${firebase.service.account.path}")
    lateinit var serviceAccountPath: String

    @PostConstruct
    fun init() {
        val serviceAccount = FileInputStream(serviceAccountPath)
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        FirebaseApp.initializeApp(options)
    }
}
