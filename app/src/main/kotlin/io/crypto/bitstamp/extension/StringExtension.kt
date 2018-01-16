package io.crypto.bitstamp.extension

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toTimestamp(): Long {
	return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(this).time / 1000
}
