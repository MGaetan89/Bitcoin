package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountOrdersAdapter
import io.crypto.bitstamp.model.CanceledOrder
import io.crypto.bitstamp.model.OpenOrder
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_account_orders.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountOrdersFragment : BaseFragment(), AccountOrdersAdapter.OnCancelOrderListener {
	companion object {
		fun newInstance() = AccountOrdersFragment()
	}

	private val adapter by lazy { AccountOrdersAdapter(this) }
	private val layoutManager by lazy { LinearLayoutManager(this.context) }

	override fun onCancelOrder(position: Int, order: OpenOrder) {
		BitstampServices.privateApi.cancelOrder(order.id).enqueue(object : Callback<CanceledOrder> {
			override fun onFailure(call: Call<CanceledOrder>, t: Throwable) {
				if (isAdded) {
					context?.let {
						Toast.makeText(it, t.localizedMessage, Toast.LENGTH_SHORT).show()
					}
				}
			}

			override fun onResponse(call: Call<CanceledOrder>, response: Response<CanceledOrder>) {
				if (isAdded) {
					if (response.isSuccessful) {
						if (response.body()?.id == order.id) {
							adapter.updateOrderStatus(order.id, "Canceled")
						}
					} else {
						context?.let {
							response.errorBody()?.string()?.let { message ->
								Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
							}
						}
					}
				}
			}
		})
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
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
				val orderIds = orders.map { it.id }

				orderIds.forEach {
					val status = BitstampServices.getOrderStatus(it).status.orEmpty()

					launch(UI) {
						adapter.updateOrderStatus(it, status)
					}
				}

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
			it.addItemDecoration(
				DividerItemDecoration(
					this.context,
					DividerItemDecoration.VERTICAL
				)
			)
		}
	}
}
