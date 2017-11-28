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
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.pusher.client.channel.SubscriptionEventListener
import io.bitcoin.R
import io.bitcoin.adapter.ExchangeAdapter
import io.bitcoin.extension.getExchanges
import io.bitcoin.extension.toCurrencyPair
import io.bitcoin.extension.toPrices
import io.bitcoin.network.BitstampApi
import io.bitcoin.network.BitstampApi.Channel
import io.bitcoin.network.BitstampApi.Event
import kotlinx.android.synthetic.main.fragment_exchange.list

class ExchangeFragment : Fragment(), SubscriptionEventListener {
	private val adapter by lazy { ExchangeAdapter() }
	private val receiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			when (intent.action) {
				ConfigureExchangeFragment.ACTION_EXCHANGES_UPDATED -> {
					BitstampApi.unSubscribeFrom(Channel.order_book, adapter.currencyPairs.map { it.toTag() })

					val channels = PreferenceManager.getDefaultSharedPreferences(context).getExchanges()

					adapter.updateCurrencyPairs(channels.map { it.toCurrencyPair(Channel.order_book.name) })

					BitstampApi.subscribeTo(Channel.order_book, Event.data, channels, this@ExchangeFragment)
				}
			}
		}
	}

	init {
		this.setHasOptionsMenu(true)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		this.activity?.setTitle(R.string.exchange)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.exchange, menu)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_exchange, container, false)

	override fun onEvent(channelName: String, eventName: String, data: String) {
		val currencyPair = channelName.toCurrencyPair(Channel.order_book.name)
		val prices = data.toPrices()

		this.activity?.runOnUiThread {
			this.adapter.updatePrice(currencyPair, prices)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.menu_configure_exchange -> {
			this.displayConfigureExchange()
			true
		}
		else -> super.onOptionsItemSelected(item)
	}

	override fun onPause() {
		this.context?.let {
			LocalBroadcastManager.getInstance(it).unregisterReceiver(this.receiver)
		}

		BitstampApi.unSubscribeFrom(Channel.order_book, this.adapter.currencyPairs.map { it.toTag() })

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		val filter = IntentFilter(ConfigureExchangeFragment.ACTION_EXCHANGES_UPDATED)

		this.context?.let {
			LocalBroadcastManager.getInstance(it).registerReceiver(this.receiver, filter)
		}

		val channels = PreferenceManager.getDefaultSharedPreferences(this.context).getExchanges()

		this.adapter.updateCurrencyPairs(channels.map { it.toCurrencyPair(Channel.order_book.name) })

		BitstampApi.subscribeTo(Channel.order_book, Event.data, channels, this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.list.also {
			it.adapter = this.adapter
			it.itemAnimator = null
			it.layoutManager = LinearLayoutManager(this.context)
			it.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
		}
	}

	private fun displayConfigureExchange() {
		ConfigureExchangeFragment.newInstance()
				.show(this.childFragmentManager, "configure_exchange")
	}

	companion object {
		fun newInstance() = ExchangeFragment()
	}
}
