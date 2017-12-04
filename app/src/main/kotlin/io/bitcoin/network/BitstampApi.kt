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

	fun subscribeTo(channel: Channel, event: Event, urlSymbols: Iterable<String>, listener: SubscriptionEventListener) {
		this.pusher.connect()

		urlSymbols.toSet().forEach {
			val channelName = if (it.isEmpty()) channel.name else "${channel}_$it"

			if (this.pusher.getChannel(channelName)?.isSubscribed != true) {
				this.pusher.subscribe(channelName).bind(event.name, listener)
			}
		}
	}

	fun unSubscribeFrom(channel: Channel, urlSymbols: Iterable<String>) {
		urlSymbols.toSet().forEach {
			val channelName = if (it.isEmpty()) channel.name else "${channel}_$it"

			if (this.pusher.getChannel(channelName)?.isSubscribed == true) {
				this.pusher.unsubscribe(channelName)
			}
		}

		this.pusher.disconnect()
	}
}
