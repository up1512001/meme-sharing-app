package com.example.android.memesharingapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import layout.MySingleton

class MainActivity : AppCompatActivity() {

    var currentImageUrl:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

    }

     private fun loadMeme(){

         progressbar.visibility = View.VISIBLE
         nextButton.isEnabled = false
         shareButton.isEnabled = false

// Instantiate the RequestQueue.
         val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
         val jsonObjectRequest = JsonObjectRequest(
             Request.Method.GET, url,null,
             Response.Listener{ response ->
                 currentImageUrl = response.getString("url")
                 Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<Drawable>?,
                         isFirstResource: Boolean
                     ): Boolean {
                         progressbar.visibility = View.GONE
                         nextButton.isEnabled = true
                         return false
                     }

                     override fun onResourceReady(
                         resource: Drawable?,
                         model: Any?,
                         target: Target<Drawable>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean
                     ): Boolean {
                         progressbar.visibility = View.GONE
                         nextButton.isEnabled = true
                         shareButton.isEnabled = true
                         return false
                     }
                 }).into(memeImageView)
             },
             Response.ErrorListener {
                 Log.d("Failed","url failer")
             })

// Add the request to the RequestQueue.
         MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
     }

    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout new cool meme that I got from Reddit \n$currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme Using...")
        startActivity(chooser)

    }
}