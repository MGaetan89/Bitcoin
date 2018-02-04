package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountTransactionsAdapter
import io.crypto.bitstamp.model.UserTransaction
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_account_transactions.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountTransactionsFragment : BaseFragment(), Callback<List<UserTransaction>> {
	companion object {
		fun newInstance() = AccountTransactionsFragment()
	}

	private val adapter by lazy { AccountTransactionsAdapter() }
	private val layoutManager by lazy { LinearLayoutManager(this.context) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		launch {
			val tradingPairs = BitstampServices.getTradingPairs()

			launch(UI) {
				adapter.updateTradingPairs(tradingPairs)
			}
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_account_transactions, container, false)
	}

	override fun onFailure(call: Call<List<UserTransaction>>, t: Throwable) {
		if (this.isAdded) {
			this.context?.let {
				Toast.makeText(it, t.localizedMessage, Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onResponse(
		call: Call<List<UserTransaction>>,
		response: Response<List<UserTransaction>>
	) {
		if (this.isAdded) {
			if (response.isSuccessful) {
				val transactions = response.body() ?: return

				this.adapter.updateTransactions(transactions)

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

		BitstampServices.privateApi.getUserTransactions().enqueue(this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.list.let {
			it.adapter = this.adapter
			it.layoutManager = this.layoutManager
			it.addItemDecoration(
				DividerItemDecoration(
					this.context,
					DividerItemDecoration.VERTICAL
				)
			)
		}
	}
}
