package com.rachidbs.todo.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rachidbs.todo.databinding.FragmentTaskListBinding
import com.rachidbs.todo.task.TaskActivity

class TaskListFragment : Fragment() {
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var taskListAdapter: TaskListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val newTask = result.data!!.getSerializableExtra(TaskActivity.NEW_TASK) as Task
                    val id = taskList.indexOfFirst {
                        it.id == newTask.id
                    }
                    if (id < 0) {
                        taskList.add(
                            newTask
                        )
                    } else {
                        taskList[id] = newTask
                    }
                    taskListAdapter.submitList(taskList.toList())
                }
            }

        taskListAdapter = TaskListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter
        taskListAdapter.onDeleteTask = {
            taskList.remove(it)
            taskListAdapter.submitList(taskList.toList())
        }


        if (activity?.intent?.action == Intent.ACTION_SEND) {
            val description = activity?.intent?.getStringExtra(Intent.EXTRA_TEXT)
            val newIntent = Intent(activity, TaskActivity::class.java)
            newIntent.putExtra(TaskActivity.SHARE_TASK, description)
            startForResult.launch(newIntent)
        }

        taskListAdapter.onLongClick = { task ->
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, task.description)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareIntent, "Share task to.."))
        }

        taskListAdapter.onEditTask = {
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.EDIT_TASK, it)
            startForResult.launch(intent)
        }
        taskListAdapter.submitList(taskList.toList())

        binding.floatingButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startForResult.launch(intent)
        }
    }
}