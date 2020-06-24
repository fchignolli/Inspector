package com.example.inspector.Controller

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.inspector.R
import com.google.android.material.navigation.NavigationView.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var auth: FirebaseAuth
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_my_reports,
                R.id.nav_profile,
                R.id.nav_change_password,
                R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        auth = FirebaseAuth.getInstance()
        setupUI(auth.currentUser)
        setupNavigationHeader(auth.currentUser)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        when(item.itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.nav_home)
            }
            R.id.nav_my_reports -> {
                navController.navigate(R.id.nav_my_reports)
            }
            R.id.nav_profile -> {
                navController.navigate(R.id.nav_profile)
            }
            R.id.nav_change_password -> {
                navController.navigate(R.id.nav_change_password)
            }
            R.id.nav_logout -> {
               logout()
            }
        }
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setupNavigationHeader(currentUser: FirebaseUser?) {
        val headerView = navView.getHeaderView(0)
        val name = headerView.findViewById(R.id.userNameTextView) as TextView
        val email = headerView.findViewById(R.id.userEmailTextView) as TextView
        val profileImage = headerView.findViewById(R.id.userProfileImageView) as ImageView

        name.text = currentUser?.displayName
        email.text = currentUser?.email
        Glide.with(this)
            .load(currentUser?.photoUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transform(CircleCrop())
            .into(profileImage)
    }

    fun logout() {
        // TODO: Colocar um dialog
        auth.signOut()
        setupUI(auth.currentUser)
    }

    fun setupUI(currentUser: FirebaseUser?) {
        if(auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }



}
