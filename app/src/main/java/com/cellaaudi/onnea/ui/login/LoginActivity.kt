package com.cellaaudi.onnea.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cellaaudi.onnea.MainActivity
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityLoginBinding
import com.cellaaudi.onnea.ui.questions.QuestionsActivity
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

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnLogin2Register?.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.sign_in))
        progressDialog.setMessage(getString(R.string.please_wait))

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding?.btnLogin?.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            //Menangani Process Login Google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                // Jika Berhasil Login
                val account = task.getResult(ApiException::class.java)!!
//                viewModel.doesUserExist.observe(this) { existing ->
                    firebaseAuthWithGoogle(account.idToken!!)
//                }
//                Log.e(TAG, "Lo")
            }catch (e: ApiException){
                e.printStackTrace()
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        progressDialog.show()
        val credentian = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credentian)
            .addOnSuccessListener {
//                if (exist) {
                    startActivity(Intent(this, MainActivity::class.java))
//                } else {
//                    startActivity(Intent(this, QuestionsActivity::class.java))
//                }
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
    }

    companion object{
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 1001
    }
}