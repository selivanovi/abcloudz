package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.listener.CreateGameListener
import com.example.spyfall.ui.fragment.listener.NavigationListener

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId){

    abstract val TAG: String

    private var navigationListener: NavigationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        if (requireActivity() is NavigationListener) {
            navigationListener = requireActivity() as NavigationListener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        val buttonDrawer = view.findViewById<ImageView>(R.id.menu)

        buttonDrawer.setOnClickListener {
            openDrawer()
        }

//        viewLifecycleOwnerLiveData.observe(requireActivity()) {
//            Log.d(TAG, it.lifecycle.currentState.toString())
//        }
    }

    fun openDrawer() {
        navigationListener?.openDrawer()
    }

    fun back() {
        navigationListener?.back()
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
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
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
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