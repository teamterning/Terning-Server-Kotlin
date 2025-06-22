package com.terning.server.kotlin.application.auth.social.apple

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64
import kotlin.text.Charsets.UTF_8

@Component
@Transactional
class AppleAuthTokenValidator(
    private val applePublicKeyClient: ApplePublicKeyClient,
) {
    fun extractAppleId(authAccessToken: String): String {
        val publicKeys = applePublicKeyClient.getApplePublicKeys()
        val publicKey = makePublicKey(authAccessToken, publicKeys)

        val claims: Claims =
            Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(getTokenFromBearerString(authAccessToken))
                .body

        val userInfo = JsonParser.parseString(Gson().toJson(claims)).asJsonObject

        return userInfo[ID].asString
    }

    private fun makePublicKey(
        token: String,
        keys: JsonArray,
    ): PublicKey {
        val parts = token.split(TOKEN_VALUE_DELIMITER.toRegex())
        val headerJson = String(Base64.getDecoder().decode(parts[0]), UTF_8)
        val header = JsonParser.parseString(headerJson).asJsonObject

        val kid = header[KID_HEADER_KEY]
        val alg = header[ALG_HEADER_KEY]
        val match =
            findMatchingKey(keys, kid, alg)
                ?: throw AuthException(AuthErrorCode.INVALID_KEY)

        return buildPublicKey(match)
    }

    private fun getTokenFromBearerString(token: String): String = token.replaceFirst(BEARER_HEADER, "")

    private fun findMatchingKey(
        keys: JsonArray,
        kid: JsonElement,
        alg: JsonElement,
    ): JsonObject? =
        keys.map { it.asJsonObject }
            .firstOrNull { it[KID_HEADER_KEY] == kid && it[ALG_HEADER_KEY] == alg }

    private fun buildPublicKey(key: JsonObject): PublicKey =
        try {
            val modulusBytes = Base64.getUrlDecoder().decode(key[MODULUS].asString)
            val exponentBytes = Base64.getUrlDecoder().decode(key[EXPONENT].asString)

            val spec = RSAPublicKeySpec(BigInteger(POSITIVE, modulusBytes), BigInteger(POSITIVE, exponentBytes))
            KeyFactory.getInstance(RSA).generatePublic(spec)
        } catch (e: Exception) {
            throw AuthException(AuthErrorCode.INVALID_KEY)
        }

    companion object {
        private const val TOKEN_VALUE_DELIMITER = "\\."
        private const val BEARER_HEADER = "Bearer "
        private const val MODULUS = "n"
        private const val EXPONENT = "e"
        private const val KID_HEADER_KEY = "kid"
        private const val ALG_HEADER_KEY = "alg"
        private const val RSA = "RSA"
        private const val ID = "sub"
        private const val POSITIVE = 1
    }
}
