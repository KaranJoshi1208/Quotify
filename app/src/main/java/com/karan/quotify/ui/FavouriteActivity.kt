package com.karan.quotify.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.karan.quotify.R
import com.karan.quotify.adapter.FavAdapter
import com.karan.quotify.databinding.ActivityFavouriteBinding
import com.karan.quotify.viewmodel.FavViewModel
import java.io.File
import java.io.FileOutputStream

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binder: ActivityFavouriteBinding
    private lateinit var viewModel: FavViewModel
    private lateinit var favAdapter: FavAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binder = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binder.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this@FavouriteActivity,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[FavViewModel::class.java]

        favAdapter = FavAdapter(this@FavouriteActivity)

        binder.apply {
            favView.adapter = favAdapter
            favView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        }

        viewModel.favList.observe(
            this@FavouriteActivity,
            Observer { list->
                list?.let {
                    favAdapter.updateList(it)
//                    Log.d("room_values", "Number of favorite quotes: ${it.size}")
                }
            }
        )

        binder.searchView.apply {
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        favAdapter.filterList(newText.toString())
                    }
                    return true
                }
            })
        }

    }

}