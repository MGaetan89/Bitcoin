package io.crypto.bitstamp.adapter

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import io.crypto.bitstamp.R
import io.crypto.bitstamp.fragment.PriceOrderBookFragment
import io.crypto.bitstamp.fragment.PriceOverviewFragment
import io.crypto.bitstamp.fragment.PriceTransactionsFragment
import io.crypto.bitstamp.model.TradingPair

class PricePagerAdapter(private val tradingPair: TradingPair, private val resources: Resources, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
	override fun getCount() = 3

	override fun getItem(position: Int): Fragment {
		return when (position) {
			0 -> PriceOverviewFragment.newInstance(this.tradingPair)
			1 -> PriceOrderBookFragment.newInstance(this.tradingPair)
			2 -> PriceTransactionsFragment.newInstance(this.tradingPair)
			else -> throw IllegalArgumentException("Invalid position '$position'")
		}
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return when (position) {
			0 -> this.resources.getString(R.string.overview)
			1 -> this.resources.getString(R.string.order_book)
			2 -> this.resources.getString(R.string.transactions)
			else -> throw IllegalArgumentException("Invalid position '$position'")
		}
	}
}
