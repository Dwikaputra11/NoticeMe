package com.example.noticeme.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentLoginBinding
import com.example.noticeme.sharedpref.SharedPref

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = activity?.getSharedPreferences(SharedPref.name,Context.MODE_PRIVATE)!!
        val usernamePref = sharedPref.getString(SharedPref.username, "")
        Log.d("Login", "Username: $usernamePref")

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsernameLogin.text.toString()
            val password = binding.etPassswordLogin.text.toString()
            if(isExist(username)){
                if(isPasswordCorrect(password)){
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment)
                }else{
                    Toast.makeText(context, "Password Anda Salah", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Username Tidak Terdaftar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun isExist(username: String):Boolean{
        val usernameSharedPref = sharedPref.getString("username", "")
        if (usernameSharedPref != null) {
            if(usernameSharedPref.isBlank()){
                return false
            }else{
                if(usernameSharedPref == username){
                    return true
                }
            }
        }
        return false
    }

    private fun isPasswordCorrect(password:String):Boolean{
        val passwordSharedPref = sharedPref.getString("password", "")
        if (passwordSharedPref != null){
            if(passwordSharedPref.isBlank()){
                return false
            }else{
                if(passwordSharedPref == password){
                    return true
                }
            }
        }
        return false
    }

}