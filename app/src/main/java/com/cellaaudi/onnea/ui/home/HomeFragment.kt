package com.cellaaudi.onnea.ui.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentHomeBinding
import com.cellaaudi.onnea.ui.addfood.AddFoodActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd MMMM yyyy")

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.txtDay.text = dateFormat.format(getToday())
        binding.btnNext.visibility = View.INVISIBLE

        binding.btnPrev.setOnClickListener {
            val button = "prev"
            val newDay = changeDay(binding.txtDay.text.toString(), button)
            binding.txtDay.text = dateFormat.format(newDay)
        }

        binding.btnNext.setOnClickListener {
            val button = "next"
            val newDay = changeDay(binding.txtDay.text.toString(), button)
            binding.txtDay.text = dateFormat.format(newDay)
        }

        binding.txtDay.addTextChangedListener {
            val today = dateFormat.format(getToday())
            val displayedDay = dateFormat.format(getDisplayedDay(binding.txtDay.text.toString()))

            if (today.equals(displayedDay)) {
                binding.btnNext.visibility = View.INVISIBLE
            } else {
                binding.btnNext.visibility = View.VISIBLE
            }
        }

        binding.txtDay.setOnClickListener {
            val displayedDay = getDisplayedDay(binding.txtDay.text.toString())
            val cal = Calendar.getInstance()
            cal.time = displayedDay
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            var picker = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(selYear, selMonth, selDay)

                    binding.txtDay.text = dateFormat.format(calendar.time)
                },
                year,
                month,
                day
            )

            picker.datePicker.maxDate = System.currentTimeMillis()
            picker.show()
        }

        binding.btnAddBreakfast.setOnClickListener {
            val intent = Intent(requireContext(), AddFoodActivity::class.java)
            startActivity(intent)
        }

//        val textView: TextView = binding.textView
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getToday(): Date {
        val cal = Calendar.getInstance()

        return cal.time
    }

    private fun getDisplayedDay(displayedDay: String): Date {
        val parsedDate = dateFormat.parse(displayedDay)
        val cal = Calendar.getInstance()
        if (parsedDate != null) {
            cal.time = parsedDate
        }

        return cal.time
    }

    private fun changeDay(displayedDay: String, button: String): Date {
        val displayed = getDisplayedDay(displayedDay)
        val cal = Calendar.getInstance()
        cal.time = displayed

        val value = if (button == "prev") -1 else 1
        cal.add(Calendar.DAY_OF_MONTH, value)

        return cal.time
    }
}