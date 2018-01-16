package io.crypto.bitstamp.extension

import io.crypto.bitstamp.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Locale

class FloatExtensionTest {
	@Before
	fun before() {
		Locale.setDefault(Locale.US)
	}

	@Test
	fun toFormattedPercent() {
		assertThat((-12.347258f).toFormattedPercent()).isEqualTo("-1,234.73%")
		assertThat((-2.5f).toFormattedPercent()).isEqualTo("-250.00%")
		assertThat((-0.1f).toFormattedPercent()).isEqualTo("-10.00%")
		assertThat(0f.toFormattedPercent()).isEqualTo("0.00%")
		assertThat(0.1f.toFormattedPercent()).isEqualTo("10.00%")
		assertThat(2.5f.toFormattedPercent()).isEqualTo("250.00%")
		assertThat(12.347258f.toFormattedPercent()).isEqualTo("1,234.73%")
	}

	@Test
	fun toFormattedString() {
		// Negative precision
		assertThat((-1234.7258f).toFormattedString(-2)).isEqualTo("-1,235")
		assertThat((-10f).toFormattedString(-2)).isEqualTo("-10")
		assertThat((-2.5f).toFormattedString(-2)).isEqualTo("-2")
		assertThat(0f.toFormattedString(-2)).isEqualTo("0")
		assertThat(2.5f.toFormattedString(-2)).isEqualTo("2")
		assertThat(10f.toFormattedString(-2)).isEqualTo("10")
		assertThat(1234.7258f.toFormattedString(-2)).isEqualTo("1,235")

		// Zero precision
		assertThat((-1234.7258f).toFormattedString(0)).isEqualTo("-1,235")
		assertThat((-10f).toFormattedString(0)).isEqualTo("-10")
		assertThat((-2.5f).toFormattedString(0)).isEqualTo("-2")
		assertThat(0f.toFormattedString(0)).isEqualTo("0")
		assertThat(2.5f.toFormattedString(0)).isEqualTo("2")
		assertThat(10f.toFormattedString(0)).isEqualTo("10")
		assertThat(1234.7258f.toFormattedString(0)).isEqualTo("1,235")

		// Positive precision
		assertThat((-1234.7258f).toFormattedString(2)).isEqualTo("-1,234.73")
		assertThat((-10f).toFormattedString(2)).isEqualTo("-10.00")
		assertThat((-2.5f).toFormattedString(2)).isEqualTo("-2.50")
		assertThat(0f.toFormattedString(2)).isEqualTo("0.00")
		assertThat(2.5f.toFormattedString(2)).isEqualTo("2.50")
		assertThat(10f.toFormattedString(2)).isEqualTo("10.00")
		assertThat(1234.7258f.toFormattedString(2)).isEqualTo("1,234.73")
	}

	@Test
	fun toVariationColor() {
		assertThat((-10f).toVariationColor()).isEqualTo(R.color.ask)
		assertThat((-2.5f).toVariationColor()).isEqualTo(R.color.ask)
		assertThat(0f.toVariationColor()).isEqualTo(R.color.text)
		assertThat(2.5f.toVariationColor()).isEqualTo(R.color.bid)
		assertThat(10f.toVariationColor()).isEqualTo(R.color.bid)
	}
}
