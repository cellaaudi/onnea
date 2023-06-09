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
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.FragmentHomeBinding
import com.cellaaudi.onnea.ui.addfood.AddFoodActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

//    var calTaken = 0
//    var carbTaken = 0
//    var protTaken = 0
//    var fatTaken = 0
//
//    var bLoaded = false
//    var lLoaded = false
//    var dLoaded = false

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd MMMM yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View? = binding?.root

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        binding?.txtDay?.text = dateFormat.format(getToday())
        binding?.btnNext?.visibility = View.INVISIBLE

        viewModel.date.observe(viewLifecycleOwner) { selDate ->
            binding?.txtDay?.text = dateFormat.format(selDate)
        }

        getNutrition(uid)
        calculateNutrition(uid, binding?.txtDay?.text.toString())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            getRecommendation(auth.currentUser?.uid, binding?.txtDay?.text.toString())
        }


        binding?.btnPrev?.setOnClickListener {
            val button = "prev"
            val newDay = changeDay(binding?.txtDay?.text.toString(), button)
            binding?.txtDay?.text = dateFormat.format(newDay)
        }

        binding?.btnNext?.setOnClickListener {
            val button = "next"
            val newDay = changeDay(binding?.txtDay?.text.toString(), button)
            binding?.txtDay?.text = dateFormat.format(newDay)
        }

        binding?.txtDay?.addTextChangedListener {
            val today = dateFormat.format(getToday())
            val displayedDay = dateFormat.format(getDisplayedDay(binding?.txtDay?.text.toString()))

            if (today.equals(displayedDay)) {
                binding?.btnNext?.visibility = View.INVISIBLE
            } else {
                binding?.btnNext?.visibility = View.VISIBLE
            }
        }

        binding?.txtDay?.setOnClickListener {
            val displayedDay = getDisplayedDay(binding?.txtDay?.text.toString())
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
                    binding?.txtDay?.text = dateFormat.format(calendar.time)
                },
                year, month, day
            )

            picker.datePicker.maxDate = System.currentTimeMillis()
            picker.show()
        }

        binding?.txtDay?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
//                getRecommendation(auth.currentUser?.uid, s.toString())
//                calculateNutrition(auth.currentUser?.uid, s.toString())

                CoroutineScope(Dispatchers.Main).launch {
                    val isFinish = getRecommendation(auth.currentUser?.uid, s.toString())

                    if (isFinish) {
                        calculateNutrition(auth.currentUser?.uid, s.toString())
                    }
                }
            }
        })

        binding?.btnAddBreakfast?.setOnClickListener {
            changeFood(binding?.txtDay?.text.toString(), "Breakfast")
        }

        binding?.btnAddLunch?.setOnClickListener {
            changeFood(binding?.txtDay?.text.toString(), "Lunch")
        }

        binding?.btnAddDinner?.setOnClickListener {
            changeFood(binding?.txtDay?.text.toString(), "Dinner")
        }

        binding?.btnAddBreakfastR?.setOnClickListener {
            addRecommended(auth.currentUser?.uid, binding?.txtDay?.text.toString(), "Breakfast")
        }

        binding?.btnAddLunchR?.setOnClickListener {
            addRecommended(auth.currentUser?.uid, binding?.txtDay?.text.toString(), "Lunch")
        }

        binding?.btnAddDinnerR?.setOnClickListener {
            addRecommended(auth.currentUser?.uid, binding?.txtDay?.text.toString(), "Dinner")
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

    private fun showLoading() {
        viewModel.isLoading.observe(requireActivity()) { load ->
            binding?.pbBR?.visibility = if (load) View.VISIBLE else View.GONE
            binding?.pbLR?.visibility = if (load) View.VISIBLE else View.GONE
            binding?.pbDR?.visibility = if (load) View.VISIBLE else View.GONE

            binding?.btnAddBreakfastR?.isClickable = !load
            binding?.btnAddBreakfastR?.isEnabled = !load
            binding?.btnAddLunchR?.isClickable = !load
            binding?.btnAddLunchR?.isEnabled = !load
            binding?.btnAddDinnerR?.isClickable = !load
            binding?.btnAddDinnerR?.isEnabled = !load

            binding?.pbB?.visibility = if (load) View.VISIBLE else View.GONE
            binding?.pbL?.visibility = if (load) View.VISIBLE else View.GONE
            binding?.pbD?.visibility = if (load) View.VISIBLE else View.GONE

            binding?.btnAddBreakfast?.isClickable = !load
            binding?.btnAddBreakfast?.isEnabled = !load
            binding?.btnAddLunch?.isClickable = !load
            binding?.btnAddLunch?.isEnabled = !load
            binding?.btnAddDinner?.isClickable = !load
            binding?.btnAddDinner?.isEnabled = !load
        }
    }

    private fun showLoadingN() {
        viewModel.isLoadingN.observe(requireActivity()) { load ->
            binding?.pbNutrition?.visibility = if (load) View.VISIBLE else View.GONE
        }
    }

    private fun getNutrition(id: String?) {
        if (id != null) {
            showLoadingN()

            viewModel.getNutrition(id)

            viewModel.nutrition.observe(viewLifecycleOwner) { nutrition ->
                if (nutrition != null) {
//                val calNeeded = str2Int(nutrition.calories)
//                val carbNeeded = str2Int(nutrition.carbohydrates)
//                val protNeeded = str2Int(nutrition.protein)
//                val fatNeeded = str2Int(nutrition.fat)

                    binding?.txtNeededCal?.text = " / ${str2Int(nutrition.calories)} kcal"
                    binding?.txtNeededCarb?.text = " / ${str2Int(nutrition.carbohydrates)} g"
                    binding?.txtNeededProt?.text = " / ${str2Int(nutrition.protein)} g"
                    binding?.txtNeededFat?.text = " / ${str2Int(nutrition.fat)} g"
                }
            }
        }
    }

    private fun calculateNutrition(id: String?, date: String) {
        if (id != null) {
            val (day, month) = getDateMonthNum(date)

            showLoadingN()

            viewModel.calculateNutrition(id, day.toString())

            viewModel.nutritionTaken.observe(viewLifecycleOwner) { taken ->
                if (taken != null) {
                    binding?.txtTakenCal?.text = (str2Int(taken.calories)).toString()
                    binding?.txtTakenCarb?.text = (str2Int(taken.carbohydrates)).toString()
                    binding?.txtTakenProt?.text = (str2Int(taken.protein)).toString()
                    binding?.txtTakenFat?.text = (str2Int(taken.fat)).toString()
                }
            }
        }
    }

    private suspend fun getRecommendation(id: String?, date: String): Boolean {
        if (id != null) {
            val (day, month) = getDateMonthNum(date)

            showLoading()

            viewModel.getRecommendation(id, day.toString(), month.toString())

            viewModel.food.observe(viewLifecycleOwner) { recommendation ->
                if (recommendation.breakfast != null) {
                    val cal = str2Int(recommendation.breakfast[4].calories)
                    val carb = str2Int(recommendation.breakfast[7].carbohydrates)
                    val prot = str2Int(recommendation.breakfast[5].protein)
                    val fat = str2Int(recommendation.breakfast[6].fat)

                    binding?.imgBreakR?.let {
                        Glide.with(requireContext())
                            .load(recommendation.breakfast[2].link)
                            .into(it)
                    }
                    binding?.txtBreakNameR?.text = recommendation.breakfast[1].name
                    binding?.txtBreakCalR?.text = "$cal kcal"

                    if (recommendation.breakfast[8].eat) {
                        binding?.imgBreak?.let {
                            Glide.with(requireContext())
                                .load(recommendation.breakfast[2].link)
                                .into(it)
                        }
                        binding?.txtBreakName?.text = recommendation.breakfast[1].name
                        binding?.txtBreakCal?.text = "$cal kcal"
//                        binding?.btnAddBreakfast.setImageResource(R.drawable.ic_round_edit_24)
                        binding?.btnAddBreakfast?.visibility = View.GONE

                        binding?.cardBreakfastR?.visibility = View.GONE
                    } else {
                        binding?.imgBreak?.let { Glide.with(requireContext()).clear(it) }
                        binding?.txtBreakName?.text = getString(R.string.food_name)
                        binding?.txtBreakCal?.text = getString(R.string.kcal)
//                        binding?.btnAddBreakfast.setImageResource(R.drawable.ic_round_add_24)
                        binding?.btnAddBreakfast?.visibility = View.VISIBLE

                        binding?.cardBreakfastR?.visibility = View.VISIBLE
                    }
                }

                if (recommendation.lunch != null) {
                    val cal = str2Int(recommendation.lunch[4].calories)
                    val carb = str2Int(recommendation.lunch[7].carbohydrates)
                    val prot = str2Int(recommendation.lunch[5].protein)
                    val fat = str2Int(recommendation.lunch[6].fat)

                    binding?.imgLunchR?.let {
                        Glide.with(requireContext())
                            .load(recommendation.lunch[2].link)
                            .into(it)
                    }
                    binding?.txtLunchNameR?.text = recommendation.lunch[1].name
                    binding?.txtLunchCalR?.text = "$cal kcal"

                    if (recommendation.lunch[8].eat) {
                        binding?.imgLunch?.let {
                            Glide.with(requireContext())
                                .load(recommendation.lunch[2].link)
                                .into(it)
                        }
                        binding?.txtLunchName?.text = recommendation.lunch[1].name
                        binding?.txtLunchCal?.text = "$cal kcal"
//                        binding?.btnAddLunch.setImageResource(R.drawable.ic_round_edit_24)
                        binding?.btnAddLunch?.visibility = View.GONE

                        binding?.cardLunchR?.visibility = View.GONE
                    } else {
                        binding?.imgLunch?.let { Glide.with(requireContext()).clear(it) }
                        binding?.txtLunchName?.text = getString(R.string.food_name)
                        binding?.txtLunchCal?.text = getString(R.string.kcal)
//                        binding?.btnAddLunch.setImageResource(R.drawable.ic_round_add_24)
                        binding?.btnAddLunch?.visibility = View.VISIBLE

                        binding?.cardLunchR?.visibility = View.VISIBLE
                    }
                }

                if (recommendation.dinner != null) {
                    val cal = str2Int(recommendation.dinner[4].calories)
                    val carb = str2Int(recommendation.dinner[7].carbohydrates)
                    val prot = str2Int(recommendation.dinner[5].protein)
                    val fat = str2Int(recommendation.dinner[6].fat)

                    binding?.imgDinnerR?.let {
                        Glide.with(requireContext())
                            .load(recommendation.dinner[2].link)
                            .into(it)
                    }
                    binding?.txtDinnerNameR?.text = recommendation.dinner[1].name
                    binding?.txtDinnerCalR?.text = "$cal kcal"

                    if (recommendation.dinner[8].eat) {
                        binding?.imgDinner?.let {
                            Glide.with(requireContext())
                                .load(recommendation.dinner[2].link)
                                .into(it)
                        }
                        binding?.txtDinnerName?.text = recommendation.dinner[1].name
                        binding?.txtDinnerCal?.text = "$cal kcal"
//                        binding?.btnAddDinner.setImageResource(R.drawable.ic_round_edit_24)
                        binding?.btnAddDinner?.visibility = View.GONE

                        binding?.cardDinnerR?.visibility = View.GONE
                    } else {
                        binding?.imgDinner?.let { Glide.with(requireContext()).clear(it) }
                        binding?.txtDinnerName?.text = getString(R.string.food_name)
                        binding?.txtDinnerCal?.text = getString(R.string.kcal)
//                        binding?.btnAddDinner.setImageResource(R.drawable.ic_round_add_24)
                        binding?.btnAddDinner?.visibility = View.VISIBLE

                        binding?.cardDinnerR?.visibility = View.VISIBLE
                    }
                }
            }

            viewModel.recMsg.observe(requireActivity()) { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }

            calculateNutrition(id, date)

            return true
        }

        return false
    }

    private suspend fun updateEaten(id: String?, date: String, type: String): Boolean {
        if (id != null) {
            val (day, month) = getDateMonthNum(date)

            showLoading()

            viewModel.updateEat(id, day.toString(), month.toString(), type)

            viewModel.updateEaten.observe(viewLifecycleOwner) {
//                getRecommendation(id, date)
                CoroutineScope(Dispatchers.Main).launch {
                    getRecommendation(id, date)
                }
            }

            viewModel.updateMsg.observe(requireActivity()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            return true
        }

        return false
    }

    private fun str2Int(str: String?): Int? {
        return try {
            str?.let { round(it.toDouble()).toInt() }
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun changeFood(date: String, typeMeal: String) {
        val (day, month) = getDateMonthNum(date)
        val intent = Intent(requireContext(), AddFoodActivity::class.java)
        intent.putExtra(AddFoodActivity.DAY, day)
        intent.putExtra(AddFoodActivity.MONTH, month)
        intent.putExtra(AddFoodActivity.TYPE, typeMeal)
        startActivity(intent)
    }

    private fun addRecommended(uid: String?, date: String, type: String) {
        if (uid != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val isFinish = updateEaten(uid, date, type)

                if (isFinish) {
                    calculateNutrition(uid, date)
                }
            }
        }
    }
}