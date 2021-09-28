package com.example.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val navigationView: NavigationView by lazy {
        findViewById(R.id.navigationView)
    }

    private val drawerLayout: DrawerLayout by lazy {
        findViewById(R.id.drawerLayout)
    }

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment)
            .navController
    }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph, drawerLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
}