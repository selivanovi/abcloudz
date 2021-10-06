package com.example.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainFragment : Fragment(R.layout.fragment_main) {

    private val lifecycleObserver: LifecycleObserver = MyLifecycleObserver("MainFragment")
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(lifecycleObserver)
        configureToolbar(view)
        configureBottomNavigationView(view)
        configureNavigationView(view)
    }

    override fun onStart() {
        toolbar.title = bottomNavigationView.menu.findItem(bottomNavigationView.selectedItemId).title
        super.onStart()
    }

    private fun configureBottomNavigationView(view: View) {
        bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    childFragmentManager.commit {
                        replace<HomeFragment>(R.id.mainContainerView)
                        setReorderingAllowed(true)
                    }
                }
                R.id.photoFragment -> {
                    childFragmentManager.commit {
                        replace<PhotoFragment>(R.id.mainContainerView)
                        setReorderingAllowed(true)
                    }
                }
                R.id.mapFragment -> {
                    childFragmentManager.commit {
                        replace<MapFragment>(R.id.mainContainerView)
                        setReorderingAllowed(true)
                    }
                }
            }
            toolbar.title = it.title
            true
        }
    }

    private fun configureToolbar(view: View) {

        drawerLayout = view.findViewById<DrawerLayout>(R.id.drawerLayout)
        toolbar = view.findViewById<Toolbar>(R.id.mainToolbar)

        val toggle =
            ActionBarDrawerToggle(
                requireActivity(),
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun configureNavigationView(view: View) {
        navigationView = view.findViewById<NavigationView>(R.id.navigationView)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsFragment -> {
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SettingsFragment>(R.id.containerView)
                        addToBackStack(null)
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

}