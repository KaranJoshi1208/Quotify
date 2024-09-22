package com.karan.quotify.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.karan.quotify.R
import com.karan.quotify.databinding.ActivityMainBinding
import com.karan.quotify.model.Quote
import com.karan.quotify.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class DisplayFav : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding
    private var quote: Quote? = null
    private val repo : Repository by lazy {
        Repository(applicationContext)
    }

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
        binder.apply {
            heart.setBackgroundResource(R.drawable.heart_fill)
            nextBtn.visibility = View.INVISIBLE
            previousBtn.visibility = View.INVISIBLE
            wishList.visibility = View.INVISIBLE
        }

        try {
            quote = if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("quote", Quote::class.java)
            } else {
                intent.getSerializableExtra("quote") as Quote?
            }
        } catch (e: Exception) {
            finish()
        }

        quote?.let {
            binder.apply {
                heart.setBackgroundResource(R.drawable.heart_fill)
                quoteTxt.text = it.text
                authorTxt.text = it.author
            }
        }
        binder.heart.setOnClickListener {
            quote?.also {
                it.isFav = if(it.isFav <= 0) 1 else 0
                updateHeartIcon(it.isFav)
                CoroutineScope(Dispatchers.IO).launch {
                    repo.updateFav(it.id,it.isFav)                          // updating the Quote Object.
                }
            }
        }

        binder.shareBtn.setOnClickListener {
            // Capture the quote view
            val quoteView = binder.main  // Replace with the actual view ID that contains the quote
            val bitmap = captureScreen(quoteView)

            // Save bitmap to cache and get its URI
            val uri = saveBitmapToCache(bitmap)

            // Share the image if saving was successful
            uri?.let {
                shareQuoteImage(it)
            }
        }


    }

    private fun updateHeartIcon(isFav: Int) {
        if (isFav == 1) {
            binder.heart.setBackgroundResource(R.drawable.heart_fill)
        } else {
            binder.heart.setBackgroundResource(R.drawable.heart_hollow)
        }
    }

    private fun captureScreen(view : View) : Bitmap {
        val ss = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(ss)
        view.draw(canvas)
        return ss
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri? {
        val cachePath = File(externalCacheDir, "shared_images")
        cachePath.mkdirs() // Create the directory if not exists
        val file = File(cachePath, "quote_snapshot.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        return FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
    }

    private fun shareQuoteImage(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share Quote via"))
    }


}