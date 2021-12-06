package com.example.camera.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.camera.R
import com.example.camera.databinding.MenuFragmentBinding
import com.example.camera.fragments.listeners.MenuFragmentListener
import kotlinx.android.synthetic.main.menu_fragment.*

class MenuFragment : Fragment() {

    private var selectItem: ImageView? = null

    private var _binding: MenuFragmentBinding? = null
    private val binding
        get() = _binding!!

    private var listener: MenuFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is MenuFragmentListener) {
            listener = parentFragment as MenuFragmentListener
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
        setListeners()
    }

    private fun setListeners() {
        takePhotoButton.setOnClickListener {
            selectItem(it)
            listener?.showPhotoDialog()
        }
        addFilterButton.setOnClickListener {
            selectItem(it)
        }
        addEmojiButton.setOnClickListener {
            selectItem(it)
            listener?.showEmojiDialog()
        }
        drawLineButton.setOnClickListener {
            selectItem(it)
            listener?.showDrawableFragment()
        }
    }

    private fun selectItem(view: View) {
        selectItem?.setColorFilter(Color.WHITE)
        selectItem = view as ImageView
        selectItem?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow_dark))
    }
}