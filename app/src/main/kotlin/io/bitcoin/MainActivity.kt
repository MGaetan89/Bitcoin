package io.bitcoin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.bitcoin.fragment.AddOrderFragment
import io.bitcoin.fragment.ConfigureExchangeFragment
import kotlinx.android.synthetic.main.activity_main.add_order
import kotlinx.android.synthetic.main.activity_main.configure_exchange

class MainActivity : AppCompatActivity(), View.OnClickListener {
	override fun onClick(view: View) {
		when (view.id) {
			R.id.add_order -> this.addOrder()
			R.id.configure_exchange -> this.configureExchange()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.setContentView(R.layout.activity_main)

		this.add_order.setOnClickListener(this)
		this.configure_exchange.setOnClickListener(this)
	}

	private fun configureExchange() {
		ConfigureExchangeFragment.newInstance()
				.show(this.supportFragmentManager, "configure_exchange")
	}

	private fun addOrder() {
		AddOrderFragment.newInstance()
				.show(this.supportFragmentManager, "add_order")
	}
}
