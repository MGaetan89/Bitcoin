package io.crypto.bitstamp.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.activity_base.activity_stub
import kotlinx.android.synthetic.main.activity_base.bottom_navigation
import kotlinx.android.synthetic.main.activity_base.tabs
import kotlinx.android.synthetic.main.activity_base.toolbar

abstract class BaseActivity : AppCompatActivity(), ViewStub.OnInflateListener,
	BottomNavigationView.OnNavigationItemSelectedListener {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.setContentView(R.layout.activity_base)

		this.setSupportActionBar(this.toolbar)

		this.configureBottomNavigation()
		this.inflateContentView()

		this.title = this.getToolbarTitle()
	}

	@CallSuper
	override fun onInflate(stub: ViewStub, inflated: View) {
		this.configureTabs()
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.menu_account -> {
				if (BitstampServices.account == null) {
					this.startActivity<LoginActivity>(
						Intent.FLAG_ACTIVITY_NEW_TASK,
						Intent.FLAG_ACTIVITY_CLEAR_TASK
					)
				} else {
					this.startActivity<AccountActivity>(
						Intent.FLAG_ACTIVITY_NEW_TASK,
						Intent.FLAG_ACTIVITY_CLEAR_TASK
					)
				}
				true
			}
			R.id.menu_prices -> {
				this.startActivity<PricesActivity>(
					Intent.FLAG_ACTIVITY_NEW_TASK,
					Intent.FLAG_ACTIVITY_CLEAR_TASK
				)
				true
			}
			else -> false
		}
	}

	protected open fun configureTabs(tabs: TabLayout) = false

	@LayoutRes
	protected abstract fun getContentResourceId(): Int

	@IdRes
	protected abstract fun getSelectedTabId(): Int

	protected abstract fun getToolbarTitle(): CharSequence

	private fun configureBottomNavigation() {
		this.bottom_navigation.let {
			it.setOnNavigationItemSelectedListener(this)
			it.menu.findItem(this.getSelectedTabId())?.isChecked = true
		}
	}

	private fun configureTabs() {
		this.tabs.let {
			it.visibility = if (this.configureTabs(it)) View.VISIBLE else View.GONE
		}
	}

	private fun inflateContentView() {
		this.activity_stub.let {
			it.layoutResource = this.getContentResourceId()
			it.setOnInflateListener(this)
			it.inflate()
		}
	}
}
