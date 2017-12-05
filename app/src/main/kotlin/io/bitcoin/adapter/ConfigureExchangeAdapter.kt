package io.bitcoin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import io.bitcoin.R
import io.bitcoin.model.TradingPair

class ConfigureExchangeAdapter(
		private val tradingPairs: List<TradingPair>, val selectedTradingPairs: MutableList<String>
) : RecyclerView.Adapter<ConfigureExchangeAdapter.ViewHolder>() {
	override fun getItemCount() = this.tradingPairs.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindTo(this.tradingPairs[position])
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
			= ViewHolder.of(parent, this.tradingPairs, this.selectedTradingPairs)

	class ViewHolder(
			view: View, private val tradingPairs: List<TradingPair>, private val selectedTradingPairs: MutableList<String>
	) : RecyclerView.ViewHolder(view), CompoundButton.OnCheckedChangeListener {
		private val checkbox = view.findViewById<CheckBox>(android.R.id.checkbox).also {
			it.setOnCheckedChangeListener(this)
		}

		fun bindTo(tradingPair: TradingPair) {
			this.checkbox.isChecked = tradingPair.toUrlSymbol() in this.selectedTradingPairs
			this.checkbox.text = tradingPair.toString()
		}

		override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) {
			val tradingPair = this.tradingPairs.getOrNull(this.adapterPosition) ?: return

			if (isChecked) {
				if (tradingPair.toUrlSymbol() !in this.selectedTradingPairs) {
					this.selectedTradingPairs.add(tradingPair.toUrlSymbol())
				}
			} else {
				this.selectedTradingPairs.remove(tradingPair.toUrlSymbol())
			}
		}

		companion object {
			fun of(parent: ViewGroup, tradingPairs: List<TradingPair>, selectedTradingPairs: MutableList<String>): ViewHolder {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_configure_exchange, parent, false)

				return ViewHolder(view, tradingPairs, selectedTradingPairs)
			}
		}
	}
}
