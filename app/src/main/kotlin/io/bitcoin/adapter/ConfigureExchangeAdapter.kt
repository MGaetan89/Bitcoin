package io.bitcoin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import io.bitcoin.R
import io.bitcoin.extension.toCurrencyPair

class ConfigureExchangeAdapter(
		private val currencyPairs: Array<String>, val selectedCurrencyPairs: MutableList<String>
) : RecyclerView.Adapter<ConfigureExchangeAdapter.ViewHolder>() {
	override fun getItemCount() = this.currencyPairs.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindTo(this.currencyPairs[position])
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
			= ViewHolder.of(parent, this.currencyPairs, this.selectedCurrencyPairs)

	class ViewHolder(
			view: View, private val currencyPairs: Array<String>, private val selectedCurrencyPairs: MutableList<String>
	) : RecyclerView.ViewHolder(view), CompoundButton.OnCheckedChangeListener {
		private val checkbox = view.findViewById<CheckBox>(android.R.id.checkbox).also {
			it.setOnCheckedChangeListener(this)
		}

		fun bindTo(currencyPair: String) {
			this.checkbox.isChecked = currencyPair in this.selectedCurrencyPairs
			this.checkbox.text = currencyPair.toCurrencyPair().toString()
		}

		override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) {
			val currencyPair = currencyPairs.getOrNull(this.adapterPosition) ?: return

			if (isChecked) {
				if (currencyPair !in selectedCurrencyPairs) {
					selectedCurrencyPairs.add(currencyPair)
				}
			} else {
				selectedCurrencyPairs.remove(currencyPair)
			}
		}

		companion object {
			fun of(parent: ViewGroup, currencyPairs: Array<String>, selectedCurrencyPairs: MutableList<String>): ViewHolder {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_configure_exchange, parent, false)

				return ViewHolder(view, currencyPairs, selectedCurrencyPairs)
			}
		}
	}
}
