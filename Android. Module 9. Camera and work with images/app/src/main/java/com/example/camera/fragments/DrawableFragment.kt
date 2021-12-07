package com.example.camera.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.camera.DrawableImageView
import com.example.camera.R
import com.example.camera.databinding.DrawableFragmentBinding
import com.example.camera.fragments.listeners.DrawableFragmentListener
import com.example.camera.recyclerviews.ColorsAdapter

class DrawableFragment : Fragment() {

    private val adapter = ColorsAdapter(::pickColor)

    private var _binding: DrawableFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: DrawableFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "Bitmap: $parentFragment")
        if (parentFragment is DrawableFragmentListener) {
            listener = parentFragment as DrawableFragmentListener
        }
    }

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
        setListeners()
    }

    private fun pickColor(color: Int) {
        Log.d(TAG, "pickColor")
        listener?.pickColor(color)
    }

    private fun setListeners() {

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
        private const val TAG = "DrawableImageView"

    }
}