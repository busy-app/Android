package com.flipperdevices.bsb.auth.within.oauth.utils

import android.util.Base64
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.SecureRandom

private const val VERIFIER_BYTE_SIZE = 32
private const val DEFAULT_BASE64_CHARSET = "US-ASCII"
private const val HASH_ALGORITHM_NAME = "SHA-256"

object PKCEHelper {
    fun generateCodeVerifier(): String {
        val randomBytes = ByteArray(VERIFIER_BYTE_SIZE)
        SecureRandom().nextBytes(randomBytes)

        return toBase64(randomBytes)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getCodeChallenge(codeVerifier: String): String {
        val bytes: ByteArray = codeVerifier.toByteArray(Charset.forName(DEFAULT_BASE64_CHARSET))
        val md = MessageDigest.getInstance(HASH_ALGORITHM_NAME)
        md.update(bytes, 0, bytes.size)
        val digest = md.digest()
        return digest.toHexString(HexFormat.Default)
    }

    private fun toBase64(data: ByteArray): String {
        return Base64.encodeToString(
            data,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
}
