package com.example.networking.screens


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.R
import com.example.networking.delegate.*
import com.example.networking.viewmodel.CharacterViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.fragment_list) {

    private val characterViewModel: CharacterViewModel by activityViewModels<CharacterViewModel>()

    private val pagingDataAdapter: CharacterPaginDelegateAdapter<DelegateAdapterItem> =
        CharacterPaginDelegateAdapter<DelegateAdapterItem>(
            AdapterDelegateManager(
                CharacterAdapterItemDelegate<DelegateAdapterItem>(R.layout.item_recyclerview)
            ),
            Comparator()
        )

    private val recyclerView: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView?.apply {
            adapter = pagingDataAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        lifecycleScope.launch {
            characterViewModel.searchCharacter().collectLatest { pagingData ->
                pagingDataAdapter.submitData(pagingData)
            }
        }
    }
}