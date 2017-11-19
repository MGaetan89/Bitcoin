package io.bitcoin.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.NumberFormat

@Parcelize
@SuppressLint("ParcelCreator")
data class CurrencyPair(private val left: String, private val right: String) : Parcelable {
	fun getDecimalsCount() = when (this.left) {
		"BTC", "ETH", "LTC" -> when (this.right) {
			"BTC" -> 8
			else -> 2
		}
		"EUR", "USD", "XRP" -> when (this.right) {
			"BTC", "XRP" -> 8
			"EUR", "USD" -> 5
			else -> 2
		}
		else -> 2
	}

	fun getNumberFormat(): NumberFormat {
		val decimalsCount = this.getDecimalsCount()

		return NumberFormat.getNumberInstance().also {
			it.maximumFractionDigits = decimalsCount
			it.minimumFractionDigits = decimalsCount
		}
	}

	fun reciprocal() = CurrencyPair(this.right, this.left)

	fun toTag(): String {
		val tag = "${this.left.toLowerCase()}${this.right.toLowerCase()}"

		return if (tag == "btcusd") "" else tag
	}

	override fun toString() = "${this.left} / ${this.right}"
}
