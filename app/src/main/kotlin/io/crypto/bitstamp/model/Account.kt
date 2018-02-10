package io.crypto.bitstamp.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "accounts")
@Parcelize
@SuppressLint("ParcelCreator")
data class Account(
	@ColumnInfo(name = "api_key")
	var apiKey: String = "",
	@ColumnInfo(name = "customer_id")
	@PrimaryKey
	var customerId: String = "",
	@ColumnInfo
	var secret: String = ""
) : Parcelable
