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
import com.example.noticeme.databinding.FragmentRegisterBinding
import com.example.noticeme.sharedpref.SharedPref

class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = activity?.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)!!
        binding.btnRegister.setOnClickListener {
            registerAccount()
        }
        binding.tvLogin.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun registerAccount(){
        val username = binding.etUsernameRegist.text.toString()
        val password = binding.etPasswordRegist.text.toString()
        val fullName = binding.etFullNameRegist.text.toString()
        val confirmPassword = binding.etConfPassRegist.text.toString()
        val usernameExist = sharedPref.getString(SharedPref.username,"")
        if (password == confirmPassword){
            if(username != usernameExist){
                addToSharedPref(username, password, fullName)
                toastMessage("Horay! Welcome to the club!")
                Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_homeFragment)
            }else{
                toastMessage("Username Already Exist")
            }
        }else{
            toastMessage("Password Confirmation not match with Password")
        }
    }

    private fun addToSharedPref(username: String, password: String, fullName: String) {
        val addData = sharedPref.edit()
        Log.d("Register", "Username: $username")
        Log.d("Register", "Password: $password")
        Log.d("Register", "Full Name: $fullName")
        addData.putString(SharedPref.fullName, fullName)
        addData.putString(SharedPref.username, username)
        addData.putString(SharedPref.password, password)
        addData.apply()
    }

    private fun toastMessage(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

}