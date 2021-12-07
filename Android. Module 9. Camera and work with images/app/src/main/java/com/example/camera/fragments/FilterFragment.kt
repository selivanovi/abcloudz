package com.example.camera.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camera.Filter
import com.example.camera.databinding.FiltersFragmentBinding
import com.example.camera.filters
import com.example.camera.fragments.listeners.FiltersFragmentListener
import com.example.camera.recyclerviews.FiltersAdapter

class FilterFragment : Fragment() {

    private var selectItem: ImageView? = null


    private val filtersAdapter = FiltersAdapter(::pickBitmap, null)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = requireArguments()
        val bitmap = arguments.getParcelable<Bitmap>(BITMAP_ARG)!!
        filtersAdapter.bitmap = bitmap
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
        binding.emojisRecyclerView.adapter = filtersAdapter.apply { setData(filters) }
    }

    private fun pickBitmap(filter: Filter) {
        listener?.pickFilteredBitmap(filter)
    }

    companion object {
        const val BITMAP_ARG = "bitmapArg"
    }
}