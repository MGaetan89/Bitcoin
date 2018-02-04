package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountBalanceAdapter
import io.crypto.bitstamp.model.AccountBalance
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_account_balance.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountBalanceFragment : Fragment(), Callback<AccountBalance> {
	companion object {
		fun newInstance() = AccountBalanceFragment()
	}

	private val adapter by lazy { AccountBalanceAdapter() }
	private val layoutManager by lazy { GridLayoutManager(this.context, 2) }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_account_balance, container, false)
	}

	override fun onFailure(call: Call<AccountBalance>, t: Throwable) {
		if (this.isAdded) {
			this.context?.let {
				Toast.makeText(it, t.localizedMessage, Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onResponse(call: Call<AccountBalance>, response: Response<AccountBalance>) {
		if (this.isAdded) {
			if (response.isSuccessful) {
				val balance = response.body() ?: return
				val sortedBalance = balance.balance.toSortedMap(Comparator { key1, key2 ->
					key1.compareTo(key2)
				})

				this.adapter.updateBalance(sortedBalance)

				if (this.layoutManager.findFirstVisibleItemPosition() == 0) {
					this.layoutManager.scrollToPositionWithOffset(0, 0)
				}
			} else {
				this.context?.let {
					response.errorBody()?.string()?.let { message ->
						Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()

		BitstampServices.privateApi.getAccountBalance().enqueue(this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.list.let {
			it.adapter = this.adapter
			it.layoutManager = this.layoutManager
			it.addItemDecoration(
				DividerItemDecoration(
					this.context,
					DividerItemDecoration.HORIZONTAL
				)
			)
			it.addItemDecoration(
				DividerItemDecoration(
					this.context,
					DividerItemDecoration.VERTICAL
				)
			)
		}
	}
}
