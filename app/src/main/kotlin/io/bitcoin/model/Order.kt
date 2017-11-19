package io.bitcoin.model

data class Order(val currencyPair: CurrencyPair, val fees: Float, val id: Int, val quantity: Double, val unitPrice: Double) {
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

	private enum class Mode {
		BUY, SELL
	}

	private fun computeNetAmount(unitPrice: Double, quantity: Double, fees: Float, mode: Mode) = when (mode) {
		Mode.BUY -> unitPrice * quantity * (1 + fees)
		Mode.SELL -> unitPrice * quantity * (1 - fees)
	}
}
