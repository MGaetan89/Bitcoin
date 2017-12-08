package io.bitcoin.extension

import android.content.SharedPreferences
import com.google.gson.Gson
import io.bitcoin.model.Order

fun SharedPreferences.getExchanges(): List<String> {
	val exchanges = this.getStringSet("exchanges", emptySet()).toList()

	return if (exchanges.isEmpty()) listOf("btceur", "") else exchanges.sorted()
}

fun SharedPreferences.getOrders(): List<Order> {
	return this.getStringSet("orders", setOf())
			.map { it.parseJson<Order>() }
			.sortedBy { it.id }
}

fun SharedPreferences.removeOrder(order: Order) {
	val orders = this.getOrders()
			.filter { it.id != order.id }
			.map { it.toJson() }
			.toSet()

	this.edit().putStringSet("orders", orders).apply()
}

fun SharedPreferences.saveExchanges(exchanges: Set<String>) {
	val adjustedExchanges = exchanges.map { if (it == "btcusd") "" else it }.toSet()

	this.edit().putStringSet("exchanges", adjustedExchanges).apply()
}

fun SharedPreferences.saveOrder(order: Order) {
	val orders = this.getStringSet("orders", setOf()).toMutableSet()
	orders += order.toJson()

	this.edit().putStringSet("orders", orders).apply()
}

private fun Any.toJson(): String {
	return Gson().toJson(this)
}

private inline fun <reified T> String.parseJson(): T {
	return Gson().fromJson(this, T::class.java)
}
