package com.example.android.todolistapplication.tasklist

import android.content.res.ColorStateList
import android.graphics.Paint
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.android.todolistapplication.R
import com.example.android.todolistapplication.convertCreationDateToString
import com.example.android.todolistapplication.database.Task

@BindingAdapter("taskCreationDateFormatted")
fun TextView.setTaskCreationDateFormatted(item: Task?) {
    item?.let {
        text = convertCreationDateToString(item.creationDate)
    }
}

@BindingAdapter("taskCheckboxImage")
fun ImageButton.setTaskCheckboxImage(item: Task?) {
    item?.let {
        setImageResource(
            if (item.isDone) R.drawable.filled_checkmark_icon else R.drawable.empty_checkmark_icon
        )
    }
}

@BindingAdapter("taskTitleStrikethrough")
fun TextView.setTaskTitleStrikethrough(item: Task?) {
    item?.let {
        paintFlags = if (item.isDone) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

@BindingAdapter("taskColor")
fun TextView.setTaskColor(item: Task?) {
    item?.let {
        setTextColor(
            if (item.isDone) ContextCompat.getColor(context, R.color.dark_purple_lower_contrast) else ContextCompat.getColor(
                context,
                R.color.dark_purple
            )

        )
    }
}

@BindingAdapter("taskBackgroundColor")
fun ConstraintLayout.setTaskBackgroundColor(item: Task?) {
    item?.let {
        backgroundTintList =
            if (item.isDone) ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_purple_lower_contrast)) else ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.light_purple)
            )
    }
}
