package io.crypto.bitstamp.fragment

import android.support.test.runner.AndroidJUnit4
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountBalanceFragmentTest {
	@Test
	fun newInstance() {
		val fragment = AccountBalanceFragment.newInstance()

		Assertions.assertThat(fragment).isInstanceOf(AccountBalanceFragment::class.java)
		org.assertj.android.api.Assertions.assertThat(fragment.arguments).isNull()
	}
}
