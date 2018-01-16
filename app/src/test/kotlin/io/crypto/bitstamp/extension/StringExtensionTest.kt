package io.crypto.bitstamp.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringExtensionTest {
	@Test
	fun toTimestamp() {
		assertThat("".toTimestamp()).isNull()
		assertThat("2018-01-16".toTimestamp()).isNull()
		assertThat("23:02:44".toTimestamp()).isNull()
		assertThat("2018-01-16 23:02:44".toTimestamp()).isEqualTo(1516140164L)
	}
}
