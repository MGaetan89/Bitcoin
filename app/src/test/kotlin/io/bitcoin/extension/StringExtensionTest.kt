package io.bitcoin.extension

import io.bitcoin.model.Prices
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringExtensionTest {
	@Test
	fun toPrices() {
		assertThat("{}".toPrices()).isEqualTo(Prices(null, null))
		assertThat("{asks: [], bids: []}".toPrices()).isEqualTo(Prices(null, null))
		assertThat("{asks: [[1234.56, 0]], bids: [[7890.12, 1]]}".toPrices()).isEqualTo(Prices(1234.56, 7890.12))
		assertThat("{asks: [[1234.56, 0], [3456.78, 2]], bids: [[7890.12, 1], [9012.34, 3]]}".toPrices()).isEqualTo(Prices(1234.56, 7890.12))
	}
}
