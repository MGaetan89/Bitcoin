package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.AddAccountEvent
import io.crypto.bitstamp.extension.getCleanText
import kotlinx.android.synthetic.main.fragment_add_account_api_key.api_key
import kotlinx.android.synthetic.main.fragment_add_account_api_key.api_key_layout
import kotlinx.android.synthetic.main.fragment_add_account_api_key.back
import kotlinx.android.synthetic.main.fragment_add_account_api_key.next
import kotlinx.android.synthetic.main.fragment_add_account_api_key.secret
import kotlinx.android.synthetic.main.fragment_add_account_api_key.secret_layout

class AddAccountApiKeyFragment : Fragment(), View.OnClickListener {
	companion object {
		fun newInstance() = AddAccountApiKeyFragment()
	}

	private val callback by lazy { this.activity as AddAccountEvent }

	override fun onClick(view: View) {
		when (view.id) {
			R.id.back -> this.displayPreviousScreen()
			R.id.next -> this.displayNextScreen()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_add_account_api_key, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.back.setOnClickListener(this)
		this.next.setOnClickListener(this)
		this.secret.setOnEditorActionListener { _, actionId, _ ->
			return@setOnEditorActionListener when (actionId) {
				EditorInfo.IME_ACTION_GO -> next.performClick()
				else -> false
			}
		}
	}

	private fun displayNextScreen() {
		val apiKey = this.api_key.getCleanText()
		if (apiKey == null) {
			this.api_key_layout.error = this.getString(R.string.no_api_key)
			return
		} else {
			this.api_key_layout.error = null
		}

		val secret = this.secret.getCleanText()
		if (secret == null) {
			this.secret_layout.error = this.getString(R.string.no_secret)
			return
		} else {
			this.secret_layout.error = null
		}

		this.callback.setApiKey(apiKey)
		this.callback.setSecret(secret)
		this.callback.navigateToSection(AddAccountEvent.Section.CHECK_INFORMATION)
	}

	private fun displayPreviousScreen() {
		this.callback.navigateToSection(AddAccountEvent.Section.USER_ID)
	}
}
