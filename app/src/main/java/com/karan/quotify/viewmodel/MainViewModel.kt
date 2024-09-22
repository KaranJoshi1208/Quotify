package com.karan.quotify.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karan.quotify.model.Quote
import com.karan.quotify.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val context: Context , application: Application) : AndroidViewModel(application) {

    private val repo: Repository = Repository(context)

    private var _quote = MutableLiveData<Quote>()
    val quote: LiveData<Quote>
        get() = _quote

    fun getQuote() {
        viewModelScope.launch {
            _quote.value = repo.getQuote()
        }
    }

    fun nextQuote() {
        viewModelScope.launch {
            _quote.value = repo.nextQuote()
            Log.d("isExecuting", "yes , Executed log statement")
            Log.d("isExecuting", "Returned")
        }
    }

    fun previousQuote() {
        viewModelScope.launch {
            _quote.value = repo.previousQuote()
        }
    }

    fun updateFav(id : Int, isFav : Int) {
        viewModelScope.launch {
            repo.updateFav(id, isFav)
        }
    }

    fun setLastQuoteId() = repo.setLastQuoteId()
}