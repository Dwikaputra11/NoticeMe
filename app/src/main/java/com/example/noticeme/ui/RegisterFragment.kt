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

class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private lateinit var usernameList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = activity?.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)!!
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
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
        val confirmPassword = binding.etConfPasswordRegist.text.toString()

        if(isInputValid(username, password, confirmPassword)){
            addToDatabase(username, password, fullName)
            Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_loginFragment)
        }else{
            toastMessage("There is a problem, please try again!")
        }
    }

    private fun isInputValid(
        username: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        usernameList = userVM.getAllUsername()
        return if (password == confirmPassword){
            if(usernameList.contains(username)){
                if(!username.contains(" ")){
                    true
                }else{
                    toastMessage("Username should not contain whitespace!")
                    false
                }
            }else{
                toastMessage("Username Already Exist")
                false
            }
        }else{
            toastMessage("Password Confirmation not match with Password")
            false
        }
    }

    private fun addToDatabase(username: String, password: String, fullName: String) {
        val user = User(
            id = 0,
            username = username,
            password = password,
            fullname = fullName
        )
        userVM.addUser(user)
        toastMessage("Horay! Welcome to the club!")
    }
    private fun addToSharedPref(username: String, password: String, fullName: String, userId: Int) {
        val addData = sharedPref.edit()
        Log.d("Register", "Username: $username")
        Log.d("Register", "Password: $password")
        Log.d("Register", "Full Name: $fullName")
        Log.d("Register", "User Id: $userId")
        addData.putString(SharedPref.fullName, fullName)
        addData.putString(SharedPref.username, username)
        addData.putString(SharedPref.password, password)
        addData.putInt(SharedPref.userId, userId)
        addData.apply()
    }

    private fun toastMessage(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

}