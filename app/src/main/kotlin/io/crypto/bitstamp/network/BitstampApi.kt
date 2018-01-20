package io.crypto.bitstamp.network

import io.crypto.bitstamp.model.AccountBalance
import io.crypto.bitstamp.model.CanceledOrder
import io.crypto.bitstamp.model.OpenOrder
import io.crypto.bitstamp.model.OpenOrderStatus
import io.crypto.bitstamp.model.PriceOrderBook
import io.crypto.bitstamp.model.PriceTransaction
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.model.UserTransaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BitstampApi {
	@POST("/api/v2/cancel_order/")
	fun cancelOrder(@Query("id") id: Long): Call<CanceledOrder>

	@POST("/api/v2/balance/")
	fun getAccountBalance(): Call<AccountBalance>

	@POST("/api/v2/open_orders/all/")
	fun getOpenOrders(): Call<List<OpenOrder>>

	@GET("/api/v2/order_book/{currency_pair}/")
	fun getOrderBook(@Path("currency_pair") currencyPair: String): Call<PriceOrderBook>

	@POST("/api/order_status/")
	fun getOrderStatus(@Query("id") id: Long): Call<OpenOrderStatus>

	@GET("/api/v2/ticker/{currency_pair}/")
	fun getTicker(@Path("currency_pair") currencyPair: String): Call<Ticker>

	@GET("/api/v2/trading-pairs-info/")
	fun getTradingPairs(): Call<List<TradingPair>>

	@GET("/api/v2/transactions/{currency_pair}/")
	fun getTransactions(@Path("currency_pair") currencyPair: String): Call<List<PriceTransaction>>

	@POST("/api/v2/user_transactions/")
	fun getUserTransactions(): Call<List<UserTransaction>>
}
