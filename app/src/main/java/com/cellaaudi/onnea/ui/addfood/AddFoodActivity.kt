package com.cellaaudi.onnea.ui.addfood

import android.Manifest
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
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cellaaudi.onnea.MainActivity
import com.cellaaudi.onnea.adapter.AddFoodAdapter
import com.cellaaudi.onnea.adapter.SearchFoodAdapter
import com.cellaaudi.onnea.databinding.ActivityAddFoodBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddFoodActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var _binding: ActivityAddFoodBinding? = null
    private val binding get() = _binding

    private val viewModel: AddFoodViewModel by viewModels()

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var context: Context

    private var day: Int = 0
    private var month: Int = 0
    private var type: String = ""
    private var foodImg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        context = this
        supportActionBar?.hide()

        day = intent.getIntExtra(DAY, 1)
        month = intent.getIntExtra(MONTH, 1)
        type = intent.getStringExtra(TYPE).toString()

        foodImg = intent.getStringExtra(FOOD_PRED).toString() ?: null

        if (foodImg != "null") {
            binding?.searchView?.setQuery(foodImg, true)
        }

        showLoading(false)

        viewModel.load.observe(this) {
            showLoading(it)
        }

        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.load.observe(this@AddFoodActivity) {
                    showLoading(it)
                }

                binding?.viewFlipper?.displayedChild = binding?.viewFlipper!!.indexOfChild(binding?.layoutSearch)

                viewModel.search(query.toString())
                binding?.rvFood?.setHasFixedSize(true)
                val lm = LinearLayoutManager(this@AddFoodActivity)
                binding?.rvFood?.layoutManager = lm
                viewModel.search.observe(this@AddFoodActivity) { query ->
                    val listFood = AddFoodAdapter(query.results, day, month, type)
                    binding?.rvFood?.adapter = listFood
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding?.viewFlipper?.displayedChild = binding?.viewFlipper!!.indexOfChild(binding?.scDiscover)

                    return false
                }

                return false
            }
        })

        binding?.btnCamera?.setOnClickListener {
            startCamera()
        }

        binding?.btnGallery?.setOnClickListener {
            startGallery()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == CAMERA_PERMISSION) {
            startCamera()
        } else if (requestCode == STORAGE_PERMISSION) {
            startGallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasCameraPermission() = EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)

    private fun hasStoragePermission() = EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)

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
                val uri = Uri.fromFile(getFile)

                val intent = Intent(this, FoodImageActivity::class.java)
                intent.putExtra("imageUri", uri.toString())
                intent.putExtra(FoodImageActivity.DAY, day)
                intent.putExtra(FoodImageActivity.MONTH, month)
                intent.putExtra(FoodImageActivity.TYPE, type)
                startActivity(intent)
            }
        }
    }

    private fun startCamera() {
        if (hasCameraPermission()) {
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
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Onnea needs permission to access camera",
                CAMERA_PERMISSION,
                Manifest.permission.CAMERA
            )
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
            intent.putExtra(FoodImageActivity.DAY, day)
            intent.putExtra(FoodImageActivity.MONTH, month)
            intent.putExtra(FoodImageActivity.TYPE, type)
            startActivity(intent)
        }
    }

    private fun startGallery() {
//        if (hasStoragePermission()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
//        } else {
//            EasyPermissions.requestPermissions(
//                this,
//                "Onnea needs permission to access gallery",
//                STORAGE_PERMISSION,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//        }
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

    private fun showLoading(isLoading: Boolean) {
        binding?.pbSearchFood?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val CAMERA_PERMISSION = 1
        private const val  STORAGE_PERMISSION = 2

        var DAY = "day"
        var MONTH = "month"
        var TYPE = "type"

        var FOOD_PRED = "food_pred"
    }
}