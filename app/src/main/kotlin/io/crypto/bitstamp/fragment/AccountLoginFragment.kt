package io.crypto.bitstamp.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import io.crypto.bitstamp.BuildConfig
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.AccountActivity
import io.crypto.bitstamp.extension.getCleanText
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.model.Account
import io.crypto.bitstamp.network.BitstampServices
import io.crypto.bitstamp.util.CipherWrapper
import io.crypto.bitstamp.util.KeyStoreWrapper
import kotlinx.android.synthetic.main.fragment_account_login.login
import kotlinx.android.synthetic.main.fragment_account_login.pin_code
import kotlinx.android.synthetic.main.fragment_account_login.pin_code_layout
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.security.KeyStoreException

class AccountLoginFragment : DialogFragment(), View.OnClickListener {
	companion object {
		private const val EXTRA_ACCOUNT = BuildConfig.APPLICATION_ID + ".extra.account"

		fun newInstance(account: Account) = AccountLoginFragment().apply {
			this.arguments = Bundle().apply {
				this.putParcelable(EXTRA_ACCOUNT, account)
			}
		}
	}

	override fun onClick(view: View) {
		when (view.id) {
			R.id.login -> this.checkPinCodeAndLogin()
		}
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val context = this.context ?: return super.onCreateDialog(savedInstanceState)

		return AlertDialog.Builder(context)
			.setView(this.view)
			.show()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_account_login, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.login.setOnClickListener(this)
		this.pin_code.setOnEditorActionListener { _, actionId, _ ->
			return@setOnEditorActionListener when (actionId) {
				EditorInfo.IME_ACTION_GO -> login.performClick()
				else -> false
			}
		}
	}

	private fun checkPinCodeAndLogin() {
		val context = this.context
		val pinCode = this.getPin()
		if (pinCode == null) {
			this.pin_code_layout.error = this.getString(R.string.no_pin_code)
		} else if (context != null) {
			this.pin_code_layout.error = null

			val account = this.arguments!!.getParcelable<Account>(EXTRA_ACCOUNT)

			launch {
				val keyStoreWrapper = KeyStoreWrapper()
				val masterKey = try {
					keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(pinCode)
				} catch (exception: KeyStoreException) {
					launch(UI) {
						pin_code_layout.error = context.getString(R.string.wrong_pin_code)
					}

					null
				} ?: return@launch

				val cipherWrapper = CipherWrapper()
				val apiKey = cipherWrapper.decrypt(account.apiKey, masterKey.private)
				val secret = cipherWrapper.decrypt(account.secret, masterKey.private)

				BitstampServices.account = Account(apiKey, account.customerId, secret)

				launch(UI) {
					context.startActivity<AccountActivity>(
						Intent.FLAG_ACTIVITY_NEW_TASK,
						Intent.FLAG_ACTIVITY_CLEAR_TASK
					)
				}
			}
		}
	}

	private fun getPin(): String? {
		val maxSize = this.resources.getInteger(R.integer.pin_code_max_size)
		val minSize = this.resources.getInteger(R.integer.pin_code_min_size)

		return this.pin_code.getCleanText()?.takeIf { it.length in minSize..maxSize }
	}
}
