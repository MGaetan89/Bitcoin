package io.crypto.bitstamp.model

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.squareup.moshi.Json
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.toTimestamp

data class OpenOrder(
	val amount: Float,
	@Json(name = "currency_pair") val currencyPair: String,
	val datetime: String,
	val id: Long,
	val price: Float,
	val type: Int
) {
	enum class Type(@StringRes val textRes: Int, @ColorRes val colorRes: Int) {
		BUY(R.string.buy, R.color.bid),
		SELL(R.string.sell, R.color.ask)
	}

	val date get() = this.datetime.toTimestamp()

	val typeObject get() = Type.values().getOrNull(this.type)

	val urlSymbol get() = this.currencyPair.replace("/", "").toLowerCase()

	val value get() = this.amount * this.price
}
