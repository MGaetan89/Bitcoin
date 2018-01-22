package io.crypto.bitstamp.extension

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.widget.FrameLayout
import android.widget.TextView
import io.crypto.bitstamp.R
import org.assertj.android.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewGroupExtensionTest {
	private lateinit var viewGroup: FrameLayout

	@Before
	fun before() {
		this.viewGroup = FrameLayout(InstrumentationRegistry.getTargetContext())

		assertThat(this.viewGroup).hasChildCount(0)
	}

	@Test
	fun inflate() {
		val view = this.viewGroup.inflate(R.layout.adapter_account_balance)

		assertThat(this.viewGroup).hasChildCount(0)
		assertThat(view.findViewById<TextView>(R.id.amount)).isNotNull
		assertThat(view.findViewById<TextView>(R.id.currency)).isNotNull
		assertThat(view.findViewById<TextView>(R.id.last)).isNull()
	}
}
