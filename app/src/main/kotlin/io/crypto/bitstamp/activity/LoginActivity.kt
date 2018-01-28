package io.crypto.bitstamp.activity

import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountAdapter
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.model.Account
import io.crypto.bitstamp.network.BitstampServices

class LoginActivity : BaseActivity(), AccountAdapter.OnAccountEventListener {
	override fun getContentResourceId() = R.layout.activity_login

	override fun getSelectedTabId() = R.id.menu_account

	override fun getToolbarTitle(): CharSequence = this.getString(R.string.login)

	override fun onAccountSelected(account: Account) {
		BitstampServices.account = account

		this.startActivity<AccountActivity>(
			Intent.FLAG_ACTIVITY_NEW_TASK,
			Intent.FLAG_ACTIVITY_CLEAR_TASK
		)
	}

	override fun onAddAccount() {
		this.startActivity<AddAccountActivity>()
	}

	override fun onInflate(stub: ViewStub, inflated: View) {
		(inflated as RecyclerView).let {
			it.adapter = AccountAdapter(emptyList(), this)
			it.layoutManager = LinearLayoutManager(this)
			it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
		}

		super.onInflate(stub, inflated)
	}

	override fun onResume() {
		super.onResume()

		if (BitstampServices.account != null) {
			this.startActivity<AccountActivity>(
				Intent.FLAG_ACTIVITY_NEW_TASK,
				Intent.FLAG_ACTIVITY_CLEAR_TASK
			)
		}
	}
}
