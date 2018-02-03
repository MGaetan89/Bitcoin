package io.crypto.bitstamp.fragment

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions
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
class AddAccountApiKeyFragmentTest {
	@JvmField
	@Rule
	var activityRule = ActivityTestRule(PricesActivity::class.java)

	@Test
	fun back() {
		this.displayApiKeySection()

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.user_id)).check(matches(allOf(isDisplayed(), withText("user_id"))))
	}

	@Test
	fun newInstance() {
		val fragment = AddAccountApiKeyFragment.newInstance()

		assertThat(fragment).isInstanceOf(AddAccountApiKeyFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun emptyApiKeyEmptySecret() {
		this.displayApiKeySection()

		onView(withId(R.id.api_key)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_api_key)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
	}

	@Test
	fun emptyApiKeyNoSecret() {
		this.displayApiKeySection()

		onView(withId(R.id.api_key)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_api_key)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
	}

	@Test
	fun emptyApiKeyWithSecret() {
		this.displayApiKeySection()

		onView(withId(R.id.api_key)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(typeText("secret"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_api_key)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
	}

	@Test
	fun noApiKeyEmptySecret() {
		this.displayApiKeySection()

		onView(withId(R.id.secret)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_api_key)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
	}

	@Test
	fun noApiKeyNoSecret() {
		this.displayApiKeySection()

		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_api_key)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
	}

	@Test
	fun noApiKeyWithSecret() {
		this.displayApiKeySection()

		onView(withId(R.id.secret)).perform(typeText("secret"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_api_key)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
	}

	@Test
	fun withApiKeyEmptySecret() {
		this.displayApiKeySection()

		onView(withId(R.id.api_key)).perform(typeText("api_key"), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(typeText("     "), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_secret)))
	}

	@Test
	fun withApiKeyNoSecret() {
		this.displayApiKeySection()

		onView(withId(R.id.api_key)).perform(typeText("api_key"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.api_key_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(null)))
		onView(withId(R.id.secret_layout))
			.check(ViewAssertions.matches(hasTextInputLayoutError(R.string.no_secret)))
	}

	@Test
	fun withApiKeyWithSecret() {
		this.displayApiKeySection()

		onView(withId(R.id.api_key)).perform(typeText("api_key"), closeSoftKeyboard())
		onView(withId(R.id.secret)).perform(typeText("secret"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.progress)).check(matches(isDisplayed()))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.api_key)).check(matches(allOf(isDisplayed(), withText("api_key"))))
		onView(withId(R.id.secret)).check(matches(allOf(isDisplayed(), withText("secret"))))

		onView(withId(R.id.back)).perform(click())
		onView(withId(R.id.user_id)).check(matches(allOf(isDisplayed(), withText("user_id"))))
	}

	private fun displayApiKeySection() {
		onView(withId(R.id.menu_account)).perform(click())
		onView(withId(R.id.add_account)).perform(click())
		onView(withId(R.id.user_id)).perform(typeText("user_id"), closeSoftKeyboard())
		onView(withId(R.id.next)).perform(click())
		onView(withId(R.id.add_account_api_key)).check(matches(isDisplayed()))
	}
}
