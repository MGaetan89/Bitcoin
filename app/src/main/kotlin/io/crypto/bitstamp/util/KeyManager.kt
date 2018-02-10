package io.crypto.bitstamp.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.math.BigInteger
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.Calendar
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

// From: https://github.com/TeamTechnologies/security-workshop-sample/tree/master/app/src/stages/stage1/level1/java/co/temy/securitysample/encryption
class KeyStoreWrapper {
	private val keyStore: KeyStore = createAndroidKeyStore()

	fun createAndroidKeyStoreAsymmetricKey(alias: String, context: Context): KeyPair {
		val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			this.initGeneratorWithKeyGenParameterSpec(generator, alias)
		} else {
			this.initGeneratorWithKeyPairGeneratorSpec(generator, alias, context)
		}

		return generator.generateKeyPair()
	}

	fun getAndroidKeyStoreAsymmetricKeyPair(alias: String): KeyPair? {
		val privateKey = this.keyStore.getKey(alias, null) as PrivateKey?
		val publicKey = this.keyStore.getCertificate(alias)?.publicKey

		return if (privateKey != null && publicKey != null) KeyPair(publicKey, privateKey) else null
	}

	fun removeAndroidKeyStoreKey(alias: String) = this.keyStore.deleteEntry(alias)

	private fun createAndroidKeyStore(): KeyStore {
		val keyStore = KeyStore.getInstance("AndroidKeyStore")
		keyStore.load(null)
		return keyStore
	}

	@TargetApi(Build.VERSION_CODES.M)
	private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
		val builder = KeyGenParameterSpec.Builder(
			alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
		)
			.setBlockModes(KeyProperties.BLOCK_MODE_ECB)
			.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)

		generator.initialize(builder.build())
	}

	private fun initGeneratorWithKeyPairGeneratorSpec(
		generator: KeyPairGenerator,
		alias: String,
		context: Context
	) {
		val startDate = Calendar.getInstance()
		val endDate = Calendar.getInstance()
		endDate.add(Calendar.YEAR, 20)

		val builder = KeyPairGeneratorSpec.Builder(context)
			.setAlias(alias)
			.setSerialNumber(BigInteger.ONE)
			.setSubject(X500Principal("CN=$alias CA Certificate"))
			.setStartDate(startDate.time)
			.setEndDate(endDate.time)

		generator.initialize(builder.build())
	}
}

class CipherWrapper {
	private val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")

	fun decrypt(data: String, key: Key?): String {
		this.cipher.init(Cipher.DECRYPT_MODE, key)
		val encryptedData = Base64.decode(data, Base64.DEFAULT)
		val decodedData = this.cipher.doFinal(encryptedData)
		return String(decodedData)
	}

	fun encrypt(data: String, key: Key?): String {
		this.cipher.init(Cipher.ENCRYPT_MODE, key)
		val bytes = this.cipher.doFinal(data.toByteArray())
		return Base64.encodeToString(bytes, Base64.DEFAULT)
	}
}
