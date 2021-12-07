package com.example.camera.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.camera.PhotoViewModel
import com.example.camera.databinding.FiltersFragmentBinding
import com.example.camera.databinding.MenuFragmentBinding
import com.example.camera.fragments.listeners.FiltersFragmentListener
import com.example.camera.fragments.listeners.MenuFragmentListener
import com.example.camera.recyclerviews.FiltersAdapter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FilterFragment : Fragment() {

    private var selectItem: ImageView? = null

    private val viewModel: PhotoViewModel by viewModels()

    private val filtersAdapter = FiltersAdapter()

    private var _binding: FiltersFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: FiltersFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is FiltersFragmentListener) {
            listener = parentFragment as FiltersFragmentListener
        }
    }

    private val subscribeFilters by lazy {
        viewModel.bitmapsChannel.onEach {

        }
    }

    private fun pickBitmap(bitMap: Bitmap) {
        listener?.pickFilteredBitmap(bitMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FiltersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}