package com.cellaaudi.onnea.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cellaaudi.onnea.MainActivity
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityLoginBinding
import com.cellaaudi.onnea.ui.questions.QuestionsActivity
import com.cellaaudi.onnea.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

//    private lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var progressDialog: ProgressDialog
//    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        binding?.pbLogin?.visibility = View.GONE

        binding?.btnLogin2Register?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        progressDialog = ProgressDialog(this)
//        progressDialog.setTitle(getString(R.string.sign_in))
//        progressDialog.setMessage(getString(R.string.please_wait))
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding?.btnLogin?.setOnClickListener {
//            val intent = googleSignInClient.signInIntent
//            startActivityForResult(intent, RC_SIGN_IN)

            viewModel.isLoading.observe(this) {
                showLoading(it)
            }

            val email = binding?.txtLoginEmail?.editText?.text.toString()
            val pass = binding?.txtLoginPass?.editText?.text.toString()

            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid

                    if (uid != null) {
                        viewModel.login(uid)
                    } else {
                        Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.loginMsg.observe(this) { msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

                if (msg.contains("success")) {
                    if (msg.contains("answer")) {
                        val intent = Intent(this, QuestionsActivity::class.java)
                        startActivity(intent)
                    } else if (msg.contains("Welcome")) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding?.pbLogin?.visibility = View.VISIBLE else binding?.pbLogin?.visibility = View.GONE
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN){
//            //Menangani Process Login Google
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try{
//                // Jika Berhasil Login
//                val account = task.getResult(ApiException::class.java)!!
////                viewModel.doesUserExist.observe(this) { existing ->
//                    firebaseAuthWithGoogle(account.idToken!!)
////                }
////                Log.e(TAG, "Lo")
//            }catch (e: ApiException){
//                e.printStackTrace()
//                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private fun firebaseAuthWithGoogle(idToken: String){
//        progressDialog.show()
//        val credentian = GoogleAuthProvider.getCredential(idToken, null)
//        firebaseAuth.signInWithCredential(credentian)
//            .addOnSuccessListener {
////                if (exist) {
//                    startActivity(Intent(this, MainActivity::class.java))
////                } else {
////                    startActivity(Intent(this, QuestionsActivity::class.java))
////                }
//            }
//            .addOnCompleteListener {
//                progressDialog.dismiss()
//            }
//    }

    companion object{
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 1001
    }
}