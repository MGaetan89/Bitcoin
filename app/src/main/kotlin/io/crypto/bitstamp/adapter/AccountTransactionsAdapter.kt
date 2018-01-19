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
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.model.UserTransaction
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_account_transaction.date
import kotlinx.android.synthetic.main.adapter_account_transaction.fee
import kotlinx.android.synthetic.main.adapter_account_transaction.fee_currency
import kotlinx.android.synthetic.main.adapter_account_transaction.time
import kotlinx.android.synthetic.main.adapter_account_transaction.type
import kotlinx.android.synthetic.main.adapter_price_transaction.amount
import kotlinx.android.synthetic.main.adapter_price_transaction.amount_currency
import kotlinx.android.synthetic.main.adapter_price_transaction.price
import kotlinx.android.synthetic.main.adapter_price_transaction.price_currency
import kotlinx.android.synthetic.main.adapter_price_transaction.transaction_id
import kotlinx.android.synthetic.main.adapter_price_transaction.value
import kotlinx.android.synthetic.main.adapter_price_transaction.value_currency

class AccountTransactionsAdapter : RecyclerView.Adapter<AccountTransactionsAdapter.ViewHolder>() {
	private val tradingPairs = mutableListOf<TradingPair>()
	private val transactions = mutableListOf<UserTransaction>()

	override fun getItemCount() = this.transactions.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val transaction = this.transactions.getOrNull(position) ?: return
		val transactionInfo = transaction.transactionInfo
		val tradingPair =
			this.tradingPairs.firstOrNull { it.urlSymbol == transactionInfo?.urlSymbol }
		val baseDecimals = tradingPair?.baseDecimals ?: 0
		val counterDecimals = tradingPair?.counterDecimals ?: 0

		if (transaction.typeLabel != 0) {
			holder.type.setText(transaction.typeLabel)
		} else {
			holder.type.text = null
		}

		holder.amount.text = transactionInfo?.amount?.toFormattedString(baseDecimals)
		holder.amount_currency.text = tradingPair?.baseCurrency
		holder.date.text = transaction.date?.toFormattedDate()
		holder.fee.text = transactionInfo?.fee?.toFormattedString(counterDecimals)
		holder.fee_currency.text = tradingPair?.counterCurrency
		holder.price.text = transactionInfo?.price?.toFormattedString(counterDecimals)
		holder.price_currency.text = tradingPair?.counterCurrency
		holder.time.text = transaction.date?.toFormattedTime()
		holder.transaction_id.text = "${transaction.id}"
		holder.type.setTextColorResource(transaction.color)
		holder.value.text = transactionInfo?.value?.toFormattedString(counterDecimals)
		holder.value_currency.text = tradingPair?.counterCurrency
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_account_transaction)

		return ViewHolder(view)
	}

	fun updateTradingPairs(tradingPairs: List<TradingPair>) {
		this.tradingPairs.clear()
		this.tradingPairs.addAll(tradingPairs)

		this.notifyDataSetChanged()
	}

	fun updateTransactions(transactions: List<UserTransaction>) {
		val diffResult =
			DiffUtil.calculateDiff(TransactionsDiffCallback(this.transactions, transactions), true)

		this.transactions.clear()
		this.transactions.addAll(transactions)

		diffResult.dispatchUpdatesTo(this)
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
		LayoutContainer

	private class TransactionsDiffCallback(
		private val oldItems: List<UserTransaction>,
		private val newItems: List<UserTransaction>
	) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems.getOrNull(newItemPosition)?.id == this.oldItems.getOrNull(
				oldItemPosition
			)?.id
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
