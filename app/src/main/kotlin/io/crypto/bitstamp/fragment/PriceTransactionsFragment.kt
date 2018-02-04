package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.PriceTransactionsAdapter
import io.crypto.bitstamp.model.PriceTransaction
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_price_transactions.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriceTransactionsFragment : BaseFragment(), Callback<List<PriceTransaction>> {
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

	override fun onFailure(call: Call<List<PriceTransaction>>, t: Throwable) {
		if (this.isAdded) {
			this.context?.let {
				Toast.makeText(it, t.localizedMessage, Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onResponse(
		call: Call<List<PriceTransaction>>,
		response: Response<List<PriceTransaction>>
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

		BitstampServices.api.getTransactions(this.tradingPair.urlSymbol).enqueue(this)
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
