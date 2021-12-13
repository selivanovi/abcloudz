package com.example.networking.ui.delegate

interface DelegateAdapterItem {

    val id: Int?

    fun compareContent(item: DelegateAdapterItem): Boolean
}
