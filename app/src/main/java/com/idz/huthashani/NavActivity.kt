package com.idz.huthashani


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.idz.huthashani.firebase.FirebaseModel
import com.idz.huthashani.login.LoginActivity

class NavActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private val firebaseModel: FirebaseModel = FirebaseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.navhost) as NavHostFragment?
        navController = navHostFragment?.navController
        NavigationUI.setupActionBarWithNavController(this, navController!!)
        val navView = findViewById<BottomNavigationView>(R.id.bottomNav)
        NavigationUI.setupWithNavController(navView, navController!!)

    }

    private var fragmentMenuId = 0
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (fragmentMenuId != 0) {
            menu.removeItem(fragmentMenuId)
        }
        fragmentMenuId = 0
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController?.popBackStack()
            }
            R.id.logout  -> {
                firebaseModel.logout()
                sendUserToNextActivity(LoginActivity::class.java)
            }
            else -> {
                fragmentMenuId = item.itemId
                return NavigationUI.onNavDestinationSelected(item, navController!!)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun sendUserToNextActivity(clazz: Class<*>) {
        val intent = Intent(this@NavActivity, clazz)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}