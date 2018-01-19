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
import io.crypto.bitstamp.model.PriceTransaction
import io.crypto.bitstamp.model.TradingPair
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_price_transaction.amount
import kotlinx.android.synthetic.main.adapter_price_transaction.amount_currency
import kotlinx.android.synthetic.main.adapter_price_transaction.date
import kotlinx.android.synthetic.main.adapter_price_transaction.price
import kotlinx.android.synthetic.main.adapter_price_transaction.price_currency
import kotlinx.android.synthetic.main.adapter_price_transaction.time
import kotlinx.android.synthetic.main.adapter_price_transaction.transaction_id
import kotlinx.android.synthetic.main.adapter_price_transaction.type
import kotlinx.android.synthetic.main.adapter_price_transaction.value
import kotlinx.android.synthetic.main.adapter_price_transaction.value_currency

class PriceTransactionsAdapter(private val tradingPair: TradingPair) :
	RecyclerView.Adapter<PriceTransactionsAdapter.ViewHolder>() {
	private val transactions = mutableListOf<PriceTransaction>()

	override fun getItemCount() = this.transactions.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val transaction = this.transactions.getOrNull(position) ?: return
		val typeObject = transaction.typeObject

		if (typeObject != null) {
			holder.type.setText(typeObject.textRes)
			holder.type.setTextColorResource(typeObject.colorRes)
		} else {
			holder.type.text = null
			holder.type.setTextColorResource(R.color.text)
		}

		holder.amount.text = transaction.amount.toFormattedString(this.tradingPair.baseDecimals)
		holder.amount_currency.text = this.tradingPair.baseCurrency
		holder.date.text = transaction.date.toFormattedDate()
		holder.price.text = transaction.price.toFormattedString(this.tradingPair.counterDecimals)
		holder.price_currency.text = this.tradingPair.counterCurrency
		holder.time.text = transaction.date.toFormattedTime()
		holder.transaction_id.text = "${transaction.transactionId}"
		holder.value.text =
				(transaction.amount * transaction.price).toFormattedString(this.tradingPair.counterDecimals)
		holder.value_currency.text = this.tradingPair.counterCurrency
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_price_transaction)

		return ViewHolder(view)
	}

	fun updateTransactions(transactions: List<PriceTransaction>) {
		val diffResult =
			DiffUtil.calculateDiff(TransactionsDiffCallback(this.transactions, transactions), true)

		this.transactions.clear()
		this.transactions.addAll(transactions)

		diffResult.dispatchUpdatesTo(this)
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
		LayoutContainer

	private class TransactionsDiffCallback(
		private val oldItems: List<PriceTransaction>,
		private val newItems: List<PriceTransaction>
	) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems.getOrNull(newItemPosition)?.amount == this.oldItems.getOrNull(
				oldItemPosition
			)?.amount
					&& this.newItems.getOrNull(newItemPosition)?.price == this.oldItems.getOrNull(
				oldItemPosition
			)?.price
		}

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems.getOrNull(newItemPosition)?.transactionId == this.oldItems.getOrNull(
				oldItemPosition
			)?.transactionId
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
