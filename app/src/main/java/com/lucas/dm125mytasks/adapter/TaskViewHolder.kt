package com.lucas.dm125mytasks.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.lucas.dm125mytasks.R
import com.lucas.dm125mytasks.databinding.TaskListItemBinding
import com.lucas.dm125mytasks.entity.Task
import com.lucas.dm125mytasks.listener.TaskItemClickListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewHolder(
    private val context: Context,
    private val binding: TaskListItemBinding,
    private val listener: TaskItemClickListener
) : RecyclerView.ViewHolder(binding.root) {


    fun setValues(task: Task) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val dateFormat = sharedPreferences.getString("date_format", "date_numbers")

        binding.tvTitle.text = task.title
        binding.tvDescription.text = task.description

        binding.tvDate.text = task.date?.let { date ->
            val formatter = if (dateFormat == "date_numbers") {
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            } else {
                DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")
            }
            date.format(formatter)
        } ?: "-"

        binding.tvTime.text = task.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "-"

        if (task.completed) {
            binding.tvTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.green_700))
        } else {
            val currentDate = LocalDate.now()
            val taskDate = task.date

            val cardColor = when {
                taskDate == null || taskDate.isAfter(currentDate) -> R.color.blue_700
                taskDate.isEqual(currentDate) -> R.color.yellow_500
                taskDate.isBefore(currentDate) -> R.color.red_500
                else -> R.color.blue_700
            }

            binding.tvTitle.setBackgroundColor(ContextCompat.getColor(context, cardColor))
        }

        binding.root.setOnClickListener { listener.onClick(task) }

        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add(ContextCompat.getString(context, R.string.mark_as_completed))
                .setOnMenuItemClickListener {
                    listener.onMarkAsCompleteClick(adapterPosition, task)
                    true
                }
            menu.add(ContextCompat.getString(context, R.string.share))
                .setOnMenuItemClickListener {
                    listener.onShareClick(task)
                    true
                }
        }
    }
}
