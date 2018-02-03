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
import io.crypto.bitstamp.extension.parseJson
import io.crypto.bitstamp.model.AccountBalance
import io.crypto.bitstamp.model.ApiError
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class AddAccountCheckInformationFragment
	: Fragment(), Callback<AccountBalance>, View.OnClickListener {
	companion object {
		fun newInstance() = AddAccountCheckInformationFragment()
	}

	private val callback by lazy { this.activity as AddAccountEvent }

	override fun onClick(view: View) {
		when (view.id) {
			R.id.back -> this.displayPreviousScreen()
			R.id.login_now -> this.login()
			R.id.save_account -> this.saveAccount()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_add_account_check_information, container, false)
	}

	override fun onFailure(call: Call<AccountBalance>, t: Throwable) {
		BitstampServices.account = null

		if (this.isAdded) {
			this.displayError(t.localizedMessage)
		}
	}

	override fun onPause() {
		this.resetViewsState()

		super.onPause()
	}

	override fun onResponse(call: Call<AccountBalance>, response: Response<AccountBalance>) {
		BitstampServices.account = null

		if (this.isAdded) {
			if (response.isSuccessful) {
				this.displaySuccess()
			} else {
				val errorResponse = response.errorBody()?.string()?.parseJson<ApiError>()

				this.displayError(errorResponse?.reason)
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.back.setOnClickListener(this)
	}

	override fun onResume() {
		super.onResume()

		this.progress.visibility = View.VISIBLE

		BitstampServices.account = this.callback.getAccount()
		BitstampServices.privateApi.getAccountBalance().enqueue(this)
	}

	private fun displayError(errorMessage: String?) {
		this.login_now.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		this.save_account.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		this.status.let {
			this.context?.let { context ->
				it.imageTintList =
						ColorStateList.valueOf(ContextCompat.getColor(context, R.color.ask))
			}

			it.setImageResource(R.drawable.ic_cancel)
			it.visibility = View.VISIBLE
		}

		this.status_text.let {
			it.text = errorMessage ?: this.getString(R.string.connection_error)
			it.visibility = View.VISIBLE
		}

		this.progress.visibility = View.GONE
	}

	private fun displayPreviousScreen() {
		this.callback.navigateToSection(AddAccountEvent.Section.API_KEY)
	}

	private fun displaySuccess() {
		this.login_now.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		this.save_account.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		this.status.let {
			this.context?.let { context ->
				it.imageTintList =
						ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bid))
			}

			it.setImageResource(R.drawable.ic_check)
			it.visibility = View.VISIBLE
		}

		this.progress.visibility = View.GONE
		this.status_text.visibility = View.GONE

		launch {
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
		}
	}

	private fun login() {
		BitstampServices.account = this.callback.getAccount()

		this.callback.navigateToSection(AddAccountEvent.Section.LOGIN)
	}

	private fun resetViewsState() {
		this.login_now.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		this.save_account.let {
			it.setOnClickListener(null)
			it.visibility = View.GONE
		}

		this.progress.visibility = View.GONE
		this.status.visibility = View.GONE
		this.status_text.visibility = View.GONE
	}

	private fun saveAccount() {
		this.callback.navigateToSection(AddAccountEvent.Section.SAVE_ACCOUNT)
	}
}
