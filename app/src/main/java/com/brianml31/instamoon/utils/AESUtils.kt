package com.brianml31.instamoon.utils

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESUtils {

    companion object {

        private const val ALGORITHM: String = "AES/CBC/PKCS5Padding"

        private fun getSecretKey(password: String): SecretKey {
            val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
            val keyBytes: ByteArray = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
            val keyShort: ByteArray = keyBytes.copyOf(16)
            return SecretKeySpec(keyShort, "AES")
        }

        fun encryptTextWithPassword(text: String, password: String): String {
            val key: SecretKey = getSecretKey(password)
            val cipher: Cipher = Cipher.getInstance(ALGORITHM)
            val iv: ByteArray = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val ivSpec: IvParameterSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
            val encrypted: ByteArray = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
            val combined: ByteArray = ByteArray(iv.size + encrypted.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)
            return Base64.encodeToString(combined, Base64.NO_WRAP)
        }

        fun decryptTextWithPassword(encryptedData: String, password: String): String? {
            try {
                val combined: ByteArray = Base64.decode(encryptedData, Base64.NO_WRAP)
                val iv: ByteArray = combined.copyOfRange(0, 16)
                val encrypted: ByteArray = combined.copyOfRange(16, combined.size)
                val ivSpec: IvParameterSpec = IvParameterSpec(iv)
                val key: SecretKey = getSecretKey(password)
                val cipher: Cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
                val decrypted: ByteArray = cipher.doFinal(encrypted)
                return String(decrypted, StandardCharsets.UTF_8)
            } catch (e: Exception) {
                return null
            }
        }
    }
}
