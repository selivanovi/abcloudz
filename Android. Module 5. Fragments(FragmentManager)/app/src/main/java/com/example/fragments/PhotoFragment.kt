package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    // private val viewModel: MainViewModel by lazy {
    //     ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    // }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // viewModel.changeTitle("Photo")
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ScreenSlidePagerAdapter(requireActivity())

        super.onViewCreated(view, savedInstanceState)
    }

    private inner class ScreenSlidePagerAdapter(activity: FragmentActivity) :
        FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            val fragment = ScreenSlidePageFragment()
            fragment.arguments = Bundle().apply {
                this.putString(ScreenSlidePageFragment.ARG_STRING, (position + 1).toString())
            }
            return fragment
        }

    }

    companion object {
        private const val NUM_PAGES = 5
    }
}