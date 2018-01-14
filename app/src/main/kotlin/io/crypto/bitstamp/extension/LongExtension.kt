package io.crypto.bitstamp.extension

import java.text.DateFormat
import java.util.Date

fun Long.toFormattedDate(): String = DateFormat.getDateInstance().format(Date(this * 1000))

fun Long.toFormattedDateTime(): String = DateFormat.getDateTimeInstance().format(Date(this * 1000))

fun Long.toFormattedTime(): String = DateFormat.getTimeInstance().format(Date(this * 1000))
