package com.cellaaudi.onnea.ui.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentQ6Binding
import com.cellaaudi.onnea.databinding.FragmentQ7Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Q7Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQ7Binding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentQ7Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cbListener = CompoundButton.OnCheckedChangeListener { checkBox, isChecked ->
            val isChecked =
                binding.cbEgg.isChecked ||
                        binding.cbGluten.isChecked ||
                        binding.cbGarin.isChecked ||
                        binding.cbPeanut.isChecked ||
                        binding.cbSeafood.isChecked ||
                        binding.cbSesame.isChecked ||
                        binding.cbShellfish.isChecked ||
                        binding.cbSoy.isChecked ||
                        binding.cbSulfite.isChecked ||
                        binding.cbTreeNut.isChecked ||
                        binding.cbWheat.isChecked ||
                        binding.cbDairy.isChecked

            if (isChecked) {
                binding.btnQ7.text = resources.getString(R.string.q_yes_allergy)
            } else {
                binding.btnQ7.text = resources.getString(R.string.q_no_allergy)
            }
        }

        binding.cbEgg.setOnCheckedChangeListener(cbListener)
        binding.cbGluten.setOnCheckedChangeListener(cbListener)
        binding.cbGarin.setOnCheckedChangeListener(cbListener)
        binding.cbPeanut.setOnCheckedChangeListener(cbListener)
        binding.cbSeafood.setOnCheckedChangeListener(cbListener)
        binding.cbSesame.setOnCheckedChangeListener(cbListener)
        binding.cbShellfish.setOnCheckedChangeListener(cbListener)
        binding.cbSoy.setOnCheckedChangeListener(cbListener)
        binding.cbSulfite.setOnCheckedChangeListener(cbListener)
        binding.cbTreeNut.setOnCheckedChangeListener(cbListener)
        binding.cbWheat.setOnCheckedChangeListener(cbListener)
        binding.cbDairy.setOnCheckedChangeListener(cbListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Q7Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}