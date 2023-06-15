package com.cellaaudi.onnea.ui.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.adapter.SearchFoodAdapter
import com.cellaaudi.onnea.databinding.FragmentDiscoverBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DiscoverFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showLoading(false)

        viewModel.load.observe(requireActivity()) {
            showLoading(it)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.load.observe(requireActivity()) {
                    showLoading(it)
                }

                binding.viewFlipper.displayedChild = binding.viewFlipper.indexOfChild(binding.layoutSearch)

                viewModel.search(query.toString())
                binding.rvFood.setHasFixedSize(true)
                val lm = LinearLayoutManager(requireContext())
                binding.rvFood.layoutManager = lm
                viewModel.search.observe(requireActivity()) { query ->
                    val listFood = SearchFoodAdapter(query.results)
                    binding.rvFood.adapter = listFood
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.viewFlipper.displayedChild = binding.viewFlipper.indexOfChild(binding.scDiscover)

                    return false
                }

                return false
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbSearchFood.visibility = View.VISIBLE
        } else {
            binding.pbSearchFood.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiscoverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}