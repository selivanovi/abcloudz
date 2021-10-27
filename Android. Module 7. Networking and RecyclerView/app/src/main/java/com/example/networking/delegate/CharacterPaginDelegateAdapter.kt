package com.example.networking.delegate

import androidx.recyclerview.widget.DiffUtil

class CharacterPaginDelegateAdapter<T : Any>(
    adapterDelegateManager: AdapterDelegateManager<T>,
    diffUtil: DiffUtil.ItemCallback<T>
): PagingDelegationAdapter<T>(adapterDelegateManager, diffUtil) {
}