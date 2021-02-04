package com.rachidbs.todo.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.rachidbs.todo.databinding.FragmentTaskListBinding
import com.rachidbs.todo.task.TaskActivity
import com.rachidbs.todo.userInfo.UserInfoActivity
import com.rachidbs.todo.userInfo.UserInfoViewModel
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var taskListAdapter: TaskListAdapter
    private val tasksViewModel: TaskListViewModel by viewModels()
    private val userViewModel: UserInfoViewModel by viewModels()

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
        userViewModel.loadInfo()
        tasksViewModel.loadTasks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val resultCreateTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val newTask = result.data!!.getSerializableExtra(TaskActivity.NEW_TASK) as Task
                    tasksViewModel.createTask(newTask)
                }
            }

        val resultEditTask =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val newTask = result.data!!.getSerializableExtra(TaskActivity.NEW_TASK) as Task
                    tasksViewModel.updateTask(newTask)
                }
            }
        taskListAdapter = TaskListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter

        tasksViewModel.taskList.observe(viewLifecycleOwner, Observer {
            taskListAdapter.submitList(it)
        })

        userViewModel.user.observe(viewLifecycleOwner, { userInfo ->
            binding.userInfo.text = "${userInfo.firstName} ${userInfo.lastName}"
            binding.avatar.load(userInfo.avatar) {
                transformations(CircleCropTransformation())
            };
        })

        taskListAdapter.onDeleteTask = {
            tasksViewModel.deleteTask(it)
        }

        binding.avatar.setOnClickListener {
            startActivity(Intent(activity, UserInfoActivity::class.java))
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