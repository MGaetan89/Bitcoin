package io.crypto.bitstamp.fragment

import android.support.test.runner.AndroidJUnit4
import org.assertj.android.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountOrdersFragmentTest {
	@Test
	fun newInstance() {
		val fragment = AccountOrdersFragment.newInstance()

		assertThat(fragment).isInstanceOf(AccountOrdersFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}
}
