package io.crypto.bitstamp.adapter

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import io.crypto.bitstamp.R
import io.crypto.bitstamp.fragment.AccountOrdersFragment
import io.crypto.bitstamp.fragment.AccountOverviewFragment
import io.crypto.bitstamp.fragment.AccountTransactionsFragment

class AccountPagerAdapter(private val resources: Resources, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
	override fun getCount() = 3

	override fun getItem(position: Int): Fragment {
		return when (position) {
			0 -> AccountOverviewFragment.newInstance()
			1 -> AccountOrdersFragment.newInstance()
			2 -> AccountTransactionsFragment.newInstance()
			else -> throw IllegalArgumentException("Invalid position '$position'")
		}
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return when (position) {
			0 -> this.resources.getString(R.string.overview)
			1 -> this.resources.getString(R.string.orders)
			2 -> this.resources.getString(R.string.transactions)
			else -> throw IllegalArgumentException("Invalid position '$position'")
		}
	}
}
