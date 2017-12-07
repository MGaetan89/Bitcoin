package io.bitcoin.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pusher.client.channel.SubscriptionEventListener
import io.bitcoin.R
import io.bitcoin.adapter.OrderAdapter
import io.bitcoin.extension.getOrders
import io.bitcoin.extension.removeOrder
import io.bitcoin.extension.toPrices
import io.bitcoin.extension.toTradingPair
import io.bitcoin.model.TradingPair
import io.bitcoin.network.BitstampApi
import io.bitcoin.network.BitstampApi.Channel
import io.bitcoin.network.BitstampApi.Event
import kotlinx.android.synthetic.main.fragment_orders.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class OrdersFragment : Fragment(), View.OnClickListener, SubscriptionEventListener {
	private val adapter by lazy { OrderAdapter() }
	private val floatingActionButton by lazy { this.activity?.findViewById<FloatingActionButton>(R.id.fab) }
	private val receiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			when (intent.action) {
				AddOrderFragment.ACTION_ORDER_ADDED -> {
					BitstampApi.unSubscribeFrom(Channel.order_book, adapter.getUrlSymbols())

					val orders = PreferenceManager.getDefaultSharedPreferences(context).getOrders()

					adapter.updateOrders(orders)

					BitstampApi.subscribeTo(Channel.order_book, Event.data, orders.map { it.tradingPair.toUrlSymbol() }, this@OrdersFragment)
				}
			}
		}
	}
	private val tradingPairs = mutableListOf<TradingPair>()

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		this.activity?.setTitle(R.string.orders)
		this.floatingActionButton?.let {
			it.setImageResource(R.drawable.ic_add)
			it.setOnClickListener(this)
		}
	}

	override fun onClick(view: View) {
		this.displayAddOrder()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_orders, container, false)

	override fun onEvent(channelName: String, eventName: String, data: String) {
		data.toPrices().bid?.let {
			val adapter = this.adapter
			val tradingPair = channelName.toTradingPair(Channel.order_book.name, this@OrdersFragment.tradingPairs)

			launch(UI) {
				if (tradingPair != null) {
					adapter.updatePrice(tradingPair, it)
				}
			}
		}
	}

	override fun onPause() {
		this.floatingActionButton?.visibility = View.GONE

		this.context?.let {
			LocalBroadcastManager.getInstance(it).unregisterReceiver(this.receiver)
		}

		BitstampApi.unSubscribeFrom(Channel.order_book, this.adapter.getUrlSymbols())

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		this.floatingActionButton?.visibility = View.VISIBLE

		val adapter = this.adapter
		val context = this.context
		val listener = this
		val tradingPairs = this.tradingPairs

		launch {
			BitstampApi.getTradingPairs()?.let {
				tradingPairs.clear()
				tradingPairs.addAll(it.sortedBy { it.description })

				val orders = PreferenceManager.getDefaultSharedPreferences(context).getOrders()

				launch(UI) {
					adapter.updateOrders(orders)

					BitstampApi.subscribeTo(Channel.order_book, Event.data, adapter.getUrlSymbols(), listener)
				}
			}
		}

		this.context?.let {
			LocalBroadcastManager.getInstance(it).registerReceiver(this.receiver, IntentFilter(AddOrderFragment.ACTION_ORDER_ADDED))
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.list.also {
			it.adapter = this.adapter
			it.itemAnimator = null
			it.layoutManager = LinearLayoutManager(this.context)
			it.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

			ItemTouchHelper(OrderDismissCallback()).attachToRecyclerView(it)
		}
	}

	private fun displayAddOrder() {
		AddOrderFragment.newInstance()
				.show(this.childFragmentManager, "add_order")
	}

	companion object {
		fun newInstance() = OrdersFragment()
	}

	private inner class OrderDismissCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END or ItemTouchHelper.START) {
		override fun isItemViewSwipeEnabled() = true

		override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
			val position = viewHolder.adapterPosition
			val preferences = PreferenceManager.getDefaultSharedPreferences(context)
			val orders = preferences.getOrders()
			val order = orders[position]

			preferences.removeOrder(order)
			adapter.removeOrder(order)
		}
	}
}
