package io.bitcoin.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.bitcoin.R
import io.bitcoin.model.CurrencyPair
import io.bitcoin.model.Order
import java.text.NumberFormat

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
	private val lastPrices = mutableMapOf<CurrencyPair, Double>()
	private val orders = mutableListOf<Order>()

	override fun getItemCount() = this.orders.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val order = this.orders[position]

		holder.bindTo(order, this.lastPrices[order.currencyPair])
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.of(parent)

	fun removeOrder(order: Order) {
		val index = this.orders.indexOf(order)

		if (index >= 0) {
			this.orders.removeAt(index)

			this.notifyItemRemoved(index)
		}
	}

	fun updateOrders(orders: List<Order>) {
		this.orders.clear()
		this.orders.addAll(orders)

		this.notifyDataSetChanged()
	}

	fun updatePrice(currencyPair: CurrencyPair, price: Double) {
		this.lastPrices[currencyPair] = price

		this.orders.forEachIndexed { index, order ->
			if (order.currencyPair == currencyPair) {
				this.notifyItemChanged(index)
			}
		}
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val color: View = view.findViewById(R.id.color)
		private val currencyPair: TextView = view.findViewById(R.id.currency_pair)
		private val gain: TextView = view.findViewById(R.id.gain)
		private val gainPercent: TextView = view.findViewById(R.id.gain_percent)
		private val id: TextView = view.findViewById(R.id.id)
		private val quantity: TextView = view.findViewById(R.id.quantity)
		private val unitPrice: TextView = view.findViewById(R.id.unit_price)

		fun bindTo(order: Order, lastPrice: Double?) {
			val priceNumberFormat = order.currencyPair.getNumberFormat()
			val quantityNumberFormat = order.currencyPair.reciprocal().getNumberFormat()

			if (lastPrice != null) {
				val gain = order.getGain(lastPrice)
				val gainPercent = order.getGainPercent(lastPrice)
				val colorResource = if (gain < 0.0) R.color.ask else R.color.bid
				val color = ContextCompat.getColor(this.gain.context, colorResource)

				this.color.setBackgroundResource(colorResource)
				this.gain.text = priceNumberFormat.format(gain)
				this.gain.setTextColor(color)
				this.gainPercent.text = percentFormat.format(gainPercent)
				this.gainPercent.setTextColor(color)
			} else {
				val color = ContextCompat.getColor(this.gain.context, R.color.text)

				this.color.setBackgroundResource(R.color.background)
				this.gain.text = null
				this.gain.setTextColor(color)
				this.gainPercent.text = null
				this.gainPercent.setTextColor(color)
			}

			this.currencyPair.text = order.currencyPair.toString()
			this.id.text = this.id.resources.getString(R.string.order_id, order.id)
			this.quantity.text = quantityNumberFormat.format(order.quantity)
			this.unitPrice.text = priceNumberFormat.format(order.unitPrice)
		}

		companion object {
			private val percentFormat = NumberFormat.getPercentInstance().apply {
				this.maximumFractionDigits = 2
				this.minimumFractionDigits = 2
			}

			fun of(parent: ViewGroup): ViewHolder {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_order, parent, false)

				return ViewHolder(view)
			}
		}
	}
}
