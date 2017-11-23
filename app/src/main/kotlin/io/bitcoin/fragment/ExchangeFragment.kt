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
import com.pusher.client.Pusher
import com.pusher.client.channel.SubscriptionEventListener
import io.bitcoin.BuildConfig
import io.bitcoin.R
import io.bitcoin.adapter.ExchangeAdapter
import io.bitcoin.extension.getExchanges
import io.bitcoin.extension.toCurrencyPair
import io.bitcoin.extension.toPrices
import kotlinx.android.synthetic.main.fragment_exchange.list

class ExchangeFragment : Fragment(), SubscriptionEventListener {
	private val adapter by lazy { ExchangeAdapter() }
	private val pusher by lazy { Pusher(BuildConfig.PUSHER_API_KEY) }
	private val receiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			when (intent.action) {
				ConfigureExchangeFragment.ACTION_EXCHANGES_UPDATED -> {
					unsubscribeFromChannels(adapter.currencyPairs.map { it.toTag() })

					val channels = PreferenceManager.getDefaultSharedPreferences(context).getExchanges()

					adapter.updateCurrencyPairs(channels.map { it.toCurrencyPair(CHANNEL) })

					subscribeToChannels(channels)
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
		val currencyPair = channelName.toCurrencyPair(CHANNEL)
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

		this.unsubscribeFromChannels(this.adapter.currencyPairs.map { it.toTag() })

		this.pusher.disconnect()

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		val filter = IntentFilter(ConfigureExchangeFragment.ACTION_EXCHANGES_UPDATED)

		this.context?.let {
			LocalBroadcastManager.getInstance(it).registerReceiver(this.receiver, filter)
		}

		val channels = PreferenceManager.getDefaultSharedPreferences(this.context).getExchanges()

		this.adapter.updateCurrencyPairs(channels.map { it.toCurrencyPair(CHANNEL) })

		this.subscribeToChannels(channels)

		this.pusher.connect()
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

	private fun subscribeToChannels(channels: List<String>) {
		channels.forEach {
			if (it.isEmpty()) {
				this.pusher.subscribe(CHANNEL).bind(EVENT, this)
			} else {
				this.pusher.subscribe("${CHANNEL}_$it").bind(EVENT, this)
			}
		}
	}

	private fun unsubscribeFromChannels(channels: List<String>) {
		channels.forEach {
			if (it.isEmpty()) {
				this.pusher.unsubscribe(CHANNEL)
			} else {
				this.pusher.unsubscribe("${CHANNEL}_$it")
			}
		}
	}

	companion object {
		const val CHANNEL = "order_book"
		const val EVENT = "data"

		fun newInstance() = ExchangeFragment()
	}
}
