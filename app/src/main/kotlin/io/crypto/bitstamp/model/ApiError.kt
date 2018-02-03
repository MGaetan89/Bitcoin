package io.crypto.bitstamp.model

data class ApiError(
	val code: String,
	val reason: String,
	val status: String
)
