package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spyfall.R
import com.example.spyfall.ui.listener.DrawerListener
import kotlinx.coroutines.cancel

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {

    abstract val TAG: String

    private var drawerListener: DrawerListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        if (requireActivity() is DrawerListener) {
            drawerListener = requireActivity() as DrawerListener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        val buttonDrawer = view.findViewById<ImageView>(R.id.menu)

        buttonDrawer.setOnClickListener {
            drawerListener?.setDrawer()
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        drawerListener?.closeDrawer()
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.cancel()
        Log.d(TAG, "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}