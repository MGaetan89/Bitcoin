package io.crypto.bitstamp.network

import io.crypto.bitstamp.model.Account
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BitstampServices : Interceptor {
	private val okHttpClient = this.createOkHttpClient(false)

	var account: Account? = null
	val privateOkHttpClient = this.createOkHttpClient(true)
	val api = this.createService(false)
	val privateApi = this.createService(true)

	override fun intercept(chain: Interceptor.Chain): Response {
		// External inputs
		val account = this.account ?: return chain.proceed(chain.request())
		val (apiKey, customerId, secret) = account

		// Signature generation
		val nonce = System.nanoTime().toString()
		val signature = ByteString.encodeUtf8(nonce + customerId + apiKey)
			.hmacSha256(ByteString.encodeUtf8(secret)).hex().toUpperCase()

		// Request creation
		val url = chain.request().url()
		val newUrlBuilder = url.newBuilder()
		val formBodyBuilder = FormBody.Builder()
			.add("key", apiKey)
			.add("nonce", nonce)
			.add("signature", signature)
		url.queryParameterNames().forEach {
			formBodyBuilder.add(it, url.queryParameterValues(it).first())
			newUrlBuilder.removeAllQueryParameters(it)
		}

		val request = chain.request().newBuilder()
			.post(formBodyBuilder.build())
			.url(newUrlBuilder.build())
			.build()

		return chain.proceed(request)
	}

	private fun createOkHttpClient(privateAccess: Boolean): OkHttpClient {
		val clientBuilder = OkHttpClient.Builder()

		if (privateAccess) {
			clientBuilder.addInterceptor(this)
		}

		clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
			this.level = HttpLoggingInterceptor.Level.BODY
		})

		return clientBuilder.build()
	}

	private fun createService(privateAccess: Boolean): BitstampApi {
		return Retrofit.Builder()
			.baseUrl("https://www.bitstamp.net/")
			.addConverterFactory(MoshiConverterFactory.create())
			.client(if (privateAccess) this.privateOkHttpClient else this.okHttpClient)
			.build()
			.create(BitstampApi::class.java)
	}
}
