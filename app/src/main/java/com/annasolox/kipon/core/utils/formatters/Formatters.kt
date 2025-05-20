package com.annasolox.kipon.core.utils.formatters

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Formatters {
    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return date.format(formatter)
    }
}