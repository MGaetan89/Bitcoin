package io.crypto.bitstamp.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.model.UserTransaction
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_account_transaction.id

class AccountTransactionsAdapter : RecyclerView.Adapter<AccountTransactionsAdapter.ViewHolder>() {
	private val transactions = mutableListOf<UserTransaction>()

	override fun getItemCount() = this.transactions.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val transaction = this.transactions.getOrNull(position) ?: return

		holder.id.text = "${transaction.id}"
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_account_transaction)

		return ViewHolder(view)
	}

	fun updateTransactions(transactions: List<UserTransaction>) {
		val diffResult = DiffUtil.calculateDiff(TransactionsDiffCallback(this.transactions, transactions), true)

		this.transactions.clear()
		this.transactions.addAll(transactions)

		diffResult.dispatchUpdatesTo(this)
	}

	class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

	private class TransactionsDiffCallback(private val oldItems: List<UserTransaction>, private val newItems: List<UserTransaction>) : DiffUtil.Callback() {
		override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true

		override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
			return this.newItems.getOrNull(newItemPosition)?.id == this.oldItems.getOrNull(oldItemPosition)?.id
		}

		override fun getNewListSize() = this.newItems.size

		override fun getOldListSize() = this.oldItems.size
	}
}
