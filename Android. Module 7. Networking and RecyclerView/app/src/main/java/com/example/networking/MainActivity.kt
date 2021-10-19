package com.example.networking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networking.model.Character
import com.example.networking.model.CharactersList
import com.example.networking.network.NetworkLayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkLayer.apiService.getCharacters().enqueue(object : Callback<CharactersList> {
            override fun onResponse(
                call: Call<CharactersList>,
                response: Response<CharactersList>
            ) {
                Log.i("MainActivity", "List size: ${response.body()?.charactersList?.size}")
            }

            override fun onFailure(call: Call<CharactersList>, t: Throwable) {
                Log.i("MainActivity", t.message.toString())
            }

        })
    }
}