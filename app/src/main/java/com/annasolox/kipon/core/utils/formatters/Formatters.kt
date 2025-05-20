package com.annasolox.kipon.core.utils.formatters

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Formatters {
    fun formatDateToUi(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return date.format(formatter)
    }

    fun parseDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(dateString, formatter)
    }
}