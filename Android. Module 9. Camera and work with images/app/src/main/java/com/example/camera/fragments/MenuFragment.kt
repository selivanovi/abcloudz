package com.example.camera.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.camera.Filter
import com.example.camera.ImagePickerCamera
import com.example.camera.ImagePickerFiles
import com.example.camera.R
import com.example.camera.databinding.MenuFragmentBinding
import com.example.camera.fragments.dialogs.PhotoDialogFragment
import com.example.camera.fragments.listeners.*
import com.example.camera.viewmodels.DrawableViewModel
import kotlinx.android.synthetic.main.menu_fragment.*

class MenuFragment : Fragment(), PhotoProviderFragmentListener, DrawableFragmentListener,
    FiltersFragmentListener, EmojisFragmentListener {

    private val viewModel by activityViewModels<DrawableViewModel>()

    private var _binding: MenuFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: MenuFragmentListener? = null

    private val getImageFromCamera by lazy {
        ImagePickerCamera(
            requireActivity().activityResultRegistry,
            this
        ) {
            viewModel.emitImageUri(it)
        }
    }

    private val getImageFromFiles: ImagePickerFiles by lazy {
        ImagePickerFiles(
            requireActivity().activityResultRegistry,
            this
        ) {
            viewModel.emitImageUri(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "Parent: ${requireParentFragment().requireParentFragment()}")
        if (requireParentFragment().requireParentFragment() is MenuFragmentListener) {
            Log.d(TAG, "onAttach")
            listener = requireParentFragment().requireParentFragment() as MenuFragmentListener
        }
        getImageFromFiles
        getImageFromCamera
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
            findNavController().navigate(R.id.action_menuFragment_to_emojisFragment)
        }
        drawLineButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_drawableFragment)
        }
        savingButton.setOnClickListener {
            //
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

    override fun pickColor(color: Int) {
        listener?.setColor(color)
    }

    override fun pickSize(size: Float) {
        //todo
    }

    override fun addEmojis(idDrawable: Int) {
        listener?.addEmoji(idDrawable)
    }

    override fun pickFilteredBitmap(filter: Filter) {
        //
    }

    companion object {
        private const val TAG = "MenuFragment"
    }
}