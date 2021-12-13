package com.example.networking.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asyncoperations.R
import com.example.networking.ui.delegate.AdapterDelegateManager
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.ui.recyclerviews.characters.DelegateItemComparator
import com.example.networking.ui.viewmodels.CharacterViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ListFragment : Fragment(R.layout.fragment_list) {

    private val characterViewModel by activityViewModels<CharacterViewModel>()

    //create adapter for character

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerView(view)
    }

    override fun onStart() {
        super.onStart()
        characterViewModel.searchCharacter().onEach { pagingData ->
            //set data into adapter
        }.launchIn(lifecycleScope)
    }


    private fun createRecyclerView(view: View) {


        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            //set adapter intro recyclerview
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun onItemClickListener(item: DelegateAdapterItem) {
        findNavController().navigate(
            R.id.action_listFragment_to_detailsFragment,
            Bundle().apply {
                putInt(DetailsFragment.ARG_ID, item.id!!)
            }
        )
    }
}