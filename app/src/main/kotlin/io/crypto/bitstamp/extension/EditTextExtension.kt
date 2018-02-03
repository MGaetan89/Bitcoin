package io.crypto.bitstamp.extension

import android.widget.EditText

fun EditText.getCleanText(): String? {
	return this.text.trim().toString().takeIf { it.isNotEmpty() }
}
