package com.karan.quotify.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.LiveData
import com.karan.quotify.model.Quote
import com.karan.quotify.model.QuoteDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val context: Context) {

    private var currentID = 0
    private val dao = QuoteDB.getInstance().quoteDao()
    private var totalQuote : Int = -1
    val favList: LiveData<List<Quote>> = dao.getFav()

    init {
        val sharedPref = context.getSharedPreferences("room_pref", MODE_PRIVATE)

        // why default value is 1?
        // because in room id(auto generated) starts from 1

        val id = sharedPref.getInt("current_id", 1)
        currentID = id
//        Log.d("room_values", "Current quote ID = $id")
    }

    suspend fun getQuote(): Quote = withContext(Dispatchers.IO) {
        dao.getQuoteById(currentID)
    }

    suspend fun nextQuote(): Quote = withContext(Dispatchers.IO) {

        if(totalQuote <= -1) {
            val sp = context.getSharedPreferences("room_pref", MODE_PRIVATE)
            totalQuote = sp.getInt("quote_size", -1)
//            Log.d("room_values", "Total quotes = $totalQuote")
        }
        if(currentID < totalQuote) {
            currentID++
        }
        dao.getQuoteById(currentID)
    }

    suspend fun previousQuote(): Quote = withContext(Dispatchers.IO) {
        if(currentID > 1){
            currentID--
        }
        dao.getQuoteById(currentID)
    }

    suspend fun updateFav(id : Int, isFav : Int) {
        dao.updateFav(id, isFav)
    }

    fun setLastQuoteId() {
        val sharedPref = context.getSharedPreferences("room_pref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("current_id", currentID)
        editor.apply()
//        Log.d("room_values", "Last visited quote ID is $currentID")
    }
}