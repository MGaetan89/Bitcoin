package io.bitcoin.network

import com.pusher.client.Pusher
import com.pusher.client.channel.SubscriptionEventListener
import io.bitcoin.BuildConfig
import io.bitcoin.model.TradingPair
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BitstampApi {
	private val pusher = Pusher(BuildConfig.PUSHER_API_KEY)
	private val services = Retrofit.Builder()
			.baseUrl("https://www.bitstamp.net/api/")
			.addConverterFactory(MoshiConverterFactory.create())
			.client(OkHttpClient.Builder()
					.addInterceptor(HttpLoggingInterceptor().apply {
						this.level = HttpLoggingInterceptor.Level.BODY
					})
					.build())
			.build()
			.create(BitstampServices::class.java)

	@Suppress("EnumEntryName")
	enum class Channel {
		order_book
	}

	@Suppress("EnumEntryName")
	enum class Event {
		data
	}

	suspend fun getTradingPairs(): List<TradingPair>? {
		return this.services.getTradingPairs()
				.execute()
				.takeIf { it.isSuccessful }
				?.body()
	}

	fun subscribeTo(channel: Channel, event: Event, currencyPairs: List<String>, listener: SubscriptionEventListener) {
		currencyPairs.toSet().forEach {
			this.pusher.subscribe(if (it.isEmpty()) channel.name else "${channel}_$it")
					.bind(event.name, listener)
		}

		this.pusher.connect()
	}

	fun unSubscribeFrom(channel: Channel, currencyPairs: List<String>) {
		currencyPairs.toSet().forEach {
			if (it.isEmpty()) {
				this.pusher.unsubscribe(channel.name)
			} else {
				this.pusher.unsubscribe("${channel}_$it")
			}
		}

		this.pusher.disconnect()
	}
}
