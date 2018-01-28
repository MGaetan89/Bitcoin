package io.crypto.bitstamp.activity

import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewStub
import io.crypto.bitstamp.R
import io.crypto.bitstamp.extension.startActivity
import io.crypto.bitstamp.fragment.AddAccountApiKeyFragment
import io.crypto.bitstamp.fragment.AddAccountCheckInformationFragment
import io.crypto.bitstamp.fragment.AddAccountSaveAccountFragment
import io.crypto.bitstamp.fragment.AddAccountUserIdFragment
import io.crypto.bitstamp.model.Account
import io.crypto.bitstamp.network.BitstampServices

interface AddAccountEvent {
	enum class Section {
		API_KEY, CHECK_INFORMATION, LOGIN, SAVE_ACCOUNT, USER_ID
	}

	fun getAccount(): Account

	fun navigateToSection(section: Section)

	fun setApiKey(apiKey: String)

	fun setSecret(secret: String)

	fun setUserId(userId: String)
}

class AddAccountActivity : BaseActivity(), AddAccountEvent {
	private var account = Triple("", "", "")
	private val fragments = mutableMapOf<AddAccountEvent.Section, Fragment>()

	override fun getAccount(): Account {
		val (apiKey, userId, secret) = this.account

		return Account(apiKey, userId, secret)
	}

	override fun getContentResourceId() = R.layout.activity_add_account

	override fun getSelectedTabId() = R.id.menu_account

	override fun getToolbarTitle(): CharSequence = this.getString(R.string.add_account)

	override fun navigateToSection(section: AddAccountEvent.Section) {
		val fragment = this.fragments.getOrPut(section) {
			when (section) {
				AddAccountEvent.Section.API_KEY -> AddAccountApiKeyFragment.newInstance()
				AddAccountEvent.Section.CHECK_INFORMATION -> AddAccountCheckInformationFragment.newInstance()
				AddAccountEvent.Section.LOGIN -> {
					BitstampServices.account = this.getAccount()

					return this.startActivity<AccountActivity>(
						Intent.FLAG_ACTIVITY_NEW_TASK,
						Intent.FLAG_ACTIVITY_CLEAR_TASK
					)
				}
				AddAccountEvent.Section.SAVE_ACCOUNT -> AddAccountSaveAccountFragment.newInstance()
				AddAccountEvent.Section.USER_ID -> AddAccountUserIdFragment.newInstance()
			}
		}

		this.supportFragmentManager.beginTransaction()
			.replace(R.id.content, fragment)
			.commit()
	}

	override fun onDestroy() {
		this.fragments.clear()

		super.onDestroy()
	}

	override fun onInflate(stub: ViewStub, inflated: View) {
		this.navigateToSection(AddAccountEvent.Section.USER_ID)

		super.onInflate(stub, inflated)
	}

	override fun setApiKey(apiKey: String) {
		val (_, userId, secret) = this.account
		this.account = Triple(apiKey, userId, secret)
	}

	override fun setSecret(secret: String) {
		val (apiKey, userId, _) = this.account
		this.account = Triple(apiKey, userId, secret)
	}

	override fun setUserId(userId: String) {
		val (apiKey, _, secret) = this.account
		this.account = Triple(apiKey, userId, secret)
	}
}
