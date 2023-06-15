package com.cellaaudi.onnea.ui.fooddetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
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

        val foodId = intent.getIntExtra(FOOD_ID, 1)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getDetail(foodId)

        viewModel.detail.observe(this) { food ->
            binding?.imgFoodDetail?.let {
                Glide.with(this)
                    .load(food.image)
                    .into(it)
            }
            binding?.txtTitle?.text = food.title
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pbDetail?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        var FOOD_ID = "food_id"
    }
}