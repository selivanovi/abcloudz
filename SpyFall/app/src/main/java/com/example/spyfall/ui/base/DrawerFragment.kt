package com.example.spyfall.ui.base

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.example.spyfall.R
import com.example.spyfall.ui.listener.DrawerListener
import com.example.spyfall.utils.FragmentNotAttachedException
import com.example.spyfall.utils.Inflate

abstract class DrawerFragment<VB : ViewBinding, VM : BaseViewModel>(
    inflate: Inflate<VB>
) : BaseFragment<VB, VM>(inflate) {

    private var drawerListener: DrawerListener? = null

    private var buttonDrawer: View? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireActivity() is DrawerListener) {
            drawerListener = requireActivity() as DrawerListener
        }
        else {
            throw FragmentNotAttachedException("DrawerFragment")
        }
    }

    override fun setupView() {
        super.setupView()

        buttonDrawer = setButtonDrawer()

        buttonDrawer?.setOnClickListener {
            drawerListener?.setDrawer()
        }
    }

    abstract fun setButtonDrawer() : View

    override fun onStart() {
        super.onStart()
        drawerListener?.closeDrawer()
    }

    override fun onDetach() {
        super.onDetach()
        drawerListener = null
    }
}
