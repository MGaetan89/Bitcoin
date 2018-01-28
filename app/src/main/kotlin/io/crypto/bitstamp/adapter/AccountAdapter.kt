package io.crypto.bitstamp.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.inflate
import io.crypto.bitstamp.model.Account
import kotlinx.android.extensions.LayoutContainer

class AccountAdapter(
	private val accounts: List<Account>,
	private val listener: OnAccountEventListener
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
	interface OnAccountEventListener {
		fun onAccountSelected(account: Account)

		fun onAddAccount()
	}

	private enum class ViewType(@LayoutRes val layoutId: Int) {
		ACCOUNT(R.layout.adapter_account),
		ADD_ACCOUNT(R.layout.adapter_new_account)
	}

	override fun getItemCount() = this.accounts.size + 1

	override fun getItemViewType(position: Int): Int {
		return when {
			position < this.accounts.size -> ViewType.ACCOUNT.ordinal
			else -> ViewType.ADD_ACCOUNT.ordinal
		}
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val account = this.accounts.getOrNull(position) ?: return

		holder.name?.text = account.customerId
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(ViewType.values()[viewType].layoutId)

		return ViewHolder(view)
	}

	inner class ViewHolder(override val containerView: View) :
		RecyclerView.ViewHolder(containerView),
		LayoutContainer, View.OnClickListener {
		val name = this.containerView.findViewById<TextView?>(R.id.name)

		init {
			this.containerView.setOnClickListener(this)
		}

		override fun onClick(view: View) {
			if (this.adapterPosition < accounts.size) {
				listener.onAccountSelected(accounts[this.adapterPosition])
			} else {
				listener.onAddAccount()
			}
		}
	}
}
