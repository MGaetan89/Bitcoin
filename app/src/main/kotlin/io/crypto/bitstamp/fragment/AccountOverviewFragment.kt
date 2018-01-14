package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R

class AccountOverviewFragment : BaseFragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_account_overview, container, false)
	}

	companion object {
		fun newInstance() = AccountOverviewFragment()
	}
}
