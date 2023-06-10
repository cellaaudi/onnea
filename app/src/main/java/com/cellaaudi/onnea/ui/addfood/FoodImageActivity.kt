package com.cellaaudi.onnea.ui.addfood

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityFoodImageBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FoodImageActivity : AppCompatActivity() {

    private var _binding: ActivityFoodImageBinding? = null
    private val binding get() = _binding

    private val viewModel: FoodImageViewModel by viewModels()

    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoodImageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        binding?.txtFoodName?.visibility = View.GONE

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
}