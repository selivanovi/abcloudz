package com.example.networking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networking.model.CharactersPageResponse
import com.example.networking.network.NetworkLayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkLayer.apiService.getCharacters().enqueue(object : Callback<CharactersPageResponse> {
            override fun onResponse(
                call: Call<CharactersPageResponse>,
                response: Response<CharactersPageResponse>
            ) {
                Log.i("MainActivity", "List size: ${response.body()?.charactersList?.size}")
            }

            override fun onFailure(call: Call<CharactersPageResponse>, t: Throwable) {
                Log.i("MainActivity", t.message.toString())
            }
        })
    }
}