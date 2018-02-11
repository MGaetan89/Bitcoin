package io.crypto.bitstamp.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.crypto.bitstamp.model.Account

@Dao
interface AccountDao {
	@Query("SELECT * FROM accounts ORDER BY customer_id ASC")
	fun getAll(): List<Account>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun saveAccount(account: Account)
}
