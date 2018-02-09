package io.crypto.bitstamp.fragment

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.BuildConfig.TEST_ACCOUNT_API_KEY
import io.crypto.bitstamp.BuildConfig.TEST_ACCOUNT_SECRET
import io.crypto.bitstamp.BuildConfig.TEST_ACCOUNT_USER_ID
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.PricesActivity
import io.crypto.bitstamp.espresso.hasTextInputLayoutError
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAccountSaveAccountFragmentTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

	@Test
	fun back() {
		this.displaySaveAccountSection()

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
	fun longPinCode() {
		this.displaySaveAccountSection()

		onView(withId(R.id.pin_code)).perform(typeText("123456789"))
		onView(withId(R.id.save)).perform(click())
		onView(withId(R.id.pin_code_layout)).check(matches(hasTextInputLayoutError(R.string.no_pin_code)))
	}

	@Test
	fun newInstance() {
		val fragment = AddAccountSaveAccountFragment.newInstance()

		assertThat(fragment).isInstanceOf(AddAccountSaveAccountFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun shortPinCode() {
		this.displaySaveAccountSection()

		onView(withId(R.id.pin_code)).perform(typeText("123"))
		onView(withId(R.id.save)).perform(click())
		onView(withId(R.id.pin_code_layout)).check(matches(hasTextInputLayoutError(R.string.no_pin_code)))
	}

	@Test
	fun validPinCode() {
		this.displaySaveAccountSection()

		onView(withId(R.id.pin_code)).perform(typeText("123456"))
		onView(withId(R.id.save)).perform(click())

		// TODO
	}

	private fun displaySaveAccountSection() {
		onView(withId(R.id.menu_account)).perform(click())
		onView(withId(R.id.add_account)).perform(click())
		onView(withId(R.id.user_id)).perform(replaceText(TEST_ACCOUNT_USER_ID), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key)).perform(replaceText(TEST_ACCOUNT_API_KEY), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(replaceText(TEST_ACCOUNT_SECRET), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.add_account_check_information)).check(matches(isDisplayed()))
	}
}
