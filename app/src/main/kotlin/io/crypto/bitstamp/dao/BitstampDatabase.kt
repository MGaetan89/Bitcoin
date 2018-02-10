package io.crypto.bitstamp.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.crypto.bitstamp.model.Account

@Database(entities = [Account::class], version = 1)
abstract class BitstampDatabase : RoomDatabase() {
	abstract fun accountDao(): AccountDao
}
