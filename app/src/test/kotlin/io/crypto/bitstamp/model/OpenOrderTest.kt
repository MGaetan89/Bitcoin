package io.crypto.bitstamp.model

import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class OpenOrderTest {
	@Before
	fun before() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}

	@Test
	fun type_buy() {
		val type = OpenOrder.Type.BUY

		assertThat(type.colorRes).isEqualTo(R.color.bid)
		assertThat(type.textRes).isEqualTo(R.string.buy)
	}

	@Test
	fun type_sell() {
		val type = OpenOrder.Type.SELL

		assertThat(type.colorRes).isEqualTo(R.color.ask)
		assertThat(type.textRes).isEqualTo(R.string.sell)
	}

	@Test
	fun typeObject_buy() {
		val order = OpenOrder(210f, "XRP/EUR", "2017-12-31 08:37:12", 596422824L, 0.45f, 0)

		assertThat(order.date).isEqualTo(1514709432L)
		assertThat(order.typeObject).isEqualTo(OpenOrder.Type.BUY)
		assertThat(order.urlSymbol).isEqualTo("xrpeur")
		assertThat(order.value).isEqualTo(94.5f)
	}

	@Test
	fun typeObject_sell() {
		val order = OpenOrder(210f, "XRP/EUR", "2018-01-19 20:01:46", 796729884L, 2f, 1)

		assertThat(order.date).isEqualTo(1516392106L)
		assertThat(order.typeObject).isEqualTo(OpenOrder.Type.SELL)
		assertThat(order.urlSymbol).isEqualTo("xrpeur")
		assertThat(order.value).isEqualTo(420f)
	}
}
