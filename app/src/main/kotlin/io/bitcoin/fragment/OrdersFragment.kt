package io.bitcoin.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.pusher.client.Pusher
import com.pusher.client.channel.SubscriptionEventListener
import io.bitcoin.BuildConfig
import io.bitcoin.R
import io.bitcoin.adapter.OrderAdapter
import io.bitcoin.extension.getOrders
import io.bitcoin.extension.removeOrder
import io.bitcoin.extension.toCurrencyPair
import io.bitcoin.extension.toPrices
import kotlinx.android.synthetic.main.fragment_orders.list

class OrdersFragment : Fragment(), SubscriptionEventListener {
	private val adapter by lazy { OrderAdapter() }
	private val pusher by lazy { Pusher(BuildConfig.PUSHER_API_KEY) }
	private val receiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			when (intent.action) {
				AddOrderFragment.ACTION_ORDER_ADDED -> {
					unsubscribeFromChannels(adapter.getCurrencyPairs())

					val orders = PreferenceManager.getDefaultSharedPreferences(context).getOrders()

					adapter.updateOrders(orders)

					subscribeToChannels(orders.map { it.currencyPair.toTag() })
				}
			}
		}
	}

	init {
		this.setHasOptionsMenu(true)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		this.activity?.setTitle(R.string.orders)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.orders, menu)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_orders, container, false)

	override fun onEvent(channelName: String, eventName: String, data: String) {
		data.toPrices().bid?.let {
			this.activity?.runOnUiThread {
				val currencyPair = channelName.toCurrencyPair(ExchangeFragment.CHANNEL)

				this.adapter.updatePrice(currencyPair, it)
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.menu_add_order -> {
			this.displayAddOrder()
			true
		}
		else -> super.onOptionsItemSelected(item)
	}

	override fun onPause() {
		this.context?.let {
			LocalBroadcastManager.getInstance(it).unregisterReceiver(this.receiver)
		}

		this.unsubscribeFromChannels(this.adapter.getCurrencyPairs())

		this.pusher.disconnect()

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		this.context?.let {
			LocalBroadcastManager.getInstance(it).registerReceiver(this.receiver, IntentFilter(AddOrderFragment.ACTION_ORDER_ADDED))
		}

		val orders = PreferenceManager.getDefaultSharedPreferences(this.context).getOrders()

		this.adapter.updateOrders(orders)

		this.subscribeToChannels(this.adapter.getCurrencyPairs())

		this.pusher.connect()
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

	private fun subscribeToChannels(channels: List<String>) {
		channels.forEach {
			if (it.isEmpty()) {
				this.pusher.subscribe(ExchangeFragment.CHANNEL).bind(ExchangeFragment.EVENT, this)
			} else {
				this.pusher.subscribe("${ExchangeFragment.CHANNEL}_$it").bind(ExchangeFragment.EVENT, this)
			}
		}
	}

	private fun unsubscribeFromChannels(channels: List<String>) {
		channels.forEach {
			if (it.isEmpty()) {
				this.pusher.unsubscribe(ExchangeFragment.CHANNEL)
			} else {
				this.pusher.unsubscribe("${ExchangeFragment.CHANNEL}_$it")
			}
		}
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
