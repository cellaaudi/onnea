package com.cellaaudi.onnea.ui.questions

import androidx.lifecycle.ViewModel

class AnswerViewModel : ViewModel() {
    var name: String = ""
    var birthdate: String = ""
    var gender: String = ""
    var weight: String = ""
    var height: String = ""
    var activity: String = ""
    var goal: String = ""
    var diet: String? = null
    var allergies: String? = null
    var spicy: String? = null
    var fruit: String = ""
    var healthy: String = ""
}