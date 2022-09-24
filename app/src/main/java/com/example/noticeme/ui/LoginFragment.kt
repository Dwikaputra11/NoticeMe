package com.example.noticeme.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentLoginBinding
import com.example.noticeme.model.User
import com.example.noticeme.model.UserViewModel
import com.example.noticeme.sharedpref.SharedPref

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = activity?.getSharedPreferences(SharedPref.name,Context.MODE_PRIVATE)!!
        userVM = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsernameLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            if(isExist(username)){
                if(isPasswordCorrect(password)){
                    // if auth success username will be store in shared pref so
                    // when user open the app, it will goes to home page immediately
                    val edit = sharedPref.edit()
                    edit.putString(SharedPref.username, username)
                    edit.apply()
                    val bundle = Bundle()
                    bundle.putString(SharedPref.username, username)
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment, bundle)
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
        val usernameSharedPref = sharedPref.getString(SharedPref.username, "")
        if (usernameSharedPref != null) {
            return if(usernameSharedPref.isBlank()){
                false
            }else{
                if(usernameSharedPref == username){
                    true
                }else{
                    findInDatabase(username)
                }
            }
        }
        return false
    }

    private fun findInDatabase(username: String): Boolean {
        user = userVM.getUser(username).value
        return user != null
    }

    private fun isPasswordCorrect(password:String):Boolean{
        return if(user != null){
            if(user?.password != null)
                user?.password == password
            else false
        }else false
    }

}