package com.example.android.todolistapplication.tasklist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todolistapplication.R
import com.example.android.todolistapplication.database.Task
import com.example.android.todolistapplication.databinding.RecyclerViewListItemTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class TaskAdapter(val clickCheckboxListener: TaskListener, val clickLayoutListener: TaskListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(TaskDiffCallBack()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    class ConstraintLayoutHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {

            fun from(parent: ViewGroup): ConstraintLayoutHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return ConstraintLayoutHolder(view)
            }

        }

    }

    class ViewHolder private constructor(val binding: RecyclerViewListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, clickCheckboxListener: TaskListener, clickLayoutListener: TaskListener) {
            binding.task = item
            binding.clickCheckboxListener = clickCheckboxListener
            binding.clickLayoutListener = clickLayoutListener
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerViewListItemTaskBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> ConstraintLayoutHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.TaskItem
                holder.bind(item.task, clickCheckboxListener, clickLayoutListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.TaskItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Task>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.TaskItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

}

class TaskDiffCallBack : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class TaskListener(val clickListener: (id: Long) -> Unit) {

    fun onClick(task: Task) = clickListener(task.id)

}

sealed class DataItem() {

    abstract val id: Long

    data class TaskItem(val task: Task) : DataItem() {

        override val id = task.id

    }

    object Header : DataItem() {

        override val id = Long.MIN_VALUE

    }

}
