package com.example.lab_week_10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_10.viewmodels.TotalViewModel

class FirstFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        view?.findViewById<TextView>(R.id.text_total)?.text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        val viewModel = ViewModelProvider(requireActivity())
            .get(TotalViewModel::class.java)

        // Observe LiveData
        viewModel.total.observe(viewLifecycleOwner) {
            updateText(it)
        }
    }

    companion object {
        fun newInstance() = FirstFragment()
    }
}
