package com.brianml31.instamoon.utils

class UrlUtils {
    companion object {
        private const val BASE_URL = "ckSnfolu4rV3ptwz4oTh1zCO8KB00Opkyuy1g9sAJld/lLmOm7B6Gk8j0mlecax8ojuCBVIfzxwncOyW1OIRyI7fhmzvL7sl2OWsVRtTfXg="
        const val FONT_FACEBOOK = "Z7N8j1u+xpt2KZiDH2qFp4ARKXPAWipmbUU4vqcOnzV16iC5LgRPnUVGpEHAhw14uJ6uhj3KG7fUokYtMzPqqA=="
        const val FONT_IOS_18_4 = "YZ8B5XEghlJrEXgA+eTVXg6hHW57kwki8MgqLrbq4lmFdgKmzR6v3HHVxMdncqVdmkcXmdtLpX4VdYAeCDX5ww=="
        const val FONT_IOS_18 = "VF+DrFQS3PjjvS7fDhBX39NynajvxPSCaYegeD0z94LeJ1rYXRfbhyOPjtmnQOEh"
        const val FONT_WHATSAPP = "IGlYBsvYaPJCsoAm2dOBTctxF3YXRu627EYqkGb8pg9oM531Xxukhsdj2VqWR+aUlF8xn9FThi2JAJLZmAqu4Q=="
        const val CHECK_VERSION = "G0wCaR56gHjqAXWyh3t3wyI3ysWWI5FD3Qx4tzTtdUadT7A/SCxr6JQU/KF7Y+xK"

        fun buildUrl(encryptedEndpoint: String): String {
            val baseUrl = AESUtils.decryptTextWithPassword(BASE_URL, "InstaMoon") ?: return ""
            var endpoint = AESUtils.decryptTextWithPassword(encryptedEndpoint, "InstaMoon") ?: return ""
            if (endpoint.startsWith("/")) {
                endpoint = endpoint.substring(1)
            }
            return baseUrl + "/" + endpoint
        }
    }
}