package com.example.camera.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camera.databinding.EmojisFragmentBinding
import com.example.camera.fragments.listeners.EmojisFragmentListener
import com.example.camera.recyclerviews.EmojisAdapter
import com.example.camera.stickerList
import com.example.camera.viewmodels.DrawableViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EmojisFragment : Fragment() {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private val adapter = EmojisAdapter(::addEmoji)

    private var _binding: EmojisFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: EmojisFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is EmojisFragmentListener) {
            listener = parentFragment as EmojisFragmentListener
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
        lifecycleScope.launch {
            viewModel.channelImageURI.collectLatest {
                //
            }
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.emojisRecyclerView.adapter = adapter
        binding.emojisRecyclerView.layoutManager = layoutManager
        adapter.setData(stickerList)
    }

    private fun addEmoji(idDrawable: Int){
        listener?.addEmojis(idDrawable)
    }

}