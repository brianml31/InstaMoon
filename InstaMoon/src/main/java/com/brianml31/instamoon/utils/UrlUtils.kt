package com.brianml31.instamoon.utils

class UrlUtils {
    companion object {
        private const val BASE_URL: String = "ckSnfolu4rV3ptwz4oTh1zCO8KB00Opkyuy1g9sAJld/lLmOm7B6Gk8j0mlecax8ojuCBVIfzxwncOyW1OIRyI7fhmzvL7sl2OWsVRtTfXg="
        const val FACEBOOK_EMOJI: String = "pIG/XCyrJl5jR9YCO4pbqBPQiQ/4AbVKRV+cdAPcDnl+23poXdjpH8iUU+8V/mom"
        const val IOS_EMOJI: String = "muQySzXUHz35hu31S2QwPSH/EI3gu8cCdu7vvc0oHBJ4bcTNuKwPLCReE5H9a0CI"
        const val JOY_PIXEL_EMOJI: String = "Q3LehkrQaPfPuSFJF0tWGgfgQlJSuzWFEjFRt3/lhD8qb51ztSxsS39s+vJqqRCG"
        const val WHATSAPP_EMOJI: String = "s24abgok1Phoz37EaECZ5GQ8HNPbWLu6HgwbstamRLzK32aNnZcxpAlxInTLb0Hi"
        const val CHECK_VERSION: String = "BiQCmscDFneFqw9X8F8AwWGcyTrXEJTeSuFjzH8oauNRvcmdKsWaOmeHiAMFR/O4OBa+Ynqsg1L7qdq9PUUqkw=="
        const val GITHUB: String = "pQXQnuCJR+dUF9JCwhHBVkeegOCCkoQdOR2QIJ3SlyaCSFKUv4l+j1Pw0IiQ+lRqEZdx+GcqibiGhmY/gDrFlw=="

        fun buildUrl(encryptedEndpoint: String): String {
            val baseUrl: String = AESUtils.decryptTextWithPassword(BASE_URL, "InstaMoon") ?: return ""
            var endpoint: String = AESUtils.decryptTextWithPassword(encryptedEndpoint, "InstaMoon") ?: return ""
            if (endpoint.startsWith("/")) {
                endpoint = endpoint.substring(1)
            }
            return baseUrl + "/" + endpoint
        }
    }
}