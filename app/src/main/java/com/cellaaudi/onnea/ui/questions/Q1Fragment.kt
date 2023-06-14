package com.cellaaudi.onnea.ui.questions

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentQ1Binding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Q1Fragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQ1Binding? = null
    private val binding get() = _binding!!

    val dateFormat = SimpleDateFormat("dd MMMM yyyy")

    private var age: Int = 0

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
        _binding = FragmentQ1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tbBirthDate.isCursorVisible = false

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(AnswerViewModel::class.java)

        binding.tbBirthDate.setOnClickListener {
            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DAY_OF_MONTH)

            var picker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(selYear, selMonth, selDay)

                    binding.tbBirthDate.setText(dateFormat.format((calendar.time)))
                },
                year,
                month,
                day
            )

            picker.show()
        }

        binding.tbBirthDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim()

                if (text.isEmpty()) {
                    binding.btnQ1.isClickable = false
                    binding.btnQ1.isEnabled = false
                } else {
                    val calBday = Calendar.getInstance()
                    val parsedDate = dateFormat.parse(binding.tbBirthDate.text.toString())
                    calBday.time = parsedDate
                    val year = calBday.get(Calendar.YEAR)
                    val month = calBday.get(Calendar.MONTH) + 1
                    val day = calBday.get(Calendar.DAY_OF_MONTH)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        age = Period.between(
                            LocalDate.of(year, month, day),
                            LocalDate.now()
                        ).years

                        binding.btnQ1.text = "Yes, I'm $age years old"
                    }

                    binding.btnQ1.isClickable = true
                    binding.btnQ1.isEnabled = true
                }
            }

        })

        binding.btnQ1.setOnClickListener {
            viewModel.birthdate = age.toString()

            val q2Fragment = Q2Fragment()

            val fm = parentFragmentManager
            fm?.beginTransaction()?.apply {
                replace(R.id.frame_container, q2Fragment, Q2Fragment::class.java.simpleName)
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
        fun newInstance() =
            Q1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}