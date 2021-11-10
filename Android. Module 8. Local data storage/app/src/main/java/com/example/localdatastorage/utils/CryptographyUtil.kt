package com.example.localdatastorage.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.Charset
import java.security.AccessControlContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val SECRET_KEY = "3EM4ILP4&&W0RDk3y"
private const val KEY_SIZE = 256
private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
private const val ENCRYPTION_PADDING_MODE = KeyProperties.ENCRYPTION_PADDING_PKCS7
private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

object CryptographyUtil {

    private fun getSecretKey(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        ).apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING_MODE)
            setUserAuthenticationRequired(false)
        }
        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            ENCRYPTION_ALGORITHM,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    //When we create encryption cipher we should save parameters(for example file) and use this
    //parameters for create decryption cipher
    fun initCipher(context: Context, mode: Int) {
        if(Cipher.ENCRYPT_MODE == mode) {
            //todo
        }
        if(Cipher.DECRYPT_MODE == mode) {
            //todo
        }
    }

    private fun getCipher(): Cipher =
        Cipher.getInstance("$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING_MODE")

    fun encryptData(plaintext: String, cipher: Cipher): ByteArray {
        return cipher.doFinal(plaintext.toByteArray(Charset.forName("UTF-8")))
    }

    fun decryptData(cipherText: ByteArray, cipher: Cipher): String {
        val plainText = cipher.doFinal(cipherText)
        return String(plainText, Charset.forName("UTF-8"))
    }
}