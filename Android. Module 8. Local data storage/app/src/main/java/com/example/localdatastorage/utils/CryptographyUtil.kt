package com.example.localdatastorage.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val SECRET_KEY = "3EM4ILP4&&W0RDk3y"
private const val KEY_SIZE = 128
private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
private const val ENCRYPTION_PADDING_MODE = KeyProperties.ENCRYPTION_PADDING_NONE
private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

object CryptographyUtil {

    private fun getSecretKey(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING_MODE)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }
        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            ENCRYPTION_ALGORITHM,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    fun getInitializedCipher(cipherMode: Int): Cipher {
        val cipher = getCipher()
        val secretKey = getSecretKey(SECRET_KEY)
        cipher.init(cipherMode, secretKey)

        return cipher
    }

    private fun getCipher(): Cipher =
        Cipher.getInstance(
            "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING_MODE"
        )

    fun encryptData(plaintext: String, cipher: Cipher): ByteArray {
        val byteArray = cipher.doFinal(plaintext.toByteArray(Charset.forName("UTF-8")))
        return byteArray
    }

    fun decryptData(cipherText: ByteArray, cipher: Cipher): String {
        val plainText = cipher.doFinal(cipherText)
        return String(plainText, Charset.defaultCharset())
    }
}