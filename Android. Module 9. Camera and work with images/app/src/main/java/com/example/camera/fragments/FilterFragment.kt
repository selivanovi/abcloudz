package com.example.camera.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camera.Filter
import com.example.camera.databinding.FiltersFragmentBinding
import com.example.camera.filters
import com.example.camera.fragments.listeners.FiltersFragmentListener
import com.example.camera.recyclerviews.FiltersAdapter
import com.example.camera.viewmodels.DrawableViewModel
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class FilterFragment : Fragment() {

    private var selectItem: ImageView? = null

    private val viewModel by activityViewModels<DrawableViewModel>()

    private val filtersAdapter = FiltersAdapter(::pickBitmap, null)

    private var _binding: FiltersFragmentBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.emojisRecyclerView.layoutManager = linearLayoutManager
        binding.emojisRecyclerView.adapter = filtersAdapter.apply { setData(viewModel.getFilters()) }
    }

    private fun pickBitmap(filter: GPUImageFilter) {
//        listener?.pickFilteredBitmap(filter)
    }

    companion object {
        const val BITMAP_ARG = "bitmapArg"
    }
}