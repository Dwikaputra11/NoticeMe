package com.example.noticeme.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentSplashScreenBinding
import com.example.noticeme.sharedpref.SharedPref

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private lateinit var binding:FragmentSplashScreenBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val splashTime = 3000L
        sharedPref = activity?.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)!!
        Handler().postDelayed({
            detectAccount()
        },splashTime)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun detectAccount(){
        val username = sharedPref.getString(SharedPref.username, "")
        if (username != null) {
            if(username.isBlank()){
                Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
            }else{
                val bundle = Bundle()
                bundle.putString(SharedPref.username, username)
                Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment_to_homeFragment, bundle)
            }
        }
    }

}