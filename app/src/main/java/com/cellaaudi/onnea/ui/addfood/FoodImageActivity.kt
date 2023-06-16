package com.cellaaudi.onnea.ui.addfood

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cellaaudi.onnea.databinding.ActivityFoodImageBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FoodImageActivity : AppCompatActivity() {

    private var _binding: ActivityFoodImageBinding? = null
    private val binding get() = _binding

    private val viewModel: FoodImageViewModel by viewModels()

    private var day: Int = 0
    private var month: Int = 0
    private var type: String = ""
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoodImageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        binding?.txtFoodName?.visibility = View.GONE

        viewModel.load.observe(this) {
            showLoading(it)
        }

        day = intent.getIntExtra(DAY, 1)
        month = intent.getIntExtra(MONTH, 1)
        type = intent.getStringExtra(TYPE).toString()

        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        if (imageUri != null) {
            binding?.imgAddFood?.setImageURI(imageUri)

            val imagePath = getRealPathFromUri(imageUri)

            if (imagePath != null) {
                val file = File(imagePath)
                val requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val imageMultipart = MultipartBody.Part.createFormData("file", file.name, requestBody)

                viewModel.foodRecognition(imageMultipart)
                viewModel.name.observe(this) { name ->
                    binding?.txtFoodName?.visibility = View.VISIBLE
                    binding?.txtFoodName?.text = name.toString()
                }
            }
        }

        binding?.btnAddFoodImg?.setOnClickListener {
            if (viewModel.load.value == false) {
                val intent = Intent(this, AddFoodActivity::class.java)
                intent.putExtra(AddFoodActivity.FOOD_PRED, binding?.txtFoodName?.text)
                intent.putExtra(AddFoodActivity.DAY, day)
                intent.putExtra(AddFoodActivity.MONTH, month)
                intent.putExtra(AddFoodActivity.TYPE, type)
                startActivity(intent)
            }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        var filePath: String? = null
        val scheme = uri.scheme
        if (scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = it.getString(columnIndex)
                }
                it.close()
            }
        } else if (scheme == "file") {
            filePath = uri.path
        }
        return filePath
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pbImg?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        var DAY = "day"
        var MONTH = "month"
        var TYPE = "type"
    }
}