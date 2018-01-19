package io.crypto.bitstamp.fragment

import android.support.v4.app.Fragment
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

abstract class BaseFragment : Fragment() {
	private val jobs = mutableListOf<Job>()

	private var running = false

	override fun onPause() {
		this.running = false

		this.clearJobs()

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		this.running = true
	}

	override fun setUserVisibleHint(isVisibleToUser: Boolean) {
		super.setUserVisibleHint(isVisibleToUser)

		this.running = isVisibleToUser
	}

	protected fun runPeriodically(
		time: Long = 2L,
		unit: TimeUnit = TimeUnit.SECONDS,
		action: () -> List<Job>
	) {
		if (this.running) {
			launch {
				val jobs = action()

				this@BaseFragment.jobs.addAll(action())

				jobs.forEach {
					if (running) {
						it.join()
					} else {
						it.cancel()
					}
				}

				synchronized(this@BaseFragment.jobs) {
					this@BaseFragment.jobs.clear()
				}

				if (running) {
					delay(time, unit)

					runPeriodically(time, unit, action)
				}
			}
		}
	}

	private fun clearJobs() {
		this.jobs.forEach {
			it.cancel()
		}

		this.jobs.clear()
	}
}
