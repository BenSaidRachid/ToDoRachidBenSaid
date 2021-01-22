package com.rachidbs.todo.tasklist
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rachidbs.todo.databinding.FragmentTaskListBinding
import java.util.*

class TaskListFragment : Fragment() {
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private lateinit var binding: FragmentTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val taskListAdapter = TaskListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter
        taskListAdapter.submitList(taskList.toList())
        binding.floatingButton.setOnClickListener {
            taskList.add(
                Task(
                    id = UUID.randomUUID().toString(),
                    title = "Task ${taskList.size + 1}"
                )
            )
            taskListAdapter.submitList(taskList.toList())
        }
    }
}