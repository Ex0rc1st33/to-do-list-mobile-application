package com.example.android.todolistapplication.taskadd

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.todolistapplication.R
import com.example.android.todolistapplication.database.TaskDatabase
import com.example.android.todolistapplication.databinding.FragmentTaskAddBinding

class TaskAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_add, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskAddViewModelFactory(dataSource)
        val taskAddViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TaskAddViewModel::class.java)
        binding.taskAddViewModel = taskAddViewModel
        binding.lifecycleOwner = this

        taskAddViewModel.navigateToTaskList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    TaskAddFragmentDirections.actionTaskAddFragmentToTaskListFragment()
                )
                taskAddViewModel.doneNavigating()
            }
        })

        taskAddViewModel.submitButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "Task created!", Toast.LENGTH_SHORT).show()
                val title = binding.editTextTitle.text.toString()
                val description = binding.editTextDescription.text.toString()
                taskAddViewModel.onCreateTask(title, description)
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.view?.windowToken, 0)
                taskAddViewModel.doneSubmit()
            }
        })

        return binding.root

    }

}
