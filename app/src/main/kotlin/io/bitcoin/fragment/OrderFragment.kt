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
import android.view.View
import android.view.ViewGroup
import io.bitcoin.Config
import io.bitcoin.R
import io.bitcoin.adapter.OrderAdapter
import io.bitcoin.extension.getOrders
import io.bitcoin.extension.removeOrder
import io.bitcoin.model.CurrencyPair
import kotlinx.android.synthetic.main.fragment_orders.list

class OrderFragment : Fragment() {
	private val adapter by lazy { OrderAdapter() }
	private val receiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			when (intent.action) {
				Config.ACTION_ORDER_ADDED -> {
					val orders = PreferenceManager.getDefaultSharedPreferences(context).getOrders()

					adapter.updateOrders(orders)
				}
				Config.ACTION_PRICE_UPDATE -> {
					val currencyPair = intent.getParcelableExtra<CurrencyPair>(Config.EXTRA_CURRENCY_PAIR)
					val price = intent.getDoubleExtra(Config.EXTRA_PRICE, 0.0)

					adapter.updatePrice(currencyPair, price)
				}
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_orders, container, false)

	override fun onPause() {
		this.context?.let {
			LocalBroadcastManager.getInstance(it).unregisterReceiver(this.receiver)
		}

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		val filter = IntentFilter()
		filter.addAction(Config.ACTION_ORDER_ADDED)
		filter.addAction(Config.ACTION_PRICE_UPDATE)

		this.context?.let {
			LocalBroadcastManager.getInstance(it).registerReceiver(this.receiver, filter)
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

		val orders = PreferenceManager.getDefaultSharedPreferences(this.context).getOrders()

		this.adapter.updateOrders(orders)
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
