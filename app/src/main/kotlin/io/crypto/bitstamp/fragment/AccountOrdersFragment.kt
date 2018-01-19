package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountOrdersAdapter
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_account_orders.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AccountOrdersFragment : BaseFragment() {
	companion object {
		fun newInstance() = AccountOrdersFragment()
	}

	private val adapter by lazy { AccountOrdersAdapter() }
	private val layoutManager by lazy { LinearLayoutManager(this.context) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_account_orders, container, false)
	}

	override fun onResume() {
		super.onResume()

		launch {
			val tradingPairs = BitstampServices.getTradingPairs()

			launch(UI) {
				adapter.updateTradingPairs(tradingPairs)
			}
		}

		this.runPeriodically(10) {
			listOf(launch {
				val orders = BitstampServices.getOpenOrders()

				launch(UI) {
					adapter.updateOrders(orders)

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
			it.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
		}
	}
}
