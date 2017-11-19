package io.bitcoin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.bitcoin.R
import io.bitcoin.model.CurrencyPair
import io.bitcoin.model.Prices
import io.bitcoin.view.AnimatedTextView
import java.text.NumberFormat

class ExchangeAdapter : RecyclerView.Adapter<ExchangeAdapter.ViewHolder>() {
	val currencyPairs = mutableListOf<CurrencyPair>()
	private var prices = arrayOfNulls<Prices>(this.currencyPairs.size)

	override fun getItemCount() = this.prices.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindTo(this.currencyPairs[position], this.prices[position])
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.of(parent)

	fun updateCurrencyPairs(currencyPairs: List<CurrencyPair>) {
		this.currencyPairs.clear()
		this.currencyPairs.addAll(currencyPairs)

		this.prices = arrayOfNulls(this.currencyPairs.size)

		this.notifyDataSetChanged()
	}

	fun updatePrice(currencyPair: CurrencyPair, prices: Prices) {
		val index = this.currencyPairs.indexOf(currencyPair)

		if (index >= 0) {
			this.prices[index] = prices

			this.notifyItemChanged(index)
		}
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val ask = view.findViewById<AnimatedTextView>(R.id.ask_value)
		private val bid = view.findViewById<AnimatedTextView>(R.id.bid_value)
		private val currency = view.findViewById<TextView>(R.id.currency_pair)

		fun bindTo(currencyPair: CurrencyPair, prices: Prices?) {
			val numberFormat = currencyPair.getNumberFormat()

			this.setPrice(this.ask, prices?.ask, numberFormat)
			this.setPrice(this.bid, prices?.bid, numberFormat)

			this.currency.text = currencyPair.toString()
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
