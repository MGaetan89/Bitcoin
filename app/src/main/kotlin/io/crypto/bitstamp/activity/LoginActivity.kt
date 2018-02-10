package io.crypto.bitstamp.activity

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountAdapter
import io.crypto.bitstamp.dao.BitstampDatabase
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.fragment.AccountLoginFragment
import io.crypto.bitstamp.model.Account
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class LoginActivity : BaseActivity(), AccountAdapter.OnAccountEventListener, View.OnClickListener {
	private val accounts = mutableListOf<Account>()
	private val adapter = AccountAdapter(this.accounts, this)

	override fun getContentResourceId() = R.layout.activity_login

	override fun getSelectedTabId() = R.id.menu_account

	override fun getToolbarTitle(): CharSequence = this.getString(R.string.login)

	override fun onAccountSelected(account: Account) {
		AccountLoginFragment.newInstance(account)
			.show(this.supportFragmentManager, "pin_code")
	}

	override fun onClick(view: View) {
		when (view.id) {
			R.id.add_account -> this.displayAddAccount()
		}
	}

	override fun onInflate(stub: ViewStub, inflated: View) {
		val addAccount = inflated.findViewById<FloatingActionButton>(R.id.add_account)
		val list = inflated.findViewById<RecyclerView>(R.id.list)

		addAccount.setOnClickListener(this)
		list.let {
			it.adapter = this.adapter
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
		} else {
			this.fetchAccounts()
		}
	}

	private fun displayAddAccount() {
		this.startActivity<AddAccountActivity>()
	}

	private fun fetchAccounts() {
		val context = this
		launch {
			val database =
				Room.databaseBuilder(context, BitstampDatabase::class.java, "bitstamp").build()
			val accounts = database.accountDao().getAll()
			database.close()

			context.accounts.clear()
			context.accounts.addAll(accounts)

			launch(UI) {
				context.adapter.notifyDataSetChanged()
			}
		}
	}
}
