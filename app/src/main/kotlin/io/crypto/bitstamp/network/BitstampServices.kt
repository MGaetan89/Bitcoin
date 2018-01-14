package io.crypto.bitstamp.network

import io.crypto.bitstamp.model.OrderBook
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.model.Transaction
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BitstampServices : Interceptor {
	private val api = this.createService(false)
	private val privateApi = this.createService(true)

	suspend fun getOrderBook(currencyPair: String): OrderBook {
		return this.api.getOrderBook(currencyPair)
				.execute()
				.takeIf { it.isSuccessful }
				?.body() ?: OrderBook.EMPTY
	}

	suspend fun getTicker(currencyPair: String): Ticker {
		return this.api.getTicker(currencyPair)
				.execute()
				.takeIf { it.isSuccessful }
				?.body() ?: Ticker.EMPTY
	}

	suspend fun getTradingPairs(): List<TradingPair> {
		return this.api.getTradingPairs()
				.execute()
				.takeIf { it.isSuccessful }
				?.body() ?: emptyList()
	}

	suspend fun getTransactions(currencyPair: String): List<Transaction> {
		return this.api.getTransactions(currencyPair)
				.execute()
				.takeIf { it.isSuccessful }
				?.body() ?: emptyList()
	}

	suspend fun getUserTransactions(): ResponseBody? {
		return this.privateApi.getUserTransactions()
				.execute()
				.takeIf { it.isSuccessful }
				?.body()
	}

	override fun intercept(chain: Interceptor.Chain): Response {
		// External inputs
		val customerId = ""
		val key = ""
		val secret = ByteString.encodeUtf8("")

		// Signature generation
		val nonce = System.currentTimeMillis().toString()
		val message = nonce + customerId + key
		val signature = ByteString.encodeUtf8(message).hmacSha256(secret).hex().toUpperCase()

		// Request creation
		val requestBody = FormBody.Builder()
				.add("key", key)
				.add("nonce", nonce)
				.add("signature", signature)
				.build()
		val request = chain.request().newBuilder()
				.post(requestBody)
				.build()

		return chain.proceed(request)
	}

	private fun createService(privateAccess: Boolean): BitstampApi {
		val clientBuilder = OkHttpClient.Builder()

		if (privateAccess) {
			clientBuilder.addInterceptor(this)
		}

		clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
			this.level = HttpLoggingInterceptor.Level.BODY
		})

		return Retrofit.Builder()
				.baseUrl("https://www.bitstamp.net/api/")
				.addConverterFactory(MoshiConverterFactory.create())
				.client(clientBuilder.build())
				.build()
				.create(BitstampApi::class.java)
	}
}