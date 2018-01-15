package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R

class AccountOrdersFragment : BaseFragment() {
	companion object {
		fun newInstance() = AccountOrdersFragment()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_account_orders, container, false)
	}
}
