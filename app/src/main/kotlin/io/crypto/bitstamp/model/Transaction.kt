package io.crypto.bitstamp.model

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.squareup.moshi.Json
import io.crypto.bitstamp.R

data class Transaction(
		val amount: Float,
		val date: Long,
		val price: Float,
		@Json(name = "tid") val transactionId: Long,
		val type: Int
) {
	enum class Type(@StringRes val textRes: Int, @ColorRes val colorRes: Int) {
		BUY(R.string.buy, R.color.bid),
		SELL(R.string.sell, R.color.ask)
	}

	val typeObject get() = Type.values()[this.type]
}
