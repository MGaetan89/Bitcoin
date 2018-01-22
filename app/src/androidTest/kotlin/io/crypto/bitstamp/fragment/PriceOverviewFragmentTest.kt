package io.crypto.bitstamp.fragment

import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.model.TradingPair
import org.assertj.android.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PriceOverviewFragmentTest {
	@Test
	fun newInstance() {
		val tradingPair = TradingPair(8, 2, "Litecoin / U.S. dollar", "5.0 USD", "LTC/USD", TradingPair.Trading.Enabled, "ltcusd")
		val fragment = PriceOverviewFragment.newInstance(tradingPair)

		assertThat(fragment).isInstanceOf(PriceOverviewFragment::class.java)
		assertThat(fragment.arguments).hasKey(TradingPair.EXTRA)
		assertThat(fragment.arguments).hasSize(1)
		assertThat(fragment.arguments!!.getParcelable<TradingPair>(TradingPair.EXTRA)).isEqualTo(tradingPair)
	}
}
