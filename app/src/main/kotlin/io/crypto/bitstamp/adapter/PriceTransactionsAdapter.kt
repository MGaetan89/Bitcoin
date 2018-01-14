package io.crypto.bitstamp.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.extension.setTextColorResource
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.model.Transaction
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
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.text.DateFormat
import java.text.NumberFormat

class PriceTransactionsAdapter(private val tradingPair: TradingPair) : RecyclerView.Adapter<PriceTransactionsAdapter.ViewHolder>() {
	private val amountFormat = NumberFormat.getNumberInstance().also {
		it.maximumFractionDigits = this.tradingPair.baseDecimals
		it.minimumFractionDigits = this.tradingPair.baseDecimals
	}
	private val dateFormat = DateFormat.getDateInstance()
	private val priceFormat = NumberFormat.getNumberInstance().also {
		it.maximumFractionDigits = this.tradingPair.counterDecimals
		it.minimumFractionDigits = this.tradingPair.counterDecimals
	}
	private val timeFormat = DateFormat.getTimeInstance()
	private val transactions = mutableListOf<Transaction>()

	override fun getItemCount() = this.transactions.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val transaction = this.transactions.getOrNull(position) ?: return
		val typeObject = transaction.typeObject

		holder.amount.text = this.amountFormat.format(transaction.amount)
		holder.amount_currency.text = this.tradingPair.baseCurrency
		holder.date.text = this.dateFormat.format(transaction.date * 1000)
		holder.price.text = this.priceFormat.format(transaction.price)
		holder.price_currency.text = this.tradingPair.counterCurrency
		holder.time.text = this.timeFormat.format(transaction.date * 1000)
		holder.transaction_id.text = "${transaction.transactionId}"
		holder.type.setText(typeObject.textRes)
		holder.type.setTextColorResource(typeObject.colorRes)
		holder.value.text = this.priceFormat.format(transaction.amount * transaction.price)
		holder.value_currency.text = this.tradingPair.counterCurrency
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_price_transaction)

		return ViewHolder(view)
	}

	suspend fun updateTransactions(transactions: List<Transaction>) {
		val diffResult = DiffUtil.calculateDiff(TransactionsDiffCallback(this.transactions, transactions), true)

		this.transactions.clear()
		this.transactions.addAll(transactions)

		launch(UI) {
			diffResult.dispatchUpdatesTo(this@PriceTransactionsAdapter)
		}
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

	private class TransactionsDiffCallback(private val oldItems: List<Transaction>, private val newItems: List<Transaction>) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems[newItemPosition].transactionId == this.oldItems[oldItemPosition].transactionId
		}

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems[newItemPosition].transactionId == this.oldItems[oldItemPosition].transactionId
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
