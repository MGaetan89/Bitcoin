package io.crypto.bitstamp.adapter

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.PricesActivity
import io.crypto.bitstamp.fragment.PriceOrderBookFragment
import io.crypto.bitstamp.fragment.PriceOverviewFragment
import io.crypto.bitstamp.fragment.PriceTransactionsFragment
import io.crypto.bitstamp.model.TradingPair
import org.assertj.android.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PricePagerAdapterTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

	private lateinit var adapter: PricePagerAdapter
	private lateinit var tradingPair: TradingPair

	@Before
	fun before() {
		val fragmentManager = this.activityRule.activity.supportFragmentManager
		val resources = this.activityRule.activity.resources

		this.tradingPair = TradingPair(
			8, 2, "Litecoin / U.S. dollar", "5.0 USD",
			"LTC/USD", TradingPair.Trading.Enabled, "ltcusd"
		)

		this.adapter = PricePagerAdapter(this.tradingPair, resources, fragmentManager)
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(3)
	}

	@Test
	fun getItem_first() {
		val fragment = this.adapter.getItem(0)

		assertThat(fragment).isInstanceOf(PriceOverviewFragment::class.java)
		assertThat(fragment.arguments).hasSize(1)
		assertThat(fragment.arguments).hasKey(TradingPair.EXTRA)
		assertThat(fragment.arguments?.getParcelable<TradingPair>(TradingPair.EXTRA)).isEqualTo(this.tradingPair)
	}

	@Test
	fun getItem_second() {
		val fragment = this.adapter.getItem(1)

		assertThat(fragment).isInstanceOf(PriceOrderBookFragment::class.java)
		assertThat(fragment.arguments).hasSize(1)
		assertThat(fragment.arguments).hasKey(TradingPair.EXTRA)
		assertThat(fragment.arguments?.getParcelable<TradingPair>(TradingPair.EXTRA)).isEqualTo(this.tradingPair)
	}

	@Test
	fun getItem_third() {
		val fragment = this.adapter.getItem(2)

		assertThat(fragment).isInstanceOf(PriceTransactionsFragment::class.java)
		assertThat(fragment.arguments).hasSize(1)
		assertThat(fragment.arguments).hasKey(TradingPair.EXTRA)
		assertThat(fragment.arguments?.getParcelable<TradingPair>(TradingPair.EXTRA)).isEqualTo(this.tradingPair)
	}

	@Test(expected = IllegalArgumentException::class)
	fun getItem_unknown() {
		this.adapter.getItem(3)
	}

	@Test
	fun getPageTitle_first() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo(this.activityRule.activity.getString(R.string.overview))
	}

	@Test
	fun getPageTitle_second() {
		assertThat(this.adapter.getPageTitle(1)).isEqualTo(this.activityRule.activity.getString(R.string.order_book))
	}

	@Test
	fun getPageTitle_third() {
		assertThat(this.adapter.getPageTitle(2)).isEqualTo(this.activityRule.activity.getString(R.string.transactions))
	}

	@Test(expected = IllegalArgumentException::class)
	fun getPageTitle_unknown() {
		this.adapter.getPageTitle(3)
	}
}
