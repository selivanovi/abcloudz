package com.example.fragments

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {


    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

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
        toolbar.setupWithNavController(navController, appBarConfiguration)

        viewModel.liveData.observe(this, {

            Log.d("MainActivity", "Change toolbar")
            toolbar.title = it
        }
        )
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()


}
