package io.crypto.bitstamp.activity

import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R

abstract class TabbedActivity : BaseActivity() {
	private var viewPager: ViewPager? = null

	override fun configureTabs(tabs: TabLayout): Boolean {
		this.viewPager?.let(tabs::setupWithViewPager)

		return true
	}

	abstract fun getAdapter(): PagerAdapter

	override fun getContentResourceId() = R.layout.activity_tabbed

	override fun onInflate(stub: ViewStub, inflated: View) {
		this.viewPager = (inflated as ViewPager).also {
			it.adapter = this.getAdapter()
		}

		super.onInflate(stub, inflated)
	}
}
