package com.cellaaudi.onnea.ui.catering

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentCateringBinding
import com.cellaaudi.onnea.databinding.FragmentHomeBinding
import com.cellaaudi.onnea.model.CateringResponse
import com.cellaaudi.onnea.ui.cateringdetail.CateringDetailActivity
import com.cellaaudi.onnea.ui.cateringdetail.CateringDetailViewModel
import com.cellaaudi.onnea.ui.home.HomeViewModel

class CateringFragment : Fragment() {

    private var _binding: FragmentCateringBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CateringDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCateringBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getCatering(1)
        viewModel.data.observe(requireActivity()) { data ->
            binding?.txtCatName1?.text = data.name
            binding?.txtCatLoc1?.text = data.address
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cardCat1?.setOnClickListener {
            val intent = Intent(requireContext(), CateringDetailActivity::class.java)
            intent.putExtra(CateringDetailActivity.CAT_ID, 1)
            startActivity(intent)
        }
    }
}