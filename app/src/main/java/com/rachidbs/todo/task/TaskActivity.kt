package com.rachidbs.todo.task

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rachidbs.todo.databinding.ActivityTaskBinding
import com.rachidbs.todo.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding

    companion object {
        const val NEW_TASK = "new_task"
        const val EDIT_TASK = "edit_task"
        const val SHARE_TASK = "share_task"
    }

    private fun setTitleEditText(title: String?) {
        binding.taskTitle.setText(title)
    }

    private fun setDescriptionEditText(description: String?) {
        binding.taskDescription.setText(description)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val oldTask = intent.getSerializableExtra(EDIT_TASK) as? Task

        if (intent.hasExtra(EDIT_TASK)) {
            setTitleEditText(oldTask?.title)
            setDescriptionEditText(oldTask?.description)
        } else if (intent.hasExtra(SHARE_TASK)) {
            val sharedDescription = intent.getStringExtra(SHARE_TASK)
            setDescriptionEditText(sharedDescription)
        }

        binding.add.setOnClickListener {
            val title = binding.taskTitle.text.toString()
            val description = binding.taskDescription.text.toString()
            if (title.isEmpty())
                Toast.makeText(this, "You have to at least enter a title", Toast.LENGTH_LONG).show()
            else {
                val id = oldTask?.id ?: UUID.randomUUID().toString()
                val newTask = Task(id = id, title = title, description = description)
                intent.putExtra(NEW_TASK, newTask)
                setResult(RESULT_OK, intent)
                finish()
            }
        }


    }
}