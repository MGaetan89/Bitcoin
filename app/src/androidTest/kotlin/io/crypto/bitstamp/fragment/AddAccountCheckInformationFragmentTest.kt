package io.crypto.bitstamp.fragment

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.PricesActivity
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAccountCheckInformationFragmentTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

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
	fun wrongInformation() {
		this.displayWrongCheckInformationSection()

		InstrumentationRegistry.getInstrumentation().waitForIdleSync()

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

	private fun displayWrongCheckInformationSection() {
		onView(withId(R.id.menu_account)).perform(click())
		onView(withId(R.id.add_account)).perform(click())
		onView(withId(R.id.user_id)).perform(typeText("user_id"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key)).perform(typeText("api_key"), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(typeText("secret"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.add_account_check_information)).check(matches(isDisplayed()))
	}
}
