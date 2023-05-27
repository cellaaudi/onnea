package com.cellaaudi.onnea.ui.addfood

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ActivityFoodImageBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FoodImageActivity : AppCompatActivity() {

    private var _binding: ActivityFoodImageBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoodImageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        if (imageBitmap != null) {
            binding?.imgAddFood?.setImageBitmap(imageBitmap)
        } else if (imageUri != null) {
            binding?.imgAddFood?.setImageURI(imageUri)
        }
    }

    private fun createCustomTempFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss",
            Locale.UK
        ).format(System.currentTimeMillis())

        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }
}