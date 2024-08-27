package com.example.mysoreprintersproject_lvdmanagement.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.example.mysoreprintersproject_lvdmanagement.network.RemoteDataSource
import com.example.mysoreprintersproject_lvdmanagement.R
import com.example.mysoreprintersproject_lvdmanagement.homecontainer.HomeContainerActivity
import com.example.mysoreprintersproject_lvdmanagement.network.AuthApi
import com.example.mysoreprintersproject_lvdmanagement.network.SessionManager
import com.example.mysoreprintersproject_lvdmanagement.network.ViewModelFactory
import com.example.mysoreprintersproject_lvdmanagement.repository.AuthRepository
import com.example.mysoreprintersproject_lvdmanagement.repository.UserPreferences

class SplashScreeenFragment : Fragment(R.layout.fragment_splash_screeen) {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator1: ImageView
    private lateinit var indicator2: ImageView
    private lateinit var nextButton: ImageButton

    private lateinit var sessionManager: SessionManager

    protected val remoteDateSource=  RemoteDataSource()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sessionManager=SessionManager(requireActivity())

        // Check if the user is already logged in
        if (sessionManager.fetchAuthToken() != null) {
            // User is logged in, navigate directly to HomeContainerActivity
            navigateToHome()
            return
        }

        viewPager = requireView().findViewById(R.id.viewPager)
        indicator1 = requireView().findViewById(R.id.indicator1)
        indicator2 = requireView().findViewById(R.id.indicator2)
        nextButton = requireView().findViewById(R.id.nextButton)



        val api = remoteDateSource.buildApi(AuthApi::class.java)
        val preferences = UserPreferences(requireActivity())
        val authRepository = AuthRepository(api,preferences)
        val factory = ViewModelFactory(authRepository)
        val adapter = ViewPagerAdapter(requireActivity(), factory)

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        nextButton.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }

        // Automatically move to the second page after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }, 2000)
    }



    private fun updateIndicators(position: Int) {
        when (position) {
            0 -> {
                indicator1.setImageResource(R.drawable.indicator_selected)
                indicator2.setImageResource(R.drawable.indicator_unselected)
            }
            1 -> {
                indicator1.setImageResource(R.drawable.indicator_unselected)
                indicator2.setImageResource(R.drawable.indicator_selected)
            }
        }
    }


    private fun navigateToHome() {
        val intent = Intent(requireActivity(), HomeContainerActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}