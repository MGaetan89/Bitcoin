package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.adapter.AccountBalanceAdapter
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_account_balance.list
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AccountBalanceFragment : BaseFragment() {
	companion object {
		fun newInstance() = AccountBalanceFragment()
	}

	private val adapter by lazy { AccountBalanceAdapter() }
	private val layoutManager by lazy { GridLayoutManager(this.context, 2) }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_account_balance, container, false)
	}

	override fun onResume() {
		super.onResume()

		this.runPeriodically(10) {
			listOf(launch {
				val balance = BitstampServices.getAccountBalance()

				launch(UI) {
					val sortedBalance = balance.balance.toSortedMap(Comparator { key1, key2 ->
						key1.compareTo(key2)
					})

					adapter.updateBalance(sortedBalance)

					if (layoutManager.findFirstVisibleItemPosition() == 0) {
						layoutManager.scrollToPositionWithOffset(0, 0)
					}
				}
			})
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.list.let {
			it.adapter = this.adapter
			it.layoutManager = this.layoutManager
			it.addItemDecoration(
				DividerItemDecoration(
					this.context,
					DividerItemDecoration.HORIZONTAL
				)
			)
			it.addItemDecoration(
				DividerItemDecoration(
					this.context,
					DividerItemDecoration.VERTICAL
				)
			)
		}
	}
}
