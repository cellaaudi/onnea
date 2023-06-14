package com.cellaaudi.onnea.ui.questions

import com.cellaaudi.onnea.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.cellaaudi.onnea.databinding.ActivityLoginBinding
import com.cellaaudi.onnea.databinding.ActivityQuestionsBinding
import com.cellaaudi.onnea.ui.login.LoginViewModel

class QuestionsActivity : AppCompatActivity() {

    private var _binding: ActivityQuestionsBinding? = null
    private val binding get() = _binding

    private lateinit var answerViewModel: AnswerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        answerViewModel = ViewModelProvider(this).get(AnswerViewModel::class.java)

        val fm = supportFragmentManager
        val nameFragment = NameFragment()
        val fragment = fm.findFragmentByTag(NameFragment::class.java.simpleName)

        if (fragment !is NameFragment) {
            fm
                .beginTransaction()
                .add(R.id.frame_container, nameFragment, NameFragment::class.java.simpleName)
                .commit()
        }
    }
}