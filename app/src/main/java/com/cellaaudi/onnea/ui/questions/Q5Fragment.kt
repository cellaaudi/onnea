package com.cellaaudi.onnea.ui.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentQ4Binding
import com.cellaaudi.onnea.databinding.FragmentQ5Binding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Q5Fragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQ5Binding? = null
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
        _binding = FragmentQ5Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rdoGoal.setOnCheckedChangeListener { group, checkedId ->
            binding.btnQ5.isEnabled = true
            binding.btnQ5.isClickable = true
        }

        binding.btnQ5.setOnClickListener {
            val q6Fragment = Q6Fragment()

            val fm = parentFragmentManager
            fm.beginTransaction().apply {
                replace(R.id.frame_container, q6Fragment, Q6Fragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Q5Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}