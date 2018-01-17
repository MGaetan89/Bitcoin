package io.crypto.bitstamp.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class StringExtensionTest {
	@Before
	fun before() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}

	@Test
	fun toTimestamp() {
		assertThat("".toTimestamp()).isNull()
		assertThat("2018-01-16".toTimestamp()).isNull()
		assertThat("23:02:44".toTimestamp()).isNull()
		assertThat("2018-01-16 23:02:44".toTimestamp()).isEqualTo(1516143764L)
	}
}
