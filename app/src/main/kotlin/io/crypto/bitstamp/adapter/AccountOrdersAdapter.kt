package io.crypto.bitstamp.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.extension.setTextColorResource
import io.crypto.bitstamp.extension.toFormattedDate
import io.crypto.bitstamp.extension.toFormattedString
import io.crypto.bitstamp.extension.toFormattedTime
import io.crypto.bitstamp.model.OpenOrder
import io.crypto.bitstamp.model.TradingPair
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_account_transaction.date
import kotlinx.android.synthetic.main.adapter_account_transaction.time
import kotlinx.android.synthetic.main.adapter_account_transaction.type
import kotlinx.android.synthetic.main.adapter_price_transaction.amount
import kotlinx.android.synthetic.main.adapter_price_transaction.amount_currency
import kotlinx.android.synthetic.main.adapter_price_transaction.price
import kotlinx.android.synthetic.main.adapter_price_transaction.price_currency
import kotlinx.android.synthetic.main.adapter_price_transaction.transaction_id
import kotlinx.android.synthetic.main.adapter_price_transaction.value
import kotlinx.android.synthetic.main.adapter_price_transaction.value_currency

class AccountOrdersAdapter : RecyclerView.Adapter<AccountOrdersAdapter.ViewHolder>() {
	private val orders = mutableListOf<OpenOrder>()
	private val tradingPairs = mutableListOf<TradingPair>()

	override fun getItemCount() = this.orders.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val order = this.orders.getOrNull(position) ?: return
		val typeObject = order.typeObject
		val tradingPair = this.tradingPairs.firstOrNull { it.urlSymbol == order.urlSymbol }
		val baseDecimals = tradingPair?.baseDecimals ?: 0
		val counterDecimals = tradingPair?.counterDecimals ?: 0

		if (typeObject != null) {
			holder.type.setText(typeObject.textRes)
			holder.type.setTextColorResource(typeObject.colorRes)
		} else {
			holder.type.text = null
			holder.type.setTextColorResource(R.color.text)
		}

		holder.amount.text = order.amount.toFormattedString(baseDecimals)
		holder.amount_currency.text = tradingPair?.baseCurrency
		holder.date.text = order.date?.toFormattedDate()
		holder.price.text = order.price.toFormattedString(counterDecimals)
		holder.price_currency.text = tradingPair?.counterCurrency
		holder.time.text = order.date?.toFormattedTime()
		holder.transaction_id.text = "${order.id}"
		holder.value.text = order.value.toFormattedString(counterDecimals)
		holder.value_currency.text = tradingPair?.counterCurrency
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_account_order)

		return ViewHolder(view)
	}

	fun updateOrders(orders: List<OpenOrder>) {
		val diffResult = DiffUtil.calculateDiff(OrdersDiffCallback(this.orders, orders))

		this.orders.clear()
		this.orders.addAll(orders)

		diffResult.dispatchUpdatesTo(this)
	}

	fun updateTradingPairs(tradingPairs: List<TradingPair>) {
		this.tradingPairs.clear()
		this.tradingPairs.addAll(tradingPairs)

		this.notifyDataSetChanged()
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

	private class OrdersDiffCallback(private val oldItems: List<OpenOrder>, private val newItems: List<OpenOrder>) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems.getOrNull(newItemPosition)?.id == this.oldItems.getOrNull(oldItemPosition)?.id
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
