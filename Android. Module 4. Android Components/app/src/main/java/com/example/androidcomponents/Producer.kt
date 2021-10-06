package com.example.androidcomponents

interface Producer {

    fun addObserver(consumer: Consumer)
    fun removeObserver(consumer: Consumer)
    fun scanData()
}