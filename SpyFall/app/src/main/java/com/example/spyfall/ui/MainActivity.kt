package com.example.spyfall.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import com.example.spyfall.R
import com.example.spyfall.ui.dialog.QuiteDialog
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawerLayout)
    }

    private val navigationView by lazy {
        findViewById<NavigationView>(R.id.navigation_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createDrawerLayout()

        createNavigationView()


    }

    private fun createDrawerLayout() {

        val content = findViewById<CardView>(R.id.cardContainer)

        drawerLayout.setScrimColor(Color.TRANSPARENT);
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

        navigationView.getHeaderView(0).findViewById<ImageView>(R.id.closeImageView)
            .setOnClickListener {
                drawerLayout.closeDrawer(navigationView)
            }

        navigationView.setNavigationItemSelectedListener {

            navigationView.menu.forEach { item -> item.isChecked = false }

            it.isChecked = true

            when (it.itemId) {
                R.id.itemCreateGame -> {
                }
                R.id.itemAbout -> {
                }
                R.id.itemRules -> {
                }
                R.id.itemGoOut -> QuiteDialog().show(supportFragmentManager, null)
            }

            true
        }
    }

    /**
    Replace with callback
     */
    fun moveDrawableLayout() {
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