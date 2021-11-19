package com.example.localdatastorage.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localdatastorage.R
import com.example.localdatastorage.databinding.FragmentListBinding
import com.example.localdatastorage.model.room.DonutDataBase
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.recyclerviews.ListAdapter
import com.example.localdatastorage.viewmodels.ListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.fragment_list) {

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private val listAdapter = ListAdapter()

    private val dataBase: DonutDataBase by lazy {
        DonutDataBase.getInstance(requireContext())
    }

    private val repository: DonutsRepository by lazy {
        DonutsRepository(dataBase)
    }

    private val listViewModel: ListViewModel by viewModels<ListViewModel> {
        ListViewModel.Factory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerView()


        lifecycleScope.launch {
            listViewModel.getDonutsUI().collect {
                listAdapter.setData(it)
            }
        }
    }

    private fun createRecyclerView() {
        binding.donutsRecyclerView.adapter = listAdapter
        listAdapter.onLongClickListener = ::openEditDialogFragment
        binding.donutsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun openEditDialogFragment(id: Int) {

    }
}