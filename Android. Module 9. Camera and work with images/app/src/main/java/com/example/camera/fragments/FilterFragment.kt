package com.example.camera.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camera.databinding.FiltersFragmentBinding
import com.example.camera.recyclerviews.FiltersAdapter
import com.example.camera.viewmodels.DrawableViewModel

class FilterFragment : Fragment() {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private val filtersAdapter = FiltersAdapter(::pickBitmap, null)

    private var _binding: FiltersFragmentBinding? = null
    private val binding
        get() = _binding!!

    private val observeImageBitmap by lazy {
        viewModel.channelImageBitmap.observe(this, {
            filtersAdapter.bitmap = it
            filtersAdapter.setData(viewModel.getFilters())
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FiltersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeImageBitmap
        setRecyclerView()
        setListener()
    }

    private fun setListener() {
        binding.doneImageView.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.emojisRecyclerView.layoutManager = linearLayoutManager
        binding.emojisRecyclerView.adapter = filtersAdapter
    }

    private fun pickBitmap(filter: Bitmap) {
        viewModel.emitImageBitmap(filter)
    }

}