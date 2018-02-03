package io.crypto.bitstamp.espresso

import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

fun hasTextInputLayoutError(@StringRes errorRes: Int): Matcher<View> {
	return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
		override fun describeTo(description: Description) {
			description.appendText("has error text")
		}

		override fun matchesSafely(textInputLayout: TextInputLayout): Boolean {
			return textInputLayout.error == textInputLayout.resources.getString(errorRes)
		}
	}
}

fun hasTextInputLayoutError(expectedError: String?): Matcher<View> {
	return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
		override fun describeTo(description: Description) {
			description.appendText("has error text")
		}

		override fun matchesSafely(textInputLayout: TextInputLayout): Boolean {
			return textInputLayout.error == expectedError
		}
	}
}
