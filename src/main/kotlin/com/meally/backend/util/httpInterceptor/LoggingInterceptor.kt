package com.meally.backend.util.httpInterceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class LoggingInterceptor : ClientHttpRequestInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        logRequest(request, body)
        val response = execution.execute(request, body)
        logResponse(response)
        return response
    }

    private fun logRequest(request: HttpRequest, body: ByteArray) {
        logger.info("Request: {} {}", request.method, request.uri)
        logger.info("Request headers: {}", request.headers)
        if (body.isNotEmpty()) {
            logger.info("Request body: {}", String(body, java.nio.charset.StandardCharsets.UTF_8))
        }
    }

    private fun logResponse(response: ClientHttpResponse) {
        logger.info("Response status: {}", response.statusCode)
        logger.info("Response headers: {}", response.headers)
        response.body.use { body ->
            if (body != null) {
                val responseBody = body.readAllBytes()
                if (responseBody.isNotEmpty()) {
                    logger.info("Response body: {}", String(responseBody, java.nio.charset.StandardCharsets.UTF_8))
                }
            }
        }
    }
}