package com.flipperdevices.bsb.auth.within.oauth.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class PKCEHelperTest {
    @Test
    fun checkCodeVerifierChallenge() {
        val codeVerifier = "test"
        val expectedChallenge = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"

        val actual = PKCEHelper.getCodeChallenge(codeVerifier)

        assertEquals(expectedChallenge, actual)
    }
}
