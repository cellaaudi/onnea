package com.cellaaudi.onnea.ui.questions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentQ3Binding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Q3Fragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQ3Binding? = null
    private val binding get() = _binding!!

    private var weight: Double = 0.0
    private var height: Double = 0.0

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
        _binding = FragmentQ3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbWeight.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                weight = if (input.isNotEmpty()) {
                    input.toDouble()
                } else {
                    0.0
                }

                btnState()
            }

        })

        binding.tbHeight.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                height = if (input.isNotEmpty()) {
                    input.toDouble()
                } else {
                    0.0
                }

                btnState()
            }

        })

        binding.btnQ3.setOnClickListener {
            val q4Fragment = Q4Fragment()

            val fm = parentFragmentManager
            fm?.beginTransaction()?.apply {
                replace(R.id.frame_container, q4Fragment, Q4Fragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun btnState() {
        val state = weight > 0.0 && height > 0.0
        binding.btnQ3.isEnabled = state
        binding.btnQ3.isClickable = state
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Q3Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}