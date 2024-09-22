package com.karan.quotify.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.karan.quotify.model.Quote
import com.karan.quotify.repository.Repository

class FavViewModel(application: Application) : AndroidViewModel(application) {

    private var repo : Repository = Repository(getApplication())
    var favList : LiveData<List<Quote>> = repo.favList
}