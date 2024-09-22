package com.karan.quotify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.karan.quotify.MainActivity
import com.karan.quotify.R
import com.karan.quotify.model.Quote
import com.karan.quotify.model.QuoteDB
import com.karan.quotify.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SplashScreen : AppCompatActivity() {

    private lateinit var db: QuoteDB
    private var data : Array<Quote> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = QuoteDB.getInstance(this@SplashScreen.applicationContext)

        val sp = applicationContext.getSharedPreferences("room_pref", MODE_PRIVATE)
        val isRoomPopulated = sp.getBoolean("isRoomPopulated", false)
        Log.d("room_values", "$isRoomPopulated")

        if(!isRoomPopulated) {
            populateRoom()
            Log.d("room_values", "Data Filled in Room.....or does it?")
        }

        Handler(Looper.getMainLooper()).postDelayed(
            object : Runnable {
                override fun run() {
                    Intent(this@SplashScreen, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            },
            2500
        )
    }

    private fun populateRoom() {
        getAllQuotes(applicationContext)
        runBlocking(Dispatchers.IO) {
            db.quoteDao().insertAll(data)
        }
    }

    private fun getAllQuotes(context : Context) {
        val inputStream = context.assets.open("quotes.json")
        val size : Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        val gson = Gson()
        data = gson.fromJson(json, Array<Quote>::class.java)

        // once populated , should never execute again
        val sp = context.getSharedPreferences("room_pref", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean("isRoomPopulated", true)
        editor.putInt("quote_size", data.size)
        editor.apply()
    }
}