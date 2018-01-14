package io.crypto.bitstamp.activity

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.PricePagerAdapter
import io.crypto.bitstamp.model.TradingPair

class PriceActivity : BaseActivity() {
	private val tradingPair by lazy { this.intent.getParcelableExtra<TradingPair>(TradingPair.EXTRA) }

	private var viewPager: ViewPager? = null

	override fun configureTabs(tabs: TabLayout): Boolean {
		this.viewPager?.let {
			tabs.setupWithViewPager(it)
		}

		return true
	}

	override fun getContentResourceId() = R.layout.activity_price

	override fun getSelectedTabId() = R.id.menu_prices

	override fun getToolbarTitle() = this.tradingPair.description

	override fun onInflate(stub: ViewStub, inflated: View) {
		this.viewPager = inflated as ViewPager
		this.viewPager?.let {
			it.adapter = PricePagerAdapter(this.tradingPair, this.resources, this.supportFragmentManager)
		}

		super.onInflate(stub, inflated)
	}
}
