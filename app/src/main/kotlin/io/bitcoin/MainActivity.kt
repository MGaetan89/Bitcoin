package io.bitcoin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.bitcoin.fragment.AddOrderFragment
import io.bitcoin.fragment.ConfigureExchangeFragment

class MainActivity : AppCompatActivity() {
	fun addOrder(@Suppress("UNUSED_PARAMETER") view: View) {
		AddOrderFragment.newInstance()
				.show(this.supportFragmentManager, "add_order")
	}

	fun configureExchange(@Suppress("UNUSED_PARAMETER") view: View) {
		ConfigureExchangeFragment.newInstance()
				.show(this.supportFragmentManager, "configure_exchange")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.setContentView(R.layout.activity_main)
	}
}
