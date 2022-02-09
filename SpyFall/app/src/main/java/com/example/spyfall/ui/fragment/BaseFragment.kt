package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
import com.example.spyfall.ui.listener.DrawerListener
import com.example.spyfall.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.*

abstract class BaseFragment<VM : BaseViewModel>(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {


    private var drawerListener: DrawerListener? = null

    abstract val viewModel: VM

    open fun onBackPressed() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireActivity() is DrawerListener) {
            drawerListener = requireActivity() as DrawerListener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonDrawer = view.findViewById<ImageView>(R.id.menu)

        buttonDrawer.setOnClickListener {
            drawerListener?.setDrawer()
        }
    }



    override fun onStart() {
        super.onStart()
        drawerListener?.closeDrawer()
    }

    override fun onStop() {
        super.onStop()

        lifecycleScope.coroutineContext.cancelChildren()
        viewModel.clear()
    }

    override fun onDetach() {
        super.onDetach()
        drawerListener = null
    }


}