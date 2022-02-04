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
import com.example.spyfall.ui.listener.NavigationListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")

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
        lifecycleScope.cancel()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}