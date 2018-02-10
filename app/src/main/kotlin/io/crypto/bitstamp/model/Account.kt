package io.crypto.bitstamp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
	@ColumnInfo(name = "api_key")
	var apiKey: String = "",
	@ColumnInfo(name = "customer_id")
	@PrimaryKey
	var customerId: String = "",
	@ColumnInfo
	var secret: String = ""
)
