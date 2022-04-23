package dev.marshi.jetalarm.ui.editalarm

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

fun showTimePicker(
    context: Context,
    hour: Int = 9,
    minute: Int = 0,
    onTimeSet: (view: TimePicker?, hourOfDay: Int, minute: Int) -> Unit = { _, _, _ -> },
) {
    val dialog = TimePickerDialog(
        context,
        onTimeSet,
        hour,
        minute,
        true
    )
    dialog.show()
}