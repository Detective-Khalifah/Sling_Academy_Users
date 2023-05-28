package com.blogspot.thengnet.slingacademyusers

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {

        fun formatDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        }

    }
}