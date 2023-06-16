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
import com.cellaaudi.onnea.ui.addfood.AddFoodDetailViewModel
import com.cellaaudi.onnea.ui.register.RegisterViewModel

class FoodDetailActivity : AppCompatActivity() {

    private var _binding: ActivityFoodDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: AddFoodDetailViewModel by viewModels()

    private var calories = ""
    private var protein = ""
    private var carbohydrates = ""
    private var fat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val foodId = intent.getIntExtra(FOOD_ID, 1)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getDetail(foodId)
        viewModel.getNutrition(foodId)

        viewModel.detail.observe(this) { food ->
            binding?.imgFoodDetail?.let {
                Glide.with(this)
                    .load(food.image)
                    .into(it)
            }
            binding?.txtTitle?.text = food.title
            binding?.txtReadyDetail?.text = "Ready in ${food.readyInMinutes} minutes"
        }

        viewModel.nutrition.observe(this) { nutrition ->
            calories = nutrition.calories
            carbohydrates = nutrition.carbs.removeSuffix("g")
            protein = nutrition.protein.removeSuffix("g")
            fat = nutrition.fat.removeSuffix("g")

            binding?.txtCalDetail?.text = "$calories kcal"
            binding?.ttxtCarbDetail?.text = "$carbohydrates g"
            binding?.ttxtProtDetail?.text = "$protein g"
            binding?.txtFatDetail?.text = "$fat g"
        }

        viewModel.msgNut.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pbDetail?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        var FOOD_ID = "food_id"
    }
}