package com.example.localdatastorage.utils

import android.content.Context
import android.util.Log
import com.example.localdatastorage.model.entities.json.DonutJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

private val TAG = "DonutJsonParser"

object DonutJsonParser {

    fun pareJson(context: Context, fileName: String): List<DonutJson> {
        val jsonFileString = getJsonDataFromAsset(context, fileName)!!
        Log.i("data", jsonFileString)

        val gson = Gson()
        val listPersonType = object : TypeToken<List<DonutJson>>() {}.type

        val donuts: List<DonutJson> = gson.fromJson(jsonFileString, listPersonType)
        donuts.forEachIndexed { idx, donut -> Log.i("data", "> Item $idx:\n$donut") }
        return donuts
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            Log.e(TAG, ioException.toString())
            return null
        }
        return jsonString
    }
}
