package io.crypto.bitstamp.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class LongExtensionTest {
	@Before
	fun before() {
		Locale.setDefault(Locale.US)
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}

	@Test
	fun toFormattedDate() {
		assertThat(0L.toFormattedDate()).isEqualTo("Jan 1, 1970")
		assertThat(1516140164L.toFormattedDate()).isEqualTo("Jan 16, 2018")
		assertThat(5532594234L.toFormattedDate()).isEqualTo("Apr 27, 2145")
	}

	@Test
	fun toFormattedDateTime() {
		assertThat(0L.toFormattedDateTime()).isEqualTo("Jan 1, 1970 12:00:00 AM")
		assertThat(1516140164L.toFormattedDateTime()).isEqualTo("Jan 16, 2018 10:02:44 PM")
		assertThat(5532594234L.toFormattedDateTime()).isEqualTo("Apr 27, 2145 3:43:54 PM")
	}

	@Test
	fun toFormattedTime() {
		assertThat(0L.toFormattedTime()).isEqualTo("12:00:00 AM")
		assertThat(1516140164L.toFormattedTime()).isEqualTo("10:02:44 PM")
		assertThat(5532594234L.toFormattedTime()).isEqualTo("3:43:54 PM")
	}
}
