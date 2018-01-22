package io.crypto.bitstamp.fragment

import android.support.test.runner.AndroidJUnit4
import org.assertj.android.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountTransactionsFragmentTest {
	@Test
	fun newInstance() {
		val fragment = AccountTransactionsFragment.newInstance()

		assertThat(fragment).isInstanceOf(AccountTransactionsFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}
}
