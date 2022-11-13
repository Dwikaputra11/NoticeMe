@file:Suppress("unused", "unused", "unused")

package com.example.noticeme.ui

import android.content.Context
import android.content.Intent
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
import com.example.noticeme.databinding.FragmentLoginBinding
import com.example.noticeme.model.User
import com.example.noticeme.model.UserViewModel
import com.example.noticeme.sharedpref.SharedPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "Login Fragment"
@Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused")
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val reqCode: Int = 123
    private var user: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(SharedPref.name,Context.MODE_PRIVATE)
        userVM = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        binding.btnLogin.setOnClickListener {
//            GlobalScope.async { loginAccount() }
            loginWithFirebase()
        }

        binding.tvRegister.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }



    private fun loginWithFirebase(){
        val username = binding.etUsernameLogin.text.toString().trim()
        val password = binding.etPasswordLogin.text.toString().trim()

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)

//        auth.signInWithEmailAndPassword(username,password)
//            .addOnCompleteListener(requireActivity()) {
//                if(it.isSuccessful){
//                    Log.d(TAG, "loginWithFirebase: Success: ${it.result.user}")
//                }else{
//                    Log.d(TAG, "loginWithFirebase: Failed")
//                }
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "loginWithFirebase: ${it.message}")
//            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == reqCode){
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            resultResponse(task)
        }
    }

    private fun resultResponse(task: Task<GoogleSignInAccount>){
        try{
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if(account != null) updateUI(account)
        }catch (e: ApiException){
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val bundle = Bundle()
                goToHome("dwika")
//                Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment, bundle)
            }
        }
    }

    private suspend fun loginAccount() {
        val username = binding.etUsernameLogin.text.toString().trim()
        val password = binding.etPasswordLogin.text.toString().trim()
        if(isExist(username)){
            // if auth success username will be store in shared pref so
            // when user open the app, it will goes to home page immediately

            // cause login() run in different thread so when we go back to the ui thread it cannot work
            // so we have to declare the that the function we want tu run in ui thread like below
            requireActivity().runOnUiThread {
                addToSharedPref(username, password)
            }
        }else{
            toastMessage("Username Tidak Terdaftar")
        }
    }

    private suspend fun isExist(username: String):Boolean{
        val usernameSharedPref = sharedPref.getString(SharedPref.username, "")
        return if (usernameSharedPref != null) {
            if (usernameSharedPref.isNotBlank()) {
                if (usernameSharedPref == username) {
                    true
                } else {
                    Log.d(TAG, "isExist: Search to database user pref")
                    findInDatabase(username)
                }
            } else {
                Log.d(TAG, "isExist: Search to database user pref null")
                findInDatabase(username)
            }
        }else false
    }

    private suspend fun findInDatabase(username: String): Boolean {
        val status =
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val isExist = userVM.countUser(username) > 0
                Log.d(TAG, "findInDatabase: $isExist")
                isExist
            }
        Log.d(TAG, "findInDatabase: status $status")
        return status
    }
    private fun addToSharedPref(username: String, password: String) {
        userVM.findUser(username).observe(viewLifecycleOwner){ user ->
            val addData = sharedPref.edit()
            Log.d("Register", "Username: ${user.username}")
            Log.d("Register", "Password: ${user.password}")
            Log.d("Register", "Full Name: ${user.fullname}")
            Log.d("Register", "User Id: ${user.id}")
            if(password == user.password){
                addData.putString(SharedPref.fullName, user.fullname)
                addData.putString(SharedPref.username, username)
                addData.putString(SharedPref.password, user.password)
                addData.putInt(SharedPref.userId, user.id)
                // when password is correct go to home page
                goToHome(username)
            }else{
                toastMessage("Password Anda Salah")
            }
            addData.apply()
        }
    }

    private fun goToHome(username: String) {
        val bundle = Bundle()
        bundle.putString(SharedPref.username, username)
        val addData = sharedPref.edit()
        addData.putInt(SharedPref.userId, 1)
        addData.apply()
        toastMessage("Login Successfully")
        Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment, bundle)
    }

    private fun toastMessage(msg:String){
        requireActivity().runOnUiThread {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

}