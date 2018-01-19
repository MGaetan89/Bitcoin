package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountTransactionsAdapter
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_account_transactions.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AccountTransactionsFragment : BaseFragment() {
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

	override fun onResume() {
		super.onResume()

		this.runPeriodically {
			listOf(launch {
				val transactions = BitstampServices.getUserTransactions()

				launch(UI) {
					adapter.updateTransactions(transactions)

					if (layoutManager.findFirstVisibleItemPosition() == 0) {
						layoutManager.scrollToPositionWithOffset(0, 0)
					}
				}
			})
		}
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
