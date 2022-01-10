package com.example.retrofitdemo.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitdemo.R
import com.example.retrofitdemo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModelFactory = HomeViewModelFactory()
        homeViewModel =
            ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addBtn.setOnClickListener {
            homeViewModel.addTodo(Todo(-1, "TODO", false))
        }

        val todoAdapter =
            TodoAdapter(requireContext(), emptyList(), { todo, value ->
                homeViewModel.updateTodoObs(todo.copy(completed = value))
            }, { todo, value ->
                homeViewModel.updateTodoObs(todo.copy(text = value))
            }, { todo ->
                homeViewModel.removeTodo(todo)
            })
        binding.todosList.adapter = todoAdapter

        homeViewModel.todos.observe(viewLifecycleOwner) {
            todoAdapter.updateTodos(it)
        }

        homeViewModel.error.observe(viewLifecycleOwner) {
            it.let {
                if(!it.isNullOrEmpty())
                {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        homeViewModel.loadTodosObs()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class TodoAdapter(
        context: Context,
        todos: List<Todo>,
        private val onCheckedChanged: (Todo, value: Boolean) -> Unit,
        private val onTextChanged: (Todo, value: String) -> Unit,
        private val onDeleted: (Todo) -> Unit
    ) :
        ArrayAdapter<Todo>(context, 0, todos.toMutableList()) {

        private var currentlyFocusRow: Int = 0;

        fun updateTodos(todos: List<Todo>) {
            this.clear()
            this.addAll(todos.toMutableList())
            this.notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val todo = getItem(position)!!

            val view: View =
                (LayoutInflater.from(context).inflate(R.layout.todo_fragment, parent, false)
                    ?: convertView)!!

            val text = view.findViewById<TextView>(R.id.text)
            val completed = view.findViewById<CheckBox>(R.id.completed)
            val delBtn = view.findViewById<Button>(R.id.deleteBtn)
            val updateBtn = view.findViewById<Button>(R.id.updateBtn)

            text.text = todo.text
            completed.isChecked = todo.completed

            text.setOnFocusChangeListener { _, isFocused ->
                if (isFocused) {
                    currentlyFocusRow = position
                }
            }

            text.isFocusable = true

            if (currentlyFocusRow == position) {
                text.requestFocus()
            }

            completed.setOnCheckedChangeListener { btn, isChecked ->
                onCheckedChanged(todo, isChecked)
            }

            delBtn.setOnClickListener {
                onDeleted(todo)
            }

            updateBtn.setOnClickListener {
                onTextChanged(todo, text.text.toString())
            }

            return view
        }
    }
}