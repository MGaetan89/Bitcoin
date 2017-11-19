package io.bitcoin.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.bitcoin.BuildConfig
import io.bitcoin.Config
import io.bitcoin.R
import io.bitcoin.adapter.ConfigureExchangeAdapter
import io.bitcoin.extension.getExchanges
import io.bitcoin.extension.saveExchanges
import kotlinx.android.synthetic.main.fragment_configure_exchange.configure
import kotlinx.android.synthetic.main.fragment_configure_exchange.list

class ConfigureExchangeFragment : BottomSheetDialogFragment(), View.OnClickListener {
	private val adapter by lazy {
		val selectedExchanges = PreferenceManager.getDefaultSharedPreferences(this.context).getExchanges().toMutableList()
		ConfigureExchangeAdapter(BuildConfig.CURRENCY_PAIRS, selectedExchanges)
	}

	override fun onClick(view: View) {
		this.saveExchanges(this.adapter.selectedCurrencyPairs.toSet())
		this.notifyExchangesUpdated()
		this.dismiss()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_configure_exchange, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.configure.setOnClickListener(this)
		this.list.also {
			it.adapter = this.adapter
			it.layoutManager = LinearLayoutManager(this.context)
			it.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
		}
	}

	private fun notifyExchangesUpdated() {
		this.context?.let {
			LocalBroadcastManager.getInstance(it)
					.sendBroadcast(Intent(Config.ACTION_EXCHANGES_UPDATED))
		}
	}

	private fun saveExchanges(exchanges: Set<String>) {
		PreferenceManager.getDefaultSharedPreferences(this.context)
				.saveExchanges(exchanges)
	}

	companion object {
		fun newInstance() = ConfigureExchangeFragment()
	}
}
