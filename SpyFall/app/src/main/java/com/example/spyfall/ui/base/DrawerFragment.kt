package com.example.spyfall.ui.base

import android.content.Context
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.example.spyfall.R
import com.example.spyfall.ui.listener.DrawerListener

abstract class DrawerFragment<VB : ViewBinding, VM : BaseViewModel>(
    inflate: Inflate<VB>
) : BaseFragment<VB, VM>(inflate) {

    private var drawerListener: DrawerListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireActivity() is DrawerListener) {
            drawerListener = requireActivity() as DrawerListener
        }
    }

    override fun setupView() {
        super.setupView()

        val buttonDrawer = requireView().findViewById<ImageView>(R.id.menu)

        buttonDrawer.setOnClickListener {
            drawerListener?.setDrawer()
        }
    }

    override fun onStart() {
        super.onStart()
        drawerListener?.closeDrawer()
    }

    override fun onDetach() {
        super.onDetach()
        drawerListener = null
    }


}