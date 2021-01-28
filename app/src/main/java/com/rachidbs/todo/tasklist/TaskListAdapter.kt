package com.rachidbs.todo.tasklist
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rachidbs.todo.databinding.ItemTaskBinding

class TaskListAdapter :
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {
    var onDeleteTask: ((Task) -> Unit)? = null
    var onEditTask: ((Task) -> Unit)? = null
    var onLongClick: ((Task) -> Unit)? = null

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {

            binding.task = task
            binding.deleteButton.setOnClickListener { onDeleteTask?.invoke(task) }
            binding.editButton.setOnClickListener { onEditTask?.invoke(task) }
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(task)
                true
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListAdapter.TaskViewHolder {
        val binding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}