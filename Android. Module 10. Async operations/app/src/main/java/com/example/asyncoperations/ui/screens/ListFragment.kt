package com.example.asyncoperations.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asyncoperations.R
import com.example.asyncoperations.ui.recyclerviews.characters.adapters.CharacterAdapter
import com.example.asyncoperations.ui.viewmodels.CharacterViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ListFragment : Fragment(R.layout.fragment_list) {

    private val characterViewModel by viewModels<CharacterViewModel> {
        CharacterViewModel.Factory(requireContext())
    }

    private val characterAdapter = CharacterAdapter()

    private val characterObserver by lazy {
        characterViewModel.channelCharacters.onEach {
            characterAdapter.setData(it)
        }.launchIn(lifecycleScope)
    }

    private val errorObserver by lazy {
        characterViewModel.channelError.onEach {
            showErrorAlertDialog(it)
        }.launchIn(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListFragment", "onViewCreated")
        characterObserver
        errorObserver
        createRecyclerView(view)
    }

    private fun showErrorAlertDialog(it: Throwable) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_error_alert_dialog)
            .setMessage(it.message)
            .setPositiveButton(R.string.ok) {_,_ ->
            }
        builder.create().show()
    }

    private fun createRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.characterRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = characterAdapter
    }
}