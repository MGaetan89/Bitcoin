package io.crypto.bitstamp.extension

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.widget.EditText
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTextExtensionTest {
	private lateinit var context: Context
	private lateinit var editText: EditText

	@Before
	fun before() {
		this.context = InstrumentationRegistry.getTargetContext()
		this.editText = EditText(this.context)
	}

	@Test
	fun getCleanText() {
		this.editText.setText("Some value")

		assertThat(this.editText.getCleanText()).isEqualTo("Some value")
	}

	@Test
	fun getCleanText_default() {
		assertThat(this.editText.getCleanText()).isNull()
	}

	@Test
	fun getCleanText_empty() {
		this.editText.setText("")

		assertThat(this.editText.getCleanText()).isNull()
	}

	@Test
	fun getCleanText_spaces() {
		this.editText.setText("   Some value  ")

		assertThat(this.editText.getCleanText()).isEqualTo("Some value")
	}

	@Test
	fun getCleanText_spacesOnly() {
		this.editText.setText("     ")

		assertThat(this.editText.getCleanText()).isNull()
	}
}
