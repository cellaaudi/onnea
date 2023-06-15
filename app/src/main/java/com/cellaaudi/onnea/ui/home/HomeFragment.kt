package com.cellaaudi.onnea.ui.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.cellaaudi.onnea.databinding.FragmentHomeBinding
import com.cellaaudi.onnea.ui.addfood.AddFoodActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private var uid: String? = null

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd MMMM yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid

        binding.txtDay.text = dateFormat.format(getToday())
        binding.btnNext.visibility = View.INVISIBLE

        viewModel.date.observe(viewLifecycleOwner) { selDate ->
            binding.txtDay.text = dateFormat.format(selDate)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid?.let { getRecommendation(it, binding.txtDay.text.toString()) }

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

            val picker = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(selYear, selMonth, selDay)

                    viewModel.setDate(calendar.time)
                    binding.txtDay.text = dateFormat.format(calendar.time)
                },
                year, month, day
            )

            picker.datePicker.maxDate = System.currentTimeMillis()
            picker.show()
        }

        binding.txtDay.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                showLoadingR()
                uid?.let { getRecommendation(it, s.toString()) }
            }
        })

        binding.btnAddBreakfast.setOnClickListener {
            val intent = Intent(requireContext(), AddFoodActivity::class.java)
            startActivity(intent)
        }
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

    private fun getDateMonthNum(displayedDay: String): Pair<Int, Int> {
        val displayed = dateFormat.parse(displayedDay)
        val cal = Calendar.getInstance()
        cal.time = displayed

        val date = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH) + 1

        return Pair(date, month)
    }

    private fun changeDay(displayedDay: String, button: String): Date {
        val displayed = getDisplayedDay(displayedDay)
        val cal = Calendar.getInstance()
        cal.time = displayed

        val value = if (button == "prev") -1 else 1
        cal.add(Calendar.DAY_OF_MONTH, value)

        viewModel.setDate(cal.time)

        return cal.time
    }

    private fun showLoadingR() {
        viewModel.isLoading.observe(requireActivity()) { load ->
            binding.pbBR.visibility = if (load) View.VISIBLE else View.GONE
            binding.pbLR.visibility = if (load) View.VISIBLE else View.GONE
            binding.pbDR.visibility = if (load) View.VISIBLE else View.GONE

            binding.btnAddBreakfastR.isClickable = !load
            binding.btnAddBreakfastR.isEnabled = !load
            binding.btnAddLunchR.isClickable = !load
            binding.btnAddLunchR.isEnabled = !load
            binding.btnAddDinnerR.isClickable = !load
            binding.btnAddDinnerR.isEnabled = !load
        }
    }

    private fun getRecommendation(id: String, date: String) {
        val (day, month) = getDateMonthNum(date)

        showLoadingR()

        viewModel.getRecommendation(id, day.toString(), month.toString())

        viewModel.food.observe(viewLifecycleOwner) { recommendation ->
            if (recommendation.breakfast != null) {
                Glide.with(requireContext())
                    .load(recommendation.breakfast[2].link)
                    .into(binding.imgBreakR)
                binding.txtBreakNameR.text = recommendation.breakfast[1].name
                binding.txtBreakCalR.text = "${round(recommendation.breakfast[4].calories.toDouble()).toInt()} kcal"
            }

            if (recommendation.lunch != null) {
                Glide.with(requireContext())
                    .load(recommendation.lunch[2].link)
                    .into(binding.imgLunchR)
                binding.txtLunchNameR.text = recommendation.lunch[1].name
                binding.txtLunchCalR.text = "${round(recommendation.lunch[4].calories.toDouble()).toInt()} kcal"
            }

            if (recommendation.dinner != null) {
                Glide.with(requireContext())
                    .load(recommendation.dinner[2].link)
                    .into(binding.imgDinnerR)
                binding.txtDinnerNameR.text = recommendation.dinner[1].name
                binding.txtDinnerCalR.text = "${round(recommendation.dinner[4].calories.toDouble()).toInt()} kcal"
            }
        }

        viewModel.recMsg.observe(requireActivity()) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }
}