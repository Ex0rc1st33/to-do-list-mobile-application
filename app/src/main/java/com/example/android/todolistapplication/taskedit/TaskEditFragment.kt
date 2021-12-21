package com.example.android.todolistapplication.taskedit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.todolistapplication.R
import com.example.android.todolistapplication.database.TaskDatabase
import com.example.android.todolistapplication.databinding.FragmentTaskEditBinding

class TaskEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskEditBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_edit, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = TaskEditFragmentArgs.fromBundle(arguments)
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskEditViewModelFactory(dataSource, arguments.id)
        val taskEditViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TaskEditViewModel::class.java)
        binding.taskEditViewModel = taskEditViewModel
        binding.lifecycleOwner = this

        taskEditViewModel.navigateToTaskList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    TaskEditFragmentDirections.actionTaskEditFragmentToTaskListFragment()
                )
                taskEditViewModel.doneNavigating()
            }
        })

        taskEditViewModel.editTask.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.apply {
                    taskDetailTitle.visibility = View.GONE
                    taskDetailTitleEdit.visibility = View.VISIBLE
                    taskDetailDescription.visibility = View.GONE
                    taskDetailDescriptionEdit.visibility = View.VISIBLE
                    taskDetailTitleEdit.setSelection(taskDetailTitleEdit.length())
                    taskDetailDescriptionEdit.setSelection(taskDetailDescriptionEdit.length())
                    buttonAddNewTask.setImageResource(R.drawable.checkmark_icon)
                    taskDetailTitleEdit.requestFocus()
                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(taskDetailTitleEdit, 0)
                }
            } else if (it == false) {
                binding.apply {
                    Toast.makeText(context, "Task edited!", Toast.LENGTH_SHORT).show()
                    taskDetailTitle.visibility = View.VISIBLE
                    taskDetailTitleEdit.visibility = View.GONE
                    taskDetailDescription.visibility = View.VISIBLE
                    taskDetailDescriptionEdit.visibility = View.GONE
                    buttonAddNewTask.setImageResource(R.drawable.edit_icon)
                    taskEditViewModel.doneEditTask(taskDetailTitleEdit.text.toString(), taskDetailDescriptionEdit.text.toString())
                }
            }
        })

        taskEditViewModel.showDeleteToast.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "Task deleted!", Toast.LENGTH_SHORT).show()
                taskEditViewModel.doneShowDeleteToast()
            }
        })

        return binding.root

    }

}
