package com.cellaaudi.onnea.ui.addfood

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.cellaaudi.onnea.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.cellaaudi.onnea.databinding.ActivityAddFoodBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddFoodActivity : AppCompatActivity() {

    private var _binding: ActivityAddFoodBinding? = null
    private val binding get() = _binding

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        context = this

        binding?.btnCamera?.setOnClickListener {
            startCamera()
        }

        binding?.btnGallery?.setOnClickListener {
            startGallery()
        }
    }

    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                val bitmap = BitmapFactory.decodeFile(file.path)
                val ei = ExifInterface(file.path)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)
                }
                val rotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height,  matrix, true)
                getFile = createTempFileFromBitmap(rotate, context)

                val intent = Intent(this, FoodImageActivity::class.java)
                intent.putExtra("imageBitmap", rotate)
                startActivity(intent)
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val uri: Uri = FileProvider.getUriForFile(
                this@AddFoodActivity,
                "com.cellaaudi.onnea",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            launchIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddFoodActivity)
            }

            val intent = Intent(this@AddFoodActivity, FoodImageActivity::class.java)
            intent.putExtra("imageUri", selectedImg.toString())
            startActivity(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun createCustomTempFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss",
            Locale.UK
        ).format(System.currentTimeMillis())

        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun createTempFileFromBitmap(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "rotated_image.jpg")
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }
}