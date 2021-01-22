package com.rachidbs.todo.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rachidbs.todo.R
import kotlinx.android.synthetic.main.fragment_task_list.*


class TaskListFragment : Fragment() {
    private val taskList = listOf("Task 1","Task 2","Task 3"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = TaskListAdapter(taskList)
    }
}