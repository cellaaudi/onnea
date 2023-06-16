package com.cellaaudi.onnea.ui.cateringdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityAddFoodBinding
import com.cellaaudi.onnea.databinding.ActivityCateringDetailBinding
import com.cellaaudi.onnea.ui.addfood.AddFoodViewModel

class CateringDetailActivity : AppCompatActivity() {

    private var _binding: ActivityCateringDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: CateringDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCateringDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}