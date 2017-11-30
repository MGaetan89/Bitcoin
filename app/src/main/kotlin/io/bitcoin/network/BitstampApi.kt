package io.bitcoin.network

import com.pusher.client.Pusher
import com.pusher.client.channel.SubscriptionEventListener
import io.bitcoin.BuildConfig

object BitstampApi {
	private val pusher = Pusher(BuildConfig.PUSHER_API_KEY)

	@Suppress("EnumEntryName")
	enum class Channel {
		order_book
	}

	@Suppress("EnumEntryName")
	enum class Event {
		data
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
