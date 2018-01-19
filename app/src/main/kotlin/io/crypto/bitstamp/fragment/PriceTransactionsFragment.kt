package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.PriceTransactionsAdapter
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_price_transactions.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class PriceTransactionsFragment : BaseFragment() {
	companion object {
		fun newInstance(tradingPair: TradingPair) = PriceTransactionsFragment().apply {
			this.arguments = Bundle().apply {
				this.putParcelable(TradingPair.EXTRA, tradingPair)
			}
		}
	}

	private val adapter by lazy { PriceTransactionsAdapter(this.tradingPair) }
	private val layoutManager by lazy { LinearLayoutManager(this.context) }
	private val tradingPair by lazy { this.arguments!!.getParcelable<TradingPair>(TradingPair.EXTRA) }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_price_transactions, container, false)
	}

	override fun onResume() {
		super.onResume()

		this.runPeriodically {
			listOf(launch {
				val transactions = BitstampServices.getTransactions(tradingPair.urlSymbol)

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
