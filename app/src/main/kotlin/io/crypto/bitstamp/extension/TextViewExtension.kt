package io.crypto.bitstamp.extension

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.widget.TextView

fun TextView.setTextColorResource(@ColorRes color: Int) {
	this.setTextColor(ContextCompat.getColor(this.context, color))
}
