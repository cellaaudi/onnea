package com.cellaaudi.onnea.ui.cateringdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cellaaudi.onnea.databinding.ActivityCateringDetailBinding


class CateringDetailActivity : AppCompatActivity() {

    private var _binding: ActivityCateringDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: CateringDetailViewModel by viewModels()

    private var wazap = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCateringDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val id = intent.getIntExtra(CAT_ID, 1)

        showLoading()

        viewModel.getCatering(id)
        viewModel.data.observe(this) { cat ->
            binding?.txtCatName?.text = cat.name
            binding?.txtCatLoc?.text = cat.address
            binding?.txtCatPrice?.text = "Rp ${cat.price},-"
            val menu = cat.menu
            val menuEnd = menu.replace("\\n", "\n").replace("\\", "")
            binding?.txtCatMenu?.text = menuEnd
            val tnc = cat.tnC
            val tncEnd = tnc.replace("\\n", "\n").replace("\\", "")
            binding?.txtCatTNC?.text = tncEnd
            wazap = cat.phone
        }

        binding?.btnWazap?.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$wazap"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            binding?.pbCatDetail?.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    companion object {
        var CAT_ID = "cat_id"
    }
}