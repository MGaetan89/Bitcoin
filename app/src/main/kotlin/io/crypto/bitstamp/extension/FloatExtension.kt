package io.crypto.bitstamp.extension

import java.text.NumberFormat

fun Float.toFormattedPercent(): String = NumberFormat.getPercentInstance().also {
	it.maximumFractionDigits = 2
	it.minimumFractionDigits = 2
}.format(this)

fun Float.toFormattedString(precision: Int): String = NumberFormat.getNumberInstance().also {
	it.maximumFractionDigits = precision
	it.minimumFractionDigits = precision
}.format(this)
