package io.crypto.bitstamp.extension

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import android.widget.TextView
import io.crypto.bitstamp.R
import org.assertj.android.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextViewExtensionTest {
	private lateinit var context: Context
	private lateinit var textView: TextView

	@Before
	fun before() {
		this.context = InstrumentationRegistry.getTargetContext()
		this.textView = TextView(this.context)
	}

	@Test
	fun setTextColorResource() {
		this.textView.setTextColorResource(R.color.accent)

		assertThat(this.textView).hasCurrentTextColor(
			ContextCompat.getColor(this.context, R.color.accent)
		)
	}
}
