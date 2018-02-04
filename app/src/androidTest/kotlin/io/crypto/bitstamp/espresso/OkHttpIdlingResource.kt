package io.crypto.bitstamp.espresso

import android.support.test.espresso.IdlingResource
import okhttp3.Dispatcher

class OkHttpIdlingResource(private val resourceName: String, private val dispatcher: Dispatcher) :
	IdlingResource {
	private var callback: IdlingResource.ResourceCallback? = null

	init {
		this.dispatcher.setIdleCallback {
			this.callback?.onTransitionToIdle()
		}
	}

	override fun getName() = this.resourceName

	override fun isIdleNow(): Boolean {
		val idle = this.dispatcher.runningCallsCount() == 0
		if (idle) {
			this.callback?.onTransitionToIdle()
		}
		return idle
	}

	override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
		this.callback = callback
	}
}
