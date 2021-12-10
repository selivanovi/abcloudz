package com.example.camera.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.camera.ImagePickerCamera
import com.example.camera.ImagePickerFiles
import com.example.camera.R
import com.example.camera.activities.CameraActivity
import com.example.camera.databinding.MenuFragmentBinding
import com.example.camera.fragments.dialogs.PhotoDialogFragment
import com.example.camera.fragments.listeners.PhotoFragmentListener
import com.example.camera.fragments.listeners.PhotoProviderFragmentListener
import com.example.camera.viewmodels.DrawableViewModel
import kotlinx.android.synthetic.main.menu_fragment.*

class MenuFragment : Fragment(), PhotoProviderFragmentListener {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private var _binding: MenuFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: PhotoFragmentListener? = null

    private val observeImageUri by lazy {
        viewModel.channelImageBitmap.observe(this, {
            if (it == null)
                setVisibleForButton(false)
            else
                setVisibleForButton(true)
        })
    }

    private val getImageFromCamera by lazy {
        ImagePickerCamera(
            requireActivity().activityResultRegistry,
            this
        ) {
            val bitmap = viewModel.getBitmapFromUri(it!!)
            viewModel.emitImageBitmap(bitmap)
            listener?.clear()
        }
    }

    private val getImageFromFiles: ImagePickerFiles by lazy {
        ImagePickerFiles(
            requireActivity().activityResultRegistry,
            this
        ) {
            val bitmap = viewModel.getBitmapFromUri(it!!)
            viewModel.emitImageBitmap(bitmap)
            listener?.clear()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "Parent: ${requireParentFragment().requireParentFragment()}")
        if (requireParentFragment().requireParentFragment() is PhotoFragmentListener) {
            Log.d(TAG, "onAttach")
            listener = requireParentFragment().requireParentFragment() as PhotoFragmentListener
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getImageFromFiles
        getImageFromCamera
        observeImageUri
        setListeners()
    }

    private fun setListeners() {
        takePhotoButton.setOnClickListener {
            PhotoDialogFragment().show(childFragmentManager, null)
        }
        addFilterButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_filterFragment)
        }
        addEmojiButton.setOnClickListener {
            viewModel.emitAddable(true)
            findNavController().navigate(R.id.action_menuFragment_to_emojisFragment)
        }
        drawLineButton.setOnClickListener {
            viewModel.emitDrawable(true)
            findNavController().navigate(R.id.action_menuFragment_to_drawableFragment)
        }
        savingButton.setOnClickListener {
            if (allPermissionsGranted()) {
                listener?.save()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    override fun onClickCamera() {
        Log.d(TAG, "onClickCamera")
        getImageFromCamera.pickImage()
    }

    override fun onClickFiles() {
        Log.d(TAG, "onClickFiles")
        getImageFromFiles.pickImage()
    }

    private fun setVisibleForButton(isVisible: Boolean) {
        with(binding) {
            drawLineButton.isVisible = isVisible
            addEmojiButton.isVisible = isVisible
            addFilterButton.isVisible = isVisible
            savingButton.isVisible = isVisible
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS && allPermissionsGranted()) {
            listener?.save()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "MenuFragment"
        private const val REQUEST_CODE_PERMISSIONS = 8
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}