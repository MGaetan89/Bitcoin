package io.crypto.bitstamp.extension

import com.squareup.moshi.Moshi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

inline fun <reified T> String.parseJson(): T {
	return Moshi.Builder().build().adapter(T::class.java).fromJson(this)
}

fun String.toTimestamp(): Long? {
	return try {
		SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(this).time / 1000L
	} catch (exception: ParseException) {
		null
	}
}
