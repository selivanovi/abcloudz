package com.example.spyfall.ui

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.spyfall.R
import com.example.spyfall.ui.dialog.QuiteDialogListener
import com.example.spyfall.ui.listener.DrawerListener
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DrawerListener, QuiteDialogListener {

    private val drawerLayout: DrawerLayout by lazy {
        findViewById(R.id.drawerLayout)
    }

    private val navController: NavController by lazy {
        val navHost = supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment
        navHost.navController
    }

    private val navigationView: NavigationView by lazy {
        findViewById(R.id.navigation_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate")
        setContentView(R.layout.activity_main)

        createDrawerLayout()

        createNavigationView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d("MainActivity", "onSaveInstanceState")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("MainActivity", "onDestroy")
    }

    private fun createDrawerLayout() {

        val content = findViewById<CardView>(R.id.cardContainer)
        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                val diffScaledOffset: Float = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                content.scaleX = offsetScale
                content.scaleY = offsetScale

                val diffCornerRadius: Float =
                    slideOffset * (1 - resources.getDimension(R.dimen.corner_radius_main_container))
                val offsetRadius = 1 - diffCornerRadius
                content.radius = offsetRadius

                val diffCardElevation: Float =
                    slideOffset * (1 - resources.getDimension(R.dimen.elevation_main_container))
                val offsetElevation = 1 - diffCardElevation
                content.cardElevation = offsetElevation

                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff: Float = content.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                content.translationX = -xTranslation
            }
        })
        drawerLayout.drawerElevation = 0f
    }

    private fun createNavigationView() {


        navigationView.setupWithNavController(navController)

        navigationView.getHeaderView(0).findViewById<ImageView>(R.id.closeImageView)
            .setOnClickListener {
                openDrawer()
            }

//        navigationView.setNavigationItemSelectedListener {
//
//            navigationView.menu.forEach { item -> item.isChecked = false }
//
//            it.isChecked = true
//
//            when (it.itemId) {
//                R.id.itemCreateGame -> {
//                }
//                R.id.itemGoOut -> QuiteDialog().show(supportFragmentManager, null)
//            }
//
//            true
//        }
    }

    override fun logOut() {
        Log.d("MainActivity", "Log Out")

        navController.navigate(R.id.logInFragment)
        openDrawer()
    }

    override fun openDrawer() {
        if (!drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.openDrawer(navigationView)
        } else {
            drawerLayout.closeDrawer(navigationView)
        }
    }

    companion object {
        private const val END_SCALE = 0.7f
    }
}