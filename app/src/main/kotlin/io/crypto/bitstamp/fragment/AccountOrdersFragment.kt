package io.crypto.bitstamp.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
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
import io.crypto.bitstamp.model.OpenOrderStatus
import io.crypto.bitstamp.network.BitstampRepository
import io.crypto.bitstamp.network.BitstampServices
import io.crypto.bitstamp.viewModel.TradingPairsViewModel
import kotlinx.android.synthetic.main.fragment_account_orders.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountOrdersFragment
	: Fragment(), Callback<List<OpenOrder>>, AccountOrdersAdapter.OnCancelOrderListener {
	companion object {
		fun newInstance() = AccountOrdersFragment()
	}

	private val adapter by lazy { AccountOrdersAdapter(this) }
	private val layoutManager by lazy { LinearLayoutManager(this.context) }

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		ViewModelProviders.of(this)
			.get(TradingPairsViewModel::class.java)
			.setRepository(BitstampRepository())
			.getTradingPairs()
			.observe(this, Observer {
				it?.let(this.adapter::updateTradingPairs)
			})
	}

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

	override fun onFailure(call: Call<List<OpenOrder>>, t: Throwable) {
		if (this.isAdded) {
			this.context?.let {
				Toast.makeText(it, t.localizedMessage, Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onResponse(call: Call<List<OpenOrder>>, response: Response<List<OpenOrder>>) {
		if (this.isAdded) {
			if (response.isSuccessful) {
				val orders = response.body() ?: return

				this.requestOrdersStatus(orders.map { it.id })

				this.adapter.updateOrders(orders)

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

		BitstampServices.privateApi.getOpenOrders().enqueue(this)
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

	private fun requestOrdersStatus(ordersId: List<Long>) {
		ordersId.forEach {
			BitstampServices.privateApi.getOrderStatus(it)
				.enqueue(object : Callback<OpenOrderStatus> {
					override fun onFailure(call: Call<OpenOrderStatus>, t: Throwable) = Unit

					override fun onResponse(
						call: Call<OpenOrderStatus>,
						response: Response<OpenOrderStatus>
					) {
						if (isAdded && response.isSuccessful) {
							val status = response.body()?.status.orEmpty()

							adapter.updateOrderStatus(it, status)
						}
					}
				})
		}
	}
}
