package com.example.retrofitdemo.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitdemo.databinding.FragmentNotificationsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModelFactory: NotificationsViewModelFactory
    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModelFactory = NotificationsViewModelFactory()
        notificationsViewModel =
            ViewModelProvider(
                this,
                notificationsViewModelFactory
            ).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.generateBtn.setOnClickListener {
            val name = binding.name.text.toString()
            notificationsViewModel.generateCode(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Generating code failed. ${it.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val body = it.body()!!
                        binding.code.setText(body.code)
                    }
                }
        }

        binding.validateBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val code = binding.code.text.toString()
            notificationsViewModel.validate(Code(code, name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Validatioon failed. ${it.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val body = it.body()!!
                        binding.result.text = body.message
                    }
                }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}