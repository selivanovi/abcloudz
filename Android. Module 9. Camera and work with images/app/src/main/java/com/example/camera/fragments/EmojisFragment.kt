package com.example.camera.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camera.databinding.EmojisFragmentBinding
import com.example.camera.fragments.listeners.PhotoFragmentListener
import com.example.camera.recyclerviews.EmojisAdapter
import com.example.camera.stickerList
import com.example.camera.viewmodels.DrawableViewModel

class EmojisFragment : Fragment() {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private val adapter = EmojisAdapter(::addEmoji)

    private var _binding: EmojisFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: PhotoFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireParentFragment().requireParentFragment() is PhotoFragmentListener) {
            listener = requireParentFragment().requireParentFragment() as PhotoFragmentListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EmojisFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setListener()
    }

    private fun setListener() {
        binding.doneImageView.setOnClickListener {
            findNavController().popBackStack()
            viewModel.emitAddable(false)
        }
        binding.undoImageView.setOnClickListener {
            listener?.removeEmoji()
        }
    }

    private fun setRecyclerView() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.emojisRecyclerView.adapter = adapter
        binding.emojisRecyclerView.layoutManager = layoutManager
        adapter.setData(stickerList)
    }

    private fun addEmoji(idDrawable: Int) {
        viewModel.emitEmoji(idDrawable)
    }
}