package io.crypto.bitstamp.adapter

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.PricesActivity
import io.crypto.bitstamp.fragment.AccountBalanceFragment
import io.crypto.bitstamp.fragment.AccountOrdersFragment
import io.crypto.bitstamp.fragment.AccountTransactionsFragment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountPagerAdapterTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

	private lateinit var adapter: AccountPagerAdapter

	@Before
	fun before() {
		val fragmentManager = this.activityRule.activity.supportFragmentManager
		val resources = this.activityRule.activity.resources

		this.adapter = AccountPagerAdapter(resources, fragmentManager)
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(3)
	}

	@Test
	fun getItem_first() {
		val fragment = this.adapter.getItem(0)

		assertThat(fragment).isInstanceOf(AccountBalanceFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun getItem_second() {
		val fragment = this.adapter.getItem(1)

		assertThat(fragment).isInstanceOf(AccountOrdersFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun getItem_third() {
		val fragment = this.adapter.getItem(2)

		assertThat(fragment).isInstanceOf(AccountTransactionsFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test(expected = IllegalArgumentException::class)
	fun getItem_unknown() {
		this.adapter.getItem(3)
	}

	@Test
	fun getPageTitle_first() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo(this.activityRule.activity.getString(R.string.balance))
	}

	@Test
	fun getPageTitle_second() {
		assertThat(this.adapter.getPageTitle(1)).isEqualTo(this.activityRule.activity.getString(R.string.orders))
	}

	@Test
	fun getPageTitle_third() {
		assertThat(this.adapter.getPageTitle(2)).isEqualTo(this.activityRule.activity.getString(R.string.transactions))
	}

	@Test(expected = IllegalArgumentException::class)
	fun getPageTitle_unknown() {
		this.adapter.getPageTitle(3)
	}
}
