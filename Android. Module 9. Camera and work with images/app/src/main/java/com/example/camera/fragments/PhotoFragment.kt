package com.example.camera.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.camera.ImagePickerCamera
import com.example.camera.ImagePickerFiles
import com.example.camera.PhotoFragmentListener
import com.example.camera.R
import com.example.camera.activities.contracts.CameraContract
import com.example.camera.databinding.PhotoFragmentBinding
import com.example.camera.fragments.dialogs.PhotoDialogFragment
import com.example.camera.fragments.listeners.DrawableFragmentListener
import com.example.camera.fragments.listeners.EmojisFragmentListener
import com.example.camera.fragments.listeners.MenuFragmentListener
import kotlinx.android.synthetic.main.photo_fragment.*

class PhotoFragment : Fragment(), PhotoFragmentListener, MenuFragmentListener,
    DrawableFragmentListener, EmojisFragmentListener {

    private var _binding: PhotoFragmentBinding? = null
    private val binding
        get() = _binding!!

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
        childFragmentManager.beginTransaction()
            .add(R.id.photoFragmentContainer, MenuFragment())
            .commit()
        setListeners()
    }

    private fun setListeners() {
        binding.undoImageView.setOnClickListener {
            imageView.removeDrawable()
        }
        binding.doneImageView.setOnClickListener {
            binding.undoImageView.isVisible = false
            binding.doneImageView.isVisible = false
            imageView.isDrawable = false
            imageView.isAddableStickers = false
            childFragmentManager.beginTransaction()
                .replace(R.id.photoFragmentContainer, MenuFragment())
                .commit()
        }
    }

    private val getImageFromCamera by lazy {
        ImagePickerCamera(
            requireActivity().activityResultRegistry,
            this
        ) { binding.imageView.setImageURI(it) }
    }

    private val getImageFromFiles: ImagePickerFiles by lazy {
        ImagePickerFiles(
            requireActivity().activityResultRegistry,
            this
        ) { binding.imageView.setImageURI(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getImageFromCamera
        getImageFromFiles
    }

    override fun onClickCamera() {
        Log.d(TAG, "onClickCamera")
        getImageFromCamera.pickImage()
    }

    override fun onClickFiles() {
        Log.d(TAG, "onClickFiles")
        getImageFromFiles.pickImage()
    }

    companion object {
        private const val TAG = "PhotoFragment"
    }

    override fun showPhotoDialog() {
        PhotoDialogFragment().show(childFragmentManager, null)
    }

    override fun showFilterFragment() {
        TODO("Not yet implemented")
    }

    override fun showEmojiDialog() {
        childFragmentManager.beginTransaction()
            .replace(R.id.photoFragmentContainer, EmojisFragment()).commit()
        imageView.isAddableStickers = true
        binding.undoImageView.isVisible = true
        binding.doneImageView.isVisible = true
    }

    override fun showDrawableFragment() {
        imageView.isDrawable = true
        childFragmentManager.beginTransaction()
            .replace(R.id.photoFragmentContainer, DrawableFragment()).commit()

        binding.undoImageView.isVisible = true
        binding.doneImageView.isVisible = true
    }

    override fun saveBitmap() {

    }

    override fun pickColor(color: Int) {
        imageView.setColor(color)
    }

    override fun pickSize(size: Float) {
        TODO("Not yet implemented")
    }

    override fun addEmojis(idDrawable: Int) {
        imageView.addEmoji(idDrawable)
    }
}