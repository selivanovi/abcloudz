package com.example.networking.delegate

interface DelegateAdapterItem {

    fun id(): Int

    fun compareContent(item: DelegateAdapterItem): Boolean
}
