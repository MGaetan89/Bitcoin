package io.crypto.bitstamp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.Json
import io.crypto.bitstamp.BuildConfig
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
data class TradingPair(
	@Json(name = "base_decimals") val baseDecimals: Int,
	@Json(name = "counter_decimals") val counterDecimals: Int,
	val description: String,
	@Json(name = "minimum_order") val minimumOrder: String,
	val name: String,
	val trading: Trading,
	@Json(name = "url_symbol") val urlSymbol: String
) : Parcelable {
	companion object {
		const val EXTRA = BuildConfig.APPLICATION_ID + ".extra.trading_pair"
	}

	enum class Trading {
		Disabled, Enabled
	}

	val baseCurrency get() = this.name.split('/').firstOrNull() ?: ""

	val counterCurrency get() = this.name.split('/').getOrNull(1) ?: ""
}
