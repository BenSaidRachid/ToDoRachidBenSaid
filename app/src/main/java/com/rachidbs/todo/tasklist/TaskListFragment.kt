package com.rachidbs.todo.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rachidbs.todo.databinding.FragmentTaskListBinding
import com.rachidbs.todo.network.Api
import com.rachidbs.todo.network.TasksRepository
import com.rachidbs.todo.task.TaskActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class TaskListFragment : Fragment() {
    private val tasksRepository = TasksRepository()
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var taskListAdapter: TaskListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()

        fetchUserInfo()
        fetchTasks()
    }

    private fun fetchUserInfo() {
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            binding.userInfo.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
    }

    private fun fetchTasks() {
        lifecycleScope.launch {
            tasksRepository.refresh()
        }
    }

    private fun deleteTask(task: Task) {
        lifecycleScope.launch {
            tasksRepository.deleteTask(task)
        }
    }

    private fun addTask(task: Task) {
        lifecycleScope.launch {
            tasksRepository.addTask(task)
        }
    }

    private fun updateTask(task: Task) {
        lifecycleScope.launch {
            tasksRepository.updateTask(task)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val resultCreateTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val newTask = result.data!!.getSerializableExtra(TaskActivity.NEW_TASK) as Task
                    addTask(newTask)
                }
            }

        val resultEditTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val newTask = result.data!!.getSerializableExtra(TaskActivity.NEW_TASK) as Task
                    updateTask(newTask)
                }
            }
        taskListAdapter = TaskListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter

        tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            taskListAdapter.submitList(it)
        })

        taskListAdapter.onDeleteTask = {
            deleteTask(it)
        }



        if (activity?.intent?.action == Intent.ACTION_SEND) {
            val description = activity?.intent?.getStringExtra(Intent.EXTRA_TEXT)
            val newIntent = Intent(activity, TaskActivity::class.java)
            newIntent.putExtra(TaskActivity.SHARE_TASK, description)
            resultCreateTask.launch(newIntent)
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
            resultEditTask.launch(intent)
        }

        binding.floatingButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            resultCreateTask.launch(intent)
        }
    }
}