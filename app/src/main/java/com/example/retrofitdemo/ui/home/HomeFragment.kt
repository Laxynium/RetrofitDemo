package com.example.retrofitdemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitdemo.BR
import com.example.retrofitdemo.R
import com.example.retrofitdemo.databinding.FragmentHomeBinding
import com.example.retrofitdemo.model.TodoList


class HomeFragment : Fragment() {

//    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val view: View = binding.root

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val viewModel = makeApiCall()

        setupBinding(view, binding, viewModel)
        return view
    }

    fun setupBinding(view: View, binding: FragmentHomeBinding, viewModel: HomeViewModel) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.todosRecyclerView)
        binding.setVariable(BR.homeViewModel, viewModel)
        binding.executePendingBindings()
        val fragmentContext = context

        recyclerView.apply {
            adapter = viewModel.getAdapter()
            layoutManager = LinearLayoutManager(fragmentContext)
            val decoration  = DividerItemDecoration(fragmentContext, LinearLayout.VERTICAL)
            addItemDecoration(decoration)
        }

    }

    fun makeApiCall(): HomeViewModel {
        val viewModel =  ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.getRecyclerListDataObserver().observe(viewLifecycleOwner, Observer<TodoList>{
            if(it != null) {
                //update the adapter
                viewModel.setAdapterData(it.items)
            } else {
                Toast.makeText(context, "Error in fetching data", Toast.LENGTH_LONG).show()
                println("Error in fetching data")
            }
        })
        viewModel.makeAPICall()

        return viewModel
    }
}
