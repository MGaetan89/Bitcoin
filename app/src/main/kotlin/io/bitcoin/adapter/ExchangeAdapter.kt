package io.bitcoin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.bitcoin.R
import io.bitcoin.model.Prices
import io.bitcoin.model.TradingPair
import io.bitcoin.view.AnimatedTextView
import java.text.NumberFormat

class ExchangeAdapter : RecyclerView.Adapter<ExchangeAdapter.ViewHolder>() {
	val tradingPairs = mutableListOf<TradingPair>()
	private var prices = arrayOfNulls<Prices>(this.tradingPairs.size)

	override fun getItemCount() = this.prices.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindTo(this.tradingPairs[position], this.prices[position])
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.of(parent)

	fun updateTradingPairs(tradingPairs: List<TradingPair>) {
		this.tradingPairs.clear()
		this.tradingPairs.addAll(tradingPairs)

		this.prices = arrayOfNulls(this.tradingPairs.size)

		this.notifyDataSetChanged()
	}

	fun updatePrice(tradingPair: TradingPair, prices: Prices) {
		val index = this.tradingPairs.indexOf(tradingPair)

		if (index >= 0) {
			this.prices[index] = prices

			this.notifyItemChanged(index)
		}
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val ask = view.findViewById<AnimatedTextView>(R.id.ask)
		private val bid = view.findViewById<AnimatedTextView>(R.id.bid)
		private val tradingPair = view.findViewById<TextView>(R.id.currency_pair)

		fun bindTo(tradingPair: TradingPair, prices: Prices?) {
			val numberFormat = tradingPair.getCounterNumberFormat()

			this.setPrice(this.ask, prices?.ask, numberFormat)
			this.setPrice(this.bid, prices?.bid, numberFormat)

			this.tradingPair.text = tradingPair.toString()
		}

		private fun setPrice(textView: AnimatedTextView, price: Double?, numberFormat: NumberFormat) {
			if (price != null) {
				textView.setValue(price, numberFormat)
			} else {
				textView.setText(R.string.no_price)
			}
		}

		companion object {
			fun of(parent: ViewGroup): ViewHolder {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_exchange, parent, false)

				return ViewHolder(view)
			}
		}
	}
}
