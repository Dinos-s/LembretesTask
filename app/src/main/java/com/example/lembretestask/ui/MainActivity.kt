package com.example.lembretestask.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lembretestask.databinding.ActivityMainBinding
import com.example.lembretestask.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    private val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) updateList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()
        insertListeners()
    }

    private fun insertListeners() {
        binding.btnAdd.setOnClickListener {
            register.launch(Intent(this, AddTask::class.java))
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTask::class.java)
            intent.putExtra(AddTask.TASK_ID, it.id)
            register.launch(intent)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      //  super.onActivityResult(requestCode, resultCode, data)
     //   if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()

   //}

    private fun updateList(){
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE
        adapter.submitList(list)
    }

    //companion object{ private const val CREATE_NEW_TASK = 1000}
}