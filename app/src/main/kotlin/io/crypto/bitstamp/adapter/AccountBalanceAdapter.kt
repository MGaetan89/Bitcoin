package io.crypto.bitstamp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.extension.toFormattedString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_account_balance.amount
import kotlinx.android.synthetic.main.adapter_account_balance.currency

class AccountBalanceAdapter : RecyclerView.Adapter<AccountBalanceAdapter.ViewHolder>() {
	private val balance = mutableListOf<Float>()
	private val currencies = mutableListOf<String>()

	override fun getItemCount() = this.balance.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val balance = this.balance.getOrNull(position) ?: return
		val currency = this.currencies.getOrNull(position) ?: return
		val precision = when (currency) {
			"EUR", "USD" -> 2
			else -> 8
		}

		holder.amount.text = balance.toFormattedString(precision)
		holder.currency.text = currency
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_account_balance)

		return ViewHolder(view)
	}

	fun updateBalance(balance: Map<String, Float>) {
		this.balance.clear()
		this.balance.addAll(balance.values)

		this.currencies.clear()
		this.currencies.addAll(balance.keys)

		this.notifyDataSetChanged()
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
