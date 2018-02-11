package io.crypto.bitstamp.activity

import android.content.Intent
import android.support.v4.view.PagerAdapter
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountPagerAdapter
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.network.BitstampServices

class AccountActivity : TabbedActivity() {
	override fun getAdapter(): PagerAdapter {
		return AccountPagerAdapter(this.resources, this.supportFragmentManager)
	}

	override fun getSelectedTabId() = R.id.menu_account

	override fun getToolbarTitle(): CharSequence = this.getString(R.string.account)

	override fun onResume() {
		super.onResume()

		if (BitstampServices.account == null) {
			this.startActivity<LoginActivity>(
				Intent.FLAG_ACTIVITY_NEW_TASK,
				Intent.FLAG_ACTIVITY_CLEAR_TASK
			)
		}
	}
}
