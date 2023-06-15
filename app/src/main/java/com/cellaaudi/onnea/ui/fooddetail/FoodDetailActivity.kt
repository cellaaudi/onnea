package com.cellaaudi.onnea.ui.fooddetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityFoodDetailBinding
import com.cellaaudi.onnea.databinding.ActivityRegisterBinding
import com.cellaaudi.onnea.ui.register.RegisterViewModel

class FoodDetailActivity : AppCompatActivity() {

    private var _binding: ActivityFoodDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: FoodDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    companion object {
        var FOOD_ID = "food_id"
    }
}