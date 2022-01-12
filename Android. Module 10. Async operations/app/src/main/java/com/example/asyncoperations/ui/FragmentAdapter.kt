package com.example.asyncoperations.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asyncoperations.ui.screens.EpisodesFragment
import com.example.asyncoperations.ui.screens.CharactersFragment

class FragmentAdapter(context: FragmentActivity) : FragmentStateAdapter(context) {

    override fun getItemCount(): Int =
        NUM_PAGES

    override fun createFragment(position: Int): Fragment {

        Log.d("FragmentAdapter", "createFragment: $position")

        when(position) {
            0 -> return CharactersFragment()
            1 -> return EpisodesFragment()
        }
        throw NullPointerException()
    }

    companion object {
        private const val NUM_PAGES = 2
    }
}