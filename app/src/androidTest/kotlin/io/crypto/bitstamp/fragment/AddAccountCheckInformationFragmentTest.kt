package io.crypto.bitstamp.fragment

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.BuildConfig.TEST_ACCOUNT_API_KEY
import io.crypto.bitstamp.BuildConfig.TEST_ACCOUNT_SECRET
import io.crypto.bitstamp.BuildConfig.TEST_ACCOUNT_USER_ID
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.PricesActivity
import io.crypto.bitstamp.espresso.OkHttpIdlingResource
import io.crypto.bitstamp.network.BitstampServices
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAccountCheckInformationFragmentTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

	private lateinit var resource: OkHttpIdlingResource

	@Before
	fun before() {
		this.resource =
				OkHttpIdlingResource("OkHttp", BitstampServices.privateOkHttpClient.dispatcher())

		IdlingRegistry.getInstance().register(this.resource)
	}

	@Test
	fun back() {
		this.displayWrongCheckInformationSection()

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.api_key)).check(matches(allOf(isDisplayed(), withText("api_key"))))
		onView(withId(R.id.secret)).check(matches(allOf(isDisplayed(), withText("secret"))))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.user_id)).check(matches(allOf(isDisplayed(), withText("user_id"))))
	}

	@Test
	fun newInstance() {
		val fragment = AddAccountCheckInformationFragment.newInstance()

		assertThat(fragment).isInstanceOf(AddAccountCheckInformationFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun correctInformation_loginNow() {
		this.checkCorrectInformation()

		onView(withId(R.id.login_now)).perform(click())
		onView(withText(R.string.balance)).check(matches(isDisplayed()))

		BitstampServices.account = null
	}

	@Test
	fun correctInformation_saveAccount() {
		this.checkCorrectInformation()

		onView(withId(R.id.save_account)).perform(click())
		onView(withId(R.id.add_account_save_account)).check(matches(isDisplayed()))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.add_account_check_information)).check(matches(isDisplayed()))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.api_key))
			.check(matches(allOf(isDisplayed(), withText(TEST_ACCOUNT_API_KEY))))
		onView(withId(R.id.secret))
			.check(matches(allOf(isDisplayed(), withText(TEST_ACCOUNT_SECRET))))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.user_id))
			.check(matches(allOf(isDisplayed(), withText(TEST_ACCOUNT_USER_ID))))
	}

	@Test
	fun wrongInformation() {
		this.displayWrongCheckInformationSection()

		onView(withId(R.id.login_now)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
		onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
		onView(withId(R.id.save_account)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
		onView(withId(R.id.status)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
		onView(withId(R.id.status_text)).check(
			matches(
				allOf(
					withText("API key not found"),
					withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
				)
			)
		)
	}

	@After
	fun after() {
		IdlingRegistry.getInstance().unregister(this.resource)
	}

	private fun checkCorrectInformation() {
		this.displayCorrectCheckInformationSection()

		onView(withId(R.id.login_now)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
		onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
		onView(withId(R.id.save_account)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
		onView(withId(R.id.status)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
		onView(withId(R.id.status_text)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
	}

	private fun displayCorrectCheckInformationSection() {
		onView(withId(R.id.menu_account)).perform(click())
		onView(withId(R.id.add_account)).perform(click())
		onView(withId(R.id.user_id)).perform(replaceText(TEST_ACCOUNT_USER_ID), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key)).perform(replaceText(TEST_ACCOUNT_API_KEY), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(replaceText(TEST_ACCOUNT_SECRET), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.add_account_check_information)).check(matches(isDisplayed()))
	}

	private fun displayWrongCheckInformationSection() {
		onView(withId(R.id.menu_account)).perform(click())
		onView(withId(R.id.add_account)).perform(click())
		onView(withId(R.id.user_id)).perform(replaceText("user_id"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key)).perform(replaceText("api_key"), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(replaceText("secret"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.add_account_check_information)).check(matches(isDisplayed()))
	}
}
