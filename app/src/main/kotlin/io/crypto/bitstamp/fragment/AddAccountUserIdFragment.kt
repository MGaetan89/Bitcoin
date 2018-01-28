package io.crypto.bitstamp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import io.crypto.bitstamp.R
import io.crypto.bitstamp.activity.AddAccountEvent
import kotlinx.android.synthetic.main.fragment_add_account_user_id.next
import kotlinx.android.synthetic.main.fragment_add_account_user_id.user_id
import kotlinx.android.synthetic.main.fragment_add_account_user_id.user_id_layout

class AddAccountUserIdFragment : Fragment(), View.OnClickListener {
	companion object {
		fun newInstance() = AddAccountUserIdFragment()
	}

	private val callback by lazy { this.activity as AddAccountEvent }

	override fun onClick(view: View) {
		when (view.id) {
			R.id.next -> this.displayNextScreen()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_add_account_user_id, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.next.setOnClickListener(this)
		this.user_id.setOnEditorActionListener { _, actionId, _ ->
			return@setOnEditorActionListener when (actionId) {
				EditorInfo.IME_ACTION_GO -> next.performClick()
				else -> false
			}
		}
	}

	private fun displayNextScreen() {
		val userId = this.getUserId()
		if (userId == null) {
			this.user_id_layout.error = this.getString(R.string.no_user_id)
			return
		} else {
			this.user_id_layout.error = null
		}

		this.callback.setUserId(userId)
		this.callback.navigateToSection(AddAccountEvent.Section.API_KEY)
	}

	private fun getUserId(): String? {
		return this.user_id.text.trim().toString().takeIf { it.isNotEmpty() }
	}
}
