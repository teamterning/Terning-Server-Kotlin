package com.terning.server.kotlin.application.auth.social.apple

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.terning.server.kotlin.domain.common.config.ValueConfig
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.text.Charsets.UTF_8

@Component
class ApplePublicKeyClient(
    private val valueConfig: ValueConfig,
) {
    fun getApplePublicKeys(): JsonArray {
        val conn = sendHttpRequest()
        val response = getHttpResponse(conn)
        val json = JsonParser.parseString(response.toString()).asJsonObject

        return json[KEY].asJsonArray
    }

    private fun sendHttpRequest(): HttpURLConnection =
        try {
            val url = URL(valueConfig.appleUri)
            (url.openConnection() as HttpURLConnection).apply {
                requestMethod = REQUEST_METHOD
            }
        } catch (e: Exception) {
            throw RuntimeException("Apple 공개키 요청에 실패했습니다", e)
        }

    private fun getHttpResponse(connection: HttpURLConnection): StringBuilder =
        try {
            BufferedReader(InputStreamReader(connection.inputStream, UTF_8)).use { reader ->
                buildString {
                    reader.lineSequence().forEach { append(it) }
                }.let { StringBuilder(it) }
            }
        } catch (e: Exception) {
            throw RuntimeException("Apple 서버 응답 읽기에 실패했습니다", e)
        }

    companion object {
        private const val KEY = "keys"
        private const val REQUEST_METHOD = "GET"
    }
}
