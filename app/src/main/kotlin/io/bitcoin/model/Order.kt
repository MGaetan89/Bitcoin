package io.bitcoin.model

class Order(
		@JvmField val tradingPair: TradingPair, private val fees: Float, @JvmField val id: Int,
		@JvmField val quantity: Double, @JvmField val unitPrice: Double
) {
	override fun equals(other: Any?) = (other as? Order)?.id == this.id

	fun getGain(lastPrice: Double): Double {
		val netAmount = computeNetAmount(this.unitPrice, this.quantity, this.fees, Mode.BUY)
		val lastPriceNetAmount = computeNetAmount(lastPrice, this.quantity, this.fees, Mode.SELL)

		return lastPriceNetAmount - netAmount
	}

	fun getGainPercent(lastPrice: Double): Double {
		val netAmount = computeNetAmount(this.unitPrice, this.quantity, this.fees, Mode.BUY)
		val lastPriceNetAmount = computeNetAmount(lastPrice, this.quantity, this.fees, Mode.SELL)

		return (lastPriceNetAmount - netAmount) / netAmount
	}

	override fun hashCode(): Int {
		var result = this.tradingPair.hashCode()
		result = 31 * result + this.fees.hashCode()
		result = 31 * result + this.id
		result = 31 * result + this.quantity.hashCode()
		result = 31 * result + this.unitPrice.hashCode()
		return result
	}

	private fun computeNetAmount(unitPrice: Double, quantity: Double, fees: Float, mode: Mode) = when (mode) {
		Mode.BUY -> unitPrice * quantity * (1 + fees)
		Mode.SELL -> unitPrice * quantity * (1 - fees)
	}

	private enum class Mode {
		BUY, SELL
	}
}
