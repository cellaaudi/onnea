package com.cellaaudi.onnea.ui.addfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityAddFoodBinding
import com.cellaaudi.onnea.databinding.ActivityAddFoodDetailBinding
import com.cellaaudi.onnea.ui.fooddetail.FoodDetailActivity
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

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        foodId = intent.getIntExtra(FOOD_ID, 1)
        day = intent.getIntExtra(AddFoodActivity.DAY, 1)
        month = intent.getIntExtra(AddFoodActivity.MONTH, 1)
        type = intent.getStringExtra(AddFoodActivity.TYPE).toString()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getDetail(foodId)

        viewModel.detail.observe(this) { food ->
            binding?.imgFoodDetailAdd?.let {
                Glide.with(this)
                    .load(food.image)
                    .into(it)
            }
            binding?.txtTitleAdd?.text = food.title
            link = food.image
//            calories = food.ca
            imagetype = food.imageType
        }

        binding?.btnAddFood?.setOnClickListener {
//            if (uid != null) {
//                viewModel.changeFood(
//                    uid,
//                    foodId.toString(),
//                    day.toString(),
//                    month.toString(),
//                    binding?.txtTitleAdd?.text.toString(),
//                    /// ambil dari text
//                )
//            }
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