package io.crypto.bitstamp.activity

import android.support.v4.view.PagerAdapter
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.PricePagerAdapter
import io.crypto.bitstamp.model.TradingPair

class PriceActivity : TabbedActivity() {
	private val tradingPair by lazy { this.intent.getParcelableExtra<TradingPair>(TradingPair.EXTRA) }

	override fun getAdapter(): PagerAdapter {
		return PricePagerAdapter(this.tradingPair, this.resources, this.supportFragmentManager)
	}

	override fun getSelectedTabId() = R.id.menu_prices

	override fun getToolbarTitle() = this.tradingPair.description
}
