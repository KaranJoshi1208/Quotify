package com.karan.quotify.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Database(
    entities = [Quote::class],
    version = 1
)
abstract class QuoteDB : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    companion object {

        @Volatile
        private var INSTANCE: QuoteDB? = null

        fun getInstance(context: Context? = null): QuoteDB {
            return INSTANCE ?: synchronized(this) {
                val temp = Room.databaseBuilder(
                    context!!.applicationContext,
                    QuoteDB::class.java,
                    "quotify_database.db"
                )
//                    .addCallback(LoadData(context.applicationContext))                                                                  // U can just provide a object : RoomDatabase.CallBack an override the onCreate()
                    .build()
                INSTANCE = temp
                temp
            }
        }

//        private class LoadData(private val context: Context) :
//            RoomDatabase.Callback() {                                          // but here I am making a class and extending it from RoomDatabase.Callback()
//
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//
//                val inputStream = context.assets.open("quotes.json")
//                val json = inputStream.bufferedReader().use { it.readText() }
//                inputStream.close()
//                val gson = Gson()
//
//                INSTANCE?.quoteDao()?.insertAll(gson.fromJson(json, Array<Quote>::class.java))
//                Log.d("callBack", "Callback function successfully executed !!")
//            }
//        }
    }

}