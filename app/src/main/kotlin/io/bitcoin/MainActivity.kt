package io.bitcoin

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.bitcoin.fragment.ExchangeFragment
import io.bitcoin.fragment.OrdersFragment
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.setContentView(R.layout.activity_main)

		this.setSupportActionBar(this.toolbar)

		this.bottom_navigation.setOnNavigationItemSelectedListener(this)

		this.displayExchange()
	}

	override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
		R.id.menu_exchange -> {
			this.displayExchange()
			true
		}
		R.id.menu_orders -> {
			this.displayOrders()
			true
		}
		else -> false
	}

	private fun displayExchange() {
		this.supportFragmentManager.beginTransaction()
				.replace(R.id.content, ExchangeFragment.newInstance())
				.commit()
	}

	private fun displayOrders() {
		this.supportFragmentManager.beginTransaction()
				.replace(R.id.content, OrdersFragment.newInstance())
				.commit()
	}
}
