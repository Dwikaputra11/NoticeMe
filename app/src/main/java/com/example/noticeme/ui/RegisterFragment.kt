@file:Suppress("DeferredResultUnused")

package com.example.noticeme.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentRegisterBinding
import com.example.noticeme.model.User
import com.example.noticeme.model.UserViewModel
import com.example.noticeme.sharedpref.SharedPref
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

private const val TAG = "Register Fragment"
@Suppress("unused", "unused", "unused", "unused", "unused")
class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private lateinit var auth: FirebaseAuth

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
        auth = FirebaseAuth.getInstance()
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
//            addToDatabase(username, password, fullName)
            addToFirebase(username, password)
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
                val isExist =
                    withContext(Dispatchers.Default) {
                        findUsername(username)
                    }
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
        val waitFor =
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val isExist = userVM.countUser(username) > 0
                isExist
            }
        Log.d(TAG, "findUsername: outer $waitFor")
        return waitFor
    }

    private fun addToFirebase(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG, "addToFirebase: Success ${it.result.user}")
                }else{
                    Log.d(TAG, "addToFirebase: Unsuccessfully")
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "addToFirebase Failed: ${it.localizedMessage}")
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
        Log.d(TAG, "addToDatabase: Finish")
    }

    private fun toastMessage(msg: String){
        activity?.runOnUiThread {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

}