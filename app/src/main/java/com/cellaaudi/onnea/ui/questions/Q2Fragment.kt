package com.cellaaudi.onnea.ui.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentQ2Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Q2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQ2Binding? = null
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
        _binding = FragmentQ2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rdoGroup.setOnCheckedChangeListener { group, checkedId ->
            var gender: String = ""
            if (checkedId == R.id.rdoMale) {
                gender = "male"
            } else if (checkedId == R.id.rdoFemale) {
                gender = "female"
            }

            binding.btnQ2.text = "Yes, I'm a $gender"
            binding.btnQ2.isClickable = true
            binding.btnQ2.isEnabled = true
        }

        binding.btnQ2.setOnClickListener {
            val q3Fragment = Q3Fragment()

            val fm = parentFragmentManager
            fm?.beginTransaction()?.apply {
                replace(R.id.frame_container, q3Fragment, Q3Fragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Q2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}