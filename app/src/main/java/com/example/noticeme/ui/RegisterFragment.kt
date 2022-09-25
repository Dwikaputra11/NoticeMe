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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentRegisterBinding
import com.example.noticeme.model.User
import com.example.noticeme.model.UserViewModel
import com.example.noticeme.sharedpref.SharedPref
import kotlinx.coroutines.*
import kotlinx.coroutines.runBlocking as runBlocking

class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private val TAG = "Register Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)!!
        userVM = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.btnRegister.setOnClickListener {
            GlobalScope.async { registerAccount() }
        }
        binding.tvLogin.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun registerAccount(){
        val username = binding.etUsernameRegist.text.toString().trim()
        val password = binding.etPasswordRegist.text.toString().trim()
        val fullName = binding.etFullNameRegist.text.toString().trim()
        val confirmPassword = binding.etConfPasswordRegist.text.toString()

        if(isInputValid(username, password, confirmPassword)){
            addToDatabase(username, password, fullName)
            requireActivity().runOnUiThread {
                toastMessage("Horay! Welcome to the club!")
                Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }else{
            toastMessage("There is a problem, please try again!")
        }
    }

    @DelicateCoroutinesApi
    private suspend fun isInputValid(
        username: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        return if (password == confirmPassword){
            if(!username.contains(" ")){
                val isExist = GlobalScope.async { findUsername(username) }.await()
                Log.d(TAG, "isInputValid: $isExist")
                if(!isExist){
                    Log.d(TAG, "isInputValid: Success")
                    true
                }else{
                    toastMessage("Username Already Exist")
                    false
                }
            }else{
                toastMessage("Username should not contain whitespace!")
                false
            }
        }else{
            toastMessage("Password Confirmation not match with Password")
            false
        }
    }

    private suspend fun findUsername(username:String) : Boolean {
        Log.d(TAG, "findUsername: Started")
        val waitFor =  CoroutineScope(Dispatchers.IO).async {
            val isExist = userVM.countUser(username) > 0
            isExist
        }.await()
        Log.d(TAG, "findUsername: outer $waitFor")
        return waitFor
    }

    private fun addToDatabase(username: String, password: String, fullName: String) {
        val user = User(
            id = 0,
            username = username,
            password = password,
            fullname = fullName
        )
        userVM.addUser(user)
        Log.d(TAG, "addToDatabase: Finish")
    }

    private fun toastMessage(msg: String){
        activity?.runOnUiThread {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

}