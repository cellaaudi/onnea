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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (!intent.hasExtra(FROM_REG)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
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

        binding?.btnLogin?.setOnClickListener {
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

    companion object{
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 1001

        var FROM_REG = "from_reg"
    }
}