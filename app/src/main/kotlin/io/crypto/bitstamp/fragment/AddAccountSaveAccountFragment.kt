package io.crypto.bitstamp.fragment

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.AddAccountEvent
import io.crypto.bitstamp.dao.BitstampDatabase
import io.crypto.bitstamp.extension.getCleanText
import io.crypto.bitstamp.model.Account
import io.crypto.bitstamp.network.BitstampServices
import io.crypto.bitstamp.util.CipherWrapper
import io.crypto.bitstamp.util.KeyStoreWrapper
import kotlinx.android.synthetic.main.fragment_add_account_save_account.back
import kotlinx.android.synthetic.main.fragment_add_account_save_account.information
import kotlinx.android.synthetic.main.fragment_add_account_save_account.pin_code
import kotlinx.android.synthetic.main.fragment_add_account_save_account.pin_code_layout
import kotlinx.android.synthetic.main.fragment_add_account_save_account.save
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AddAccountSaveAccountFragment : Fragment(), View.OnClickListener {
	companion object {
		fun newInstance() = AddAccountSaveAccountFragment()
	}

	private val callback by lazy { this.activity as AddAccountEvent }

	override fun onClick(view: View) {
		when (view.id) {
			R.id.back -> this.displayPreviousScreen()
			R.id.save -> this.saveAccount()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_add_account_save_account, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val maxSize = this.resources.getInteger(R.integer.pin_code_max_size)
		val minSize = this.resources.getInteger(R.integer.pin_code_min_size)

		this.back.setOnClickListener(this)
		this.information.text = this.getString(R.string.pin_code_information, minSize, maxSize)
		this.save.setOnClickListener(this)
		this.pin_code.setOnEditorActionListener { _, actionId, _ ->
			return@setOnEditorActionListener when (actionId) {
				EditorInfo.IME_ACTION_GO -> save.performClick()
				else -> false
			}
		}
	}

	private fun displayPreviousScreen() {
		this.callback.navigateToSection(AddAccountEvent.Section.CHECK_INFORMATION)
	}

	private fun getPin(): String? {
		val maxSize = this.resources.getInteger(R.integer.pin_code_max_size)
		val minSize = this.resources.getInteger(R.integer.pin_code_min_size)

		return this.pin_code.getCleanText()?.takeIf { it.length in minSize..maxSize }
	}

	private fun saveAccount() {
		val context = this.context
		val pinCode = this.getPin()
		if (pinCode == null) {
			this.pin_code_layout.error = this.getString(R.string.no_pin_code)
		} else if (context != null) {
			this.pin_code_layout.error = null

			val account = this.callback.getAccount()

			launch {
				val keyStoreWrapper = KeyStoreWrapper()
				keyStoreWrapper.createAndroidKeyStoreAsymmetricKey(pinCode, context)

				val masterKey = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair(pinCode)

				val cipherWrapper = CipherWrapper()
				val apiKey = cipherWrapper.encrypt(account.apiKey, masterKey?.public)
				val secret = cipherWrapper.encrypt(account.secret, masterKey?.public)

				val database = Room.databaseBuilder(
					context, BitstampDatabase::class.java, "bitstamp"
				).build()
				database.accountDao().saveAccount(Account(apiKey, account.customerId, secret))
				database.close()

				BitstampServices.account = account

				launch(UI) {
					callback.navigateToSection(AddAccountEvent.Section.LOGIN)
				}
			}
		}
	}
}
