package io.crypto.bitstamp.activity

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountPagerAdapter
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.network.BitstampServices

class AccountActivity : BaseActivity() {
	private var viewPager: ViewPager? = null

	override fun configureTabs(tabs: TabLayout): Boolean {
		this.viewPager?.let {
			tabs.setupWithViewPager(it)
		}

		return true
	}

	override fun getContentResourceId() = R.layout.activity_account

	override fun getSelectedTabId() = R.id.menu_account

	override fun getToolbarTitle(): CharSequence = this.getString(R.string.account)

	override fun onInflate(stub: ViewStub, inflated: View) {
		this.viewPager = inflated as ViewPager
		this.viewPager?.let {
			it.adapter = AccountPagerAdapter(this.resources, this.supportFragmentManager)
		}

		super.onInflate(stub, inflated)
	}

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
