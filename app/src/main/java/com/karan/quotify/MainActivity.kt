package com.karan.quotify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.karan.quotify.databinding.ActivityMainBinding
import com.karan.quotify.model.Quote
import com.karan.quotify.ui.FavouriteActivity
import com.karan.quotify.viewmodel.MainViewModel
import com.karan.quotify.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // I can't(don't want to) implement the whole file sharing functionality here also (cuz I am Lazy)
        binder.shareBtn.visibility = View.INVISIBLE

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(applicationContext, application)
        )[MainViewModel::class.java]


        // here the observer is setting the quote as we change the quote in viewmodel

        mainViewModel.quote.observe(this) { quote ->
            setQuote(quote)
        }

        // since I am using shared pref to get last visited quote
        mainViewModel.getQuote()

        binder.nextBtn.setOnClickListener {
            mainViewModel.nextQuote()
        }

        binder.previousBtn.setOnClickListener {
            mainViewModel.previousQuote()
        }

        binder.heart.setOnClickListener {
            val currentQuote = mainViewModel.quote.value
            currentQuote?.also {
                it.isFav = if(it.isFav == 0) 1 else 0
                updateHeartIcon(it.isFav)
                mainViewModel.updateFav(currentQuote.id,currentQuote.isFav)                          // updating the Quote Object.
            }
        }


        binder.wishList.setOnClickListener {
            Intent(this@MainActivity, FavouriteActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setQuote(quote: Quote) {
        binder.apply {
            quoteTxt.text = quote.text
            authorTxt.text = quote.author ?: "Unknown"
            updateHeartIcon(quote.isFav)
        }
    }

    private fun updateHeartIcon(isFav: Int) {
        if (isFav == 1) {
            binder.heart.setBackgroundResource(R.drawable.heart_fill)
        } else {
            binder.heart.setBackgroundResource(R.drawable.heart_hollow)
        }
    }

    override fun onStop() {
        mainViewModel.setLastQuoteId()
        super.onStop()
    }
}