package com.cellaaudi.onnea.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityLoginBinding
import com.cellaaudi.onnea.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}