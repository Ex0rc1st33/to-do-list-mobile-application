package com.example.android.todolistapplication.tasklist

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.todolistapplication.R
import com.example.android.todolistapplication.database.TaskDatabase
import com.example.android.todolistapplication.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_list, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskListViewModelFactory(dataSource)
        val taskListViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(TaskListViewModel::class.java)
        binding.taskListViewModel = taskListViewModel
        binding.lifecycleOwner = this

        val adapter = TaskAdapter(TaskListener { id ->
            taskListViewModel.onToggleCheckbox(id)
        }, TaskListener { id ->
            taskListViewModel.onClickLayout(id)
        })
        binding.recyclerViewTasks.adapter = adapter
        taskListViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        taskListViewModel.navigateToTaskAdd.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    TaskListFragmentDirections.actionTaskListFragmentToTaskAddFragment()
                )
                taskListViewModel.doneNavigatingToTaskAdd()
            }
        })

        taskListViewModel.navigateToTaskEdit.observe(viewLifecycleOwner, Observer { id ->
            id?.let {
                this.findNavController().navigate(
                    TaskListFragmentDirections.actionTaskListFragmentToTaskEditFragment(id)
                )
                taskListViewModel.doneNavigatingToTaskEdit()
            }
        })

        taskListViewModel.showDeleteAllToast.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "All tasks deleted!", Toast.LENGTH_SHORT).show()
                taskListViewModel.doneShowDeleteAllToast()
            }
        })

        taskListViewModel.clearAllButtonVisible.observe(viewLifecycleOwner, Observer {
            binding.buttonClearAllTasks.isEnabled = it
            if (it == true) {
                binding.buttonClearAllTasks.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))
                binding.emptyListLabel.visibility = View.GONE
            } else {
                binding.buttonClearAllTasks.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red_lower_contrast))
                binding.emptyListLabel.visibility = View.VISIBLE
            }
        })

        return binding.root

    }

}
