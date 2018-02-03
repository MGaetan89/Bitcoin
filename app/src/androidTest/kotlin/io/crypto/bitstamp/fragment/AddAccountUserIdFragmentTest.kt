package io.crypto.bitstamp.fragment

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.PricesActivity
import io.crypto.bitstamp.espresso.hasTextInputLayoutError
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAccountUserIdFragmentTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

	@Test
	fun newInstance() {
		val fragment = AddAccountUserIdFragment.newInstance()

		assertThat(fragment).isInstanceOf(AddAccountUserIdFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun emptyUserId() {
		this.displayUserIdSection()

		onView(withId(R.id.user_id)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.user_id_layout))
			.check(matches(hasTextInputLayoutError(R.string.no_user_id)))
	}

	@Test
	fun noUserId() {
		this.displayUserIdSection()

		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.user_id_layout))
			.check(matches(hasTextInputLayoutError(R.string.no_user_id)))
	}

	@Test
	fun withUserId() {
		this.displayUserIdSection()

		onView(withId(R.id.user_id)).perform(typeText("user_id"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key)).check(matches(isDisplayed()))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.user_id)).check(matches(allOf(isDisplayed(), withText("user_id"))))
	}

	private fun displayUserIdSection() {
		onView(withId(R.id.menu_account)).perform(click())
		onView(withId(R.id.add_account)).perform(click())
		onView(withId(R.id.add_account_user_id)).check(matches(isDisplayed()))
	}
}
