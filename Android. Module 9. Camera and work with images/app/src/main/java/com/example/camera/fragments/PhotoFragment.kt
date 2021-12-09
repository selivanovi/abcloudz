package com.example.camera.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.camera.activities.CameraActivity
import com.example.camera.databinding.PhotoFragmentBinding
import com.example.camera.fragments.listeners.PhotoFragmentListener
import com.example.camera.viewmodels.DrawableViewModel

class PhotoFragment : Fragment(), PhotoFragmentListener {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private var _binding: PhotoFragmentBinding? = null
    private val binding
        get() = _binding!!

    private val observeAddable by lazy {
        viewModel.channelAddable.observe(this, {
            binding.imageView.isAddableStickers = it
        })
    }

    private val observeImageUri by lazy {
        viewModel.channelImageBitmap.observe(this, {
            binding.imageView.setImageBitmap(it)
        })
    }

    private val observeColor by lazy {
        viewModel.channelColor.observe(this, {
            binding.imageView.setColor(it)
        })
    }

    private val observeDrawable by lazy {
        viewModel.channelDrawable.observe(this, {
            binding.imageView.isDrawable = it
        })
    }

    private val observeEmoji by lazy {
        viewModel.channelEmoji.observe(this, {
            binding.imageView.addEmoji(it)
        })
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
        observeImageUri
        observeColor
        observeEmoji
        observeDrawable
        observeAddable
    }

    override fun clear() {
        binding.imageView.clear()
    }

    override fun save() {
        val bitmap = binding.imageView.getSavedBitmap()
        viewModel.saveBitmapToStorage(bitmap)
    }

    override fun removeDraw() {
        binding.imageView.removeDraw()
    }

    override fun removeEmoji() {
        binding.imageView.removeEmoji()
    }

}