package io.crypto.bitstamp.network

import io.crypto.bitstamp.model.AccountBalance
import io.crypto.bitstamp.model.CanceledOrder
import io.crypto.bitstamp.model.OpenOrder
import io.crypto.bitstamp.model.OpenOrderStatus
import io.crypto.bitstamp.model.PriceOrderBook
import io.crypto.bitstamp.model.PriceTransaction
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.model.UserTransaction
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BitstampServices : Interceptor {
	private val api = this.createService(false)
	private val privateApi = this.createService(true)

	suspend fun cancelOrder(id: Long): CanceledOrder {
		return this.privateApi.cancelOrder(id)
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: CanceledOrder.EMPTY
	}

	suspend fun getAccountBalance(): AccountBalance {
		return this.privateApi.getAccountBalance()
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: AccountBalance.EMPTY
	}

	suspend fun getOpenOrders(): List<OpenOrder> {
		return this.privateApi.getOpenOrders()
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: emptyList()
	}

	suspend fun getOrderBook(currencyPair: String): PriceOrderBook {
		return this.api.getOrderBook(currencyPair)
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: PriceOrderBook.EMPTY
	}

	suspend fun getOrderStatus(id: Long): OpenOrderStatus {
		return this.privateApi.getOrderStatus(id)
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: OpenOrderStatus.EMPTY
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

	suspend fun getTransactions(currencyPair: String): List<PriceTransaction> {
		return this.api.getTransactions(currencyPair)
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: emptyList()
	}

	suspend fun getUserTransactions(): List<UserTransaction> {
		return this.privateApi.getUserTransactions()
			.execute()
			.takeIf { it.isSuccessful }
			?.body() ?: emptyList()
	}

	override fun intercept(chain: Interceptor.Chain): Response {
		// External inputs
		val customerId = ""
		val key = ""
		val secret = ByteString.encodeUtf8("")

		// Signature generation
		val nonce = System.nanoTime().toString()
		val message = nonce + customerId + key
		val signature = ByteString.encodeUtf8(message).hmacSha256(secret).hex().toUpperCase()

		// Request creation
		val url = chain.request().url()
		val newUrlBuilder = url.newBuilder()
		val formBodyBuilder = FormBody.Builder()
			.add("key", key)
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
