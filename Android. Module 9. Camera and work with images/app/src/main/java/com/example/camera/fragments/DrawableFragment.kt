package com.example.camera.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.camera.R
import com.example.camera.databinding.DrawableFragmentBinding
import com.example.camera.fragments.listeners.DrawableFragmentListener
import com.example.camera.recyclerviews.ColorsAdapter
import com.example.camera.viewmodels.DrawableViewModel

class DrawableFragment : Fragment() {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private val adapter = ColorsAdapter(::pickColor)

    private var _binding: DrawableFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DrawableFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun pickColor(color: Int) {
        Log.d(TAG, "pickColor")
        viewModel.emitDrawable(true)
        viewModel.emitColor(color)
    }

    private fun setRecyclerView() {
        val colors = resources.getIntArray(R.array.rainbow).toList()
        val linearLayout =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = LinearSnapHelper()
        adapter.setData(colors)
        binding.colorPickerRecyclerView.adapter = adapter
        binding.colorPickerRecyclerView.layoutManager = linearLayout
        snapHelper.attachToRecyclerView(binding.colorPickerRecyclerView)
    }

    companion object {
        private const val TAG = "DrawableFragment"

    }
}