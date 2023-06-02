package com.cellaaudi.onnea.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cellaaudi.onnea.databinding.FragmentQ8Binding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Q8Fragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQ8Binding? = null
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
        _binding = FragmentQ8Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnQ8.setOnClickListener {
//            val q9Fragment = Q9Fragment()
//
//            val fm = parentFragmentManager
//            fm.beginTransaction().apply {
//                replace(R.id.frame_container, q9Fragment, Q9Fragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Q8Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}