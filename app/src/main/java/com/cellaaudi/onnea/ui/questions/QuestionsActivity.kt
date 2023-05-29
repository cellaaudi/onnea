package com.cellaaudi.onnea.ui.questions

import com.cellaaudi.onnea.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cellaaudi.onnea.databinding.ActivityQuestionsBinding

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val fm = supportFragmentManager
        val q1Fragment = Q1Fragment()
        val fragment = fm.findFragmentByTag(Q1Fragment::class.java.simpleName)

        if (fragment !is Q1Fragment) {
            fm
                .beginTransaction()
                .add(R.id.frame_container, q1Fragment, Q1Fragment::class.java.simpleName)
                .commit()
        }
    }
}