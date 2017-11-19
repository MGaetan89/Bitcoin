package io.bitcoin

object Config {
	const val ACTION_EXCHANGES_UPDATED = BuildConfig.APPLICATION_ID + ".action.exchanges_updated"
	const val ACTION_ORDER_ADDED = BuildConfig.APPLICATION_ID + ".action.order_added"
	const val ACTION_PRICE_UPDATE = BuildConfig.APPLICATION_ID + ".action.price_update"

	const val EXTRA_CURRENCY_PAIR = BuildConfig.APPLICATION_ID + ".extra.currency_pair"
	const val EXTRA_PRICE = BuildConfig.APPLICATION_ID + ".extra.price"
}
