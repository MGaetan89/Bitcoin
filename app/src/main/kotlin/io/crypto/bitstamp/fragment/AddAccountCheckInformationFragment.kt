package io.crypto.bitstamp.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.AddAccountEvent
import io.crypto.bitstamp.network.BitstampServices
import kotlinx.android.synthetic.main.fragment_add_account_check_information.back
import kotlinx.android.synthetic.main.fragment_add_account_check_information.login_now
import kotlinx.android.synthetic.main.fragment_add_account_check_information.progress
import kotlinx.android.synthetic.main.fragment_add_account_check_information.save_account
import kotlinx.android.synthetic.main.fragment_add_account_check_information.status
import kotlinx.android.synthetic.main.fragment_add_account_check_information.status_text
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.Random
import java.util.concurrent.TimeUnit

class AddAccountCheckInformationFragment : Fragment(), View.OnClickListener {
	companion object {
		fun newInstance() = AddAccountCheckInformationFragment()
	}

	private val callback by lazy { this.activity as AddAccountEvent }

	override fun onClick(view: View) {
		when (view.id) {
			R.id.back -> this.callback.navigateToSection(AddAccountEvent.Section.API_KEY)
			R.id.login_now -> this.login()
			R.id.save_account -> this.callback.navigateToSection(AddAccountEvent.Section.SAVE_ACCOUNT)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_add_account_check_information, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.back.setOnClickListener(this)
	}

	override fun onResume() {
		super.onResume()

		login_now.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		save_account.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		progress.visibility = View.VISIBLE
		status.visibility = View.GONE
		status_text.visibility = View.GONE

		launch {
			delay(1, TimeUnit.SECONDS)

			if (Random().nextBoolean()) {
				launch(UI) {
					login_now.let {
						it.setOnClickListener(null)
						it.visibility = View.GONE
					}

					save_account.let {
						it.setOnClickListener(null)
						it.visibility = View.GONE
					}

					status.let {
						this@AddAccountCheckInformationFragment.context?.let { context ->
							it.imageTintList = ColorStateList.valueOf(
								ContextCompat.getColor(context, R.color.bid)
							)
						}

						it.setImageResource(R.drawable.ic_check)
						it.visibility = View.VISIBLE
					}

					progress.visibility = View.GONE
					status_text.visibility = View.GONE
				}

				delay(1, TimeUnit.SECONDS)

				launch(UI) {
					val fragment = this@AddAccountCheckInformationFragment

					login_now.let {
						it.setOnClickListener(fragment)
						it.visibility = View.VISIBLE
					}

					save_account.let {
						it.setOnClickListener(fragment)
						it.visibility = View.VISIBLE
					}

					status.visibility = View.GONE
				}
			} else {
				launch(UI) {
					login_now.let {
						it.setOnClickListener(null)
						it.visibility = View.GONE
					}

					save_account.let {
						it.setOnClickListener(null)
						it.visibility = View.GONE
					}

					status.let {
						this@AddAccountCheckInformationFragment.context?.let { context ->
							it.imageTintList = ColorStateList.valueOf(
								ContextCompat.getColor(context, R.color.ask)
							)
						}

						it.setImageResource(R.drawable.ic_cancel)
						it.visibility = View.VISIBLE
					}

					progress.visibility = View.GONE
					status_text.visibility = View.VISIBLE
				}
			}
		}
	}

	private fun login() {
		BitstampServices.account = this.callback.getAccount()

		this.callback.navigateToSection(AddAccountEvent.Section.LOGIN)
	}
}
