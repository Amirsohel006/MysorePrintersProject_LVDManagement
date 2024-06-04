package com.example.mysoreprintersproject_lvdmanagement.homecontainer

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mysoreprintersproject_lvdmanagement.R
import com.example.mysoreprintersproject_lvdmanagement.attendance_fragment.AttendanceFragment
import com.example.mysoreprintersproject_lvdmanagement.home_fragment.HomeFragment
import com.example.mysoreprintersproject_lvdmanagement.lvdfragment.LVDFragment
import com.example.mysoreprintersproject_lvdmanagement.profilefragment.Profile_Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeContainerActivity : AppCompatActivity() {


    private lateinit var frameBottomBar: BottomNavigationView

    private lateinit var drawerLayout: DrawerLayout

  //  private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_container)

       // drawerLayout=findViewById(R.id.drawerlayout)

//        navigationView=findViewById(R.id.navigationView)
//
//        navigationView.itemIconTintList=null


        replaceFragment(HomeFragment())
        frameBottomBar = findViewById(R.id.frameBottombar)


//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.navHostFagment) as NavHostFragment?
//
//        val navController = navHostFragment!!.navController
//
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            Log.d("NavController", "Navigated to ${destination.label}")
//        }
//
//
//        NavigationUI.setupWithNavController(navigationView,navController)
//
//
//        NavigationUI.setupWithNavController(frameBottomBar, navController)

        frameBottomBar.setOnNavigationItemSelectedListener {it ->
            when(it.itemId){

                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.attendance -> {
                    replaceFragment(AttendanceFragment())
                    true
                }
//
                R.id.report -> {
                    replaceFragment(LVDFragment())
                    true
                }
//
                R.id.account -> {
                    replaceFragment(Profile_Fragment())
                    true
                }

                else -> {
                    TODO("Hello")
                }
            }

        }
    }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Check if the fragment is already in the back stack
        val existingFragment = fragmentManager.findFragmentByTag(fragment.javaClass.simpleName)

        if (existingFragment == null) {
            fragmentTransaction.replace(R.id.navHostFagment, fragment, fragment.javaClass.simpleName)
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            fragmentTransaction.commit()
        } else {
            // If the fragment already exists, simply pop the back stack up to it
            fragmentManager.popBackStackImmediate(existingFragment.javaClass.simpleName, 0)
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        if (fragmentManager.backStackEntryCount == 1) {
            showExitDialog()
        } else {
            if (fragmentManager.backStackEntryCount > 1) {
                fragmentManager.popBackStackImmediate(
                    fragmentManager.getBackStackEntryAt(1).id,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )

                var selectedFragment: Fragment? = null
                val fragments = supportFragmentManager.fragments
                for (fragment in fragments) {
                    if (fragment != null && fragment.isVisible) {
                        selectedFragment = fragment
                        break
                    }
                }

                selectedFragment?.let {
                    when (it) {
                        is HomeFragment -> frameBottomBar.selectedItemId = R.id.home
//                        is AttendanceFragment -> frameBottomBar.selectedItemId = R.id.attendance
//                        is ReportFragment -> frameBottomBar.selectedItemId = R.id.report
//                        is ProfileFragment -> frameBottomBar.selectedItemId = R.id.account
                    }
                } ?: super.onBackPressed()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> finish() })
            .setNegativeButton("No", null)
            .show()
    }

}