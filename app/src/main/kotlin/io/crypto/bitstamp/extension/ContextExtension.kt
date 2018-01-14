package io.crypto.bitstamp.extension

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.startActivity(vararg flags: Int, noinline extras: (Intent.() -> Unit)? = null) {
	val intent = Intent(this, T::class.java)

	extras?.invoke(intent)
	flags.forEach {
		intent.addFlags(it)
	}

	this.startActivity(intent)
}
