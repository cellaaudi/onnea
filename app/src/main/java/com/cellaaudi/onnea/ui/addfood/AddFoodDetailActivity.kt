package com.cellaaudi.onnea.ui.addfood

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.cellaaudi.onnea.databinding.ActivityAddFoodDetailBinding
import com.google.firebase.auth.FirebaseAuth

class AddFoodDetailActivity : AppCompatActivity() {

    private var _binding: ActivityAddFoodDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: AddFoodDetailViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    private var foodId = 0
    private var day = 0
    private var month = 0
    private var type = ""

    private var link = ""
    private var calories = ""
    private var imagetype = ""
    private var protein = ""
    private var carbohydrates = ""
    private var fat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        foodId = intent.getIntExtra(FOOD_ID, 1)
        day = intent.getIntExtra(DAY, 1)
        month = intent.getIntExtra(MONTH, 1)
        type = intent.getStringExtra(TYPE).toString()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getDetail(foodId)
        viewModel.getNutrition(foodId)

        viewModel.detail.observe(this) { food ->
            binding?.imgFoodDetailAdd?.let {
                Glide.with(this)
                    .load(food.image)
                    .into(it)
            }
            binding?.txtTitleAdd?.text = food.title
            binding?.txtReadyAdd?.text = "Ready in ${food.readyInMinutes} minutes"
            link = food.image
            imagetype = food.imageType
        }

        viewModel.nutrition.observe(this) { nutrition ->
            calories = nutrition.calories
            carbohydrates = nutrition.carbs.removeSuffix("g")
            protein = nutrition.protein.removeSuffix("g")
            fat = nutrition.fat.removeSuffix("g")

            binding?.txtCal?.text = "$calories kcal"
            binding?.txtCarb?.text = "$carbohydrates g"
            binding?.txtProt?.text = "$protein g"
            binding?.txtFat?.text = "$fat g"
        }

        viewModel.msgNut.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        binding?.btnAddFood?.setOnClickListener {
            if (uid != null) {
                viewModel.changeFood(
                    uid,
                    foodId.toString(),
                    day.toString(),
                    month.toString(),
                    binding?.txtTitleAdd?.text.toString(),
                    link, type, calories, imagetype, protein, carbohydrates, fat
                )

                viewModel.add.observe(this) {
                    if (it) finish() else Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pbDetailAdd?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        val FOOD_ID = "food_id"

        var DAY = "day"
        var MONTH = "month"
        var TYPE = "type"
    }
}