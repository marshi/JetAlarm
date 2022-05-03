package dev.marshi.jetalarm.extensions

import java.time.DayOfWeek
import kotlin.math.pow

fun Set<DayOfWeek>.toNumeric(): Int {
    return map { 2.0.pow(it.value.toDouble() - 1).toInt() }.fold(0) { acc, v -> acc + v }
}

fun dayOfWeekFrom(value: Int): Set<DayOfWeek> {
    val toBinaryString = Integer.toBinaryString(value)
    return toBinaryString.reversed().mapIndexedNotNull { index, c ->
        if (c == '1') {
            DayOfWeek.of(index + 1)
        } else {
            null
        }
    }.toSet()
}