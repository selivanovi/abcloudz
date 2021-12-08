package com.example.camera.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.camera.databinding.PhotoFragmentBinding
import com.example.camera.fragments.listeners.MenuFragmentListener
import com.example.camera.viewmodels.DrawableViewModel
import kotlinx.android.synthetic.main.photo_fragment.view.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PhotoFragment : Fragment() {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private var _binding: PhotoFragmentBinding? = null
    private val binding
        get() = _binding!!

    private val subscribeImageUri by lazy {
        viewModel.channelImageURI.onEach {
            binding.imageView.setImageURI(it)
        }.launchIn(lifecycleScope)
    }

    private val subscribeColor by lazy {
        viewModel.channelColor.onEach {
            binding.imageView.setColor(it)
        }.launchIn(lifecycleScope)
    }

    private val subscribeDrawable by lazy {
        viewModel.channelDrawable.onEach {
            binding.imageView.isDrawable = it
        }.launchIn(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeImageUri
        subscribeColor
        subscribeDrawable
    }

    companion object {
        private const val TAG = "PhotoFragment"
    }
}