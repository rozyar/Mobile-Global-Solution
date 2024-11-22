package com.example.solarpanelcalculatorapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.adapters.AnalysisAdapter
import com.example.solarpanelcalculatorapp.api.RetrofitInstance
import com.example.solarpanelcalculatorapp.models.AnalysisResponse
import com.example.solarpanelcalculatorapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnalysesFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnalysisAdapter
    private lateinit var textViewEmptyMessage: TextView
    private val analyses = mutableListOf<AnalysisResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analyses, container, false)

        sessionManager = SessionManager(requireContext())
        recyclerView = view.findViewById(R.id.recyclerViewAnalyses)
        textViewEmptyMessage = view.findViewById(R.id.textViewEmptyMessage)

        adapter = AnalysisAdapter(analyses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchAnalyses()

        return view
    }

    private fun fetchAnalyses() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getAnalyses(token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    analyses.clear()
                    analyses.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()

                    textViewEmptyMessage.visibility =
                        if (analyses.isEmpty()) View.VISIBLE else View.GONE
                    recyclerView.visibility =
                        if (analyses.isEmpty()) View.GONE else View.VISIBLE
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao obter as análises",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
