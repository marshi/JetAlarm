package dev.marshi.jetalarm.extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.util.stream.Stream

class DayOfWeekExtKtTest {

    companion object {
        @JvmStatic
        fun toNumericTestCase() = Stream.of(
            arguments(ToNumericTestCase(setOf(), 0)),
            arguments(ToNumericTestCase(setOf(MONDAY), 1)),
            arguments(ToNumericTestCase(setOf(TUESDAY), 2)),
            arguments(ToNumericTestCase(setOf(SUNDAY), 64)),
            arguments(ToNumericTestCase(setOf(MONDAY, TUESDAY), 3)),
            arguments(
                ToNumericTestCase(
                    setOf(
                        MONDAY,
                        TUESDAY,
                        WEDNESDAY,
                        THURSDAY,
                        FRIDAY,
                        SATURDAY,
                        SUNDAY
                    ), 127
                )
            ),
        )

        @JvmStatic
        fun fromNumericTestCase() = Stream.of(
            arguments(FromNumericTestCase(0, setOf())),
            arguments(FromNumericTestCase(1, setOf(MONDAY))),
            arguments(FromNumericTestCase(2, setOf(TUESDAY))),
            arguments(FromNumericTestCase(3, setOf(MONDAY, TUESDAY))),
            arguments(FromNumericTestCase(4, setOf(WEDNESDAY))),
            arguments(FromNumericTestCase(127, setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY))),
        )
    }

    data class ToNumericTestCase(val dayOfWeeks: Set<DayOfWeek>, val expect: Int)
    data class FromNumericTestCase(val numeric: Int, val expect: Set<DayOfWeek>)

    @ParameterizedTest()
    @MethodSource("toNumericTestCase")
    fun toNumeric(testcase: ToNumericTestCase) {
        // given
        val dayOfWeeks = testcase.dayOfWeeks

        // when
        val result = dayOfWeeks.toNumeric()

        // then
        assertEquals(testcase.expect, result)
    }


    @ParameterizedTest()
    @MethodSource("fromNumericTestCase")
    fun fromNumeric(testcase: FromNumericTestCase) {
        val result = dayOfWeekFrom(testcase.numeric)
        assertEquals(testcase.expect, result)
    }
}