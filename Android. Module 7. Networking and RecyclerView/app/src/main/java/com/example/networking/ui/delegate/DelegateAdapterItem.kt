package com.example.networking.ui.delegate

interface DelegateAdapterItem {

    fun id(): Int

    fun compareContent(item: DelegateAdapterItem): Boolean
}
