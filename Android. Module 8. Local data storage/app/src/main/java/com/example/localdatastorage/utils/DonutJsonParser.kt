package com.example.localdatastorage.utils

import android.content.Context
import android.util.Log
import com.example.localdatastorage.model.entities.Donut
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object DonutJsonParser {

    fun pareJson(context: Context, fileName: String): List<Donut> {
        val jsonFileString = getJsonDataFromAsset(context, fileName)!!
        Log.i("data", jsonFileString)

        val gson = Gson()
        val listPersonType = object : TypeToken<List<Donut>>() {}.type

        var persons: List<Donut> = gson.fromJson(jsonFileString, listPersonType)
        persons.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }
        return persons
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
