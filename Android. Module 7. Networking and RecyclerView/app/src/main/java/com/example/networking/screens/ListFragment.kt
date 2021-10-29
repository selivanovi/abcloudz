package com.example.networking.screens


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.R
import com.example.networking.delegate.*
import com.example.networking.viewmodel.CharacterViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.fragment_list) {

    private val characterViewModel by activityViewModels<CharacterViewModel>()

    private lateinit var pagingDataAdapter: CharacterPaginDelegateAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerView(view)
    }

    override fun onStart() {
        lifecycleScope.launch {
            characterViewModel.searchCharacter().collectLatest { pagingData ->
                pagingDataAdapter.submitData(pagingData)
            }
        }
        super.onStart()
    }


    private fun createRecyclerView(view: View) {

        pagingDataAdapter = CharacterPaginDelegateAdapter(
            AdapterDelegateManager(
                CharacterAliveAdapterItemDelegate(R.layout.item_alive_recyclerview),
                CharacterDeadAdapterItemDelegate(R.layout.item_dead_recyclerview),
                CharacterUnknownAdapterItemDelegate(R.layout.item_unknown_recyclerview),
            ),
            Comparator()
        )

        pagingDataAdapter.onClickListener = ::onItemClickListener

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = pagingDataAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun onItemClickListener(item: DelegateAdapterItem){
        findNavController().navigate(
            R.id.action_listFragment_to_detailsFragment,
            Bundle().apply {
                putInt(DetailsFragment.ARG_ID, item.id())
            }
        )
    }
}