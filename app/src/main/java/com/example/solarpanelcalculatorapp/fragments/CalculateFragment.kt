package com.example.solarpanelcalculatorapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.api.RetrofitInstance
import com.example.solarpanelcalculatorapp.models.AnalysisRequest
import com.example.solarpanelcalculatorapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalculateFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var editTextSunlightHours: EditText
    private lateinit var buttonCalculate: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resultContainer: LinearLayout
    private lateinit var textViewResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculate, container, false)

        sessionManager = SessionManager(requireContext())
        editTextSunlightHours = view.findViewById(R.id.editTextSunlightHours)
        buttonCalculate = view.findViewById(R.id.buttonCalculate)
        progressBar = view.findViewById(R.id.progressBar)
        resultContainer = view.findViewById(R.id.resultContainer)
        textViewResult = view.findViewById(R.id.textViewResult)

        buttonCalculate.setOnClickListener {
            checkAppliancesAndCalculate()
        }

        return view
    }

    private fun checkAppliancesAndCalculate() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"

        progressBar.visibility = View.VISIBLE
        resultContainer.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getAppliances(token)
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val appliances = response.body()!!
                    if (appliances.isNotEmpty()) {
                        val sunlightHours = editTextSunlightHours.text.toString().toIntOrNull()
                        if (sunlightHours != null && sunlightHours in 1..24) {
                            calculateAnalysis(sunlightHours)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Insira um valor entre 1 e 24",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Nenhum eletrônico encontrado. Adicione um antes de calcular.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao verificar os eletrônicos: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun calculateAnalysis(sunlightHours: Int) {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        val analysisRequest = AnalysisRequest(sunlightHours)

        progressBar.visibility = View.VISIBLE
        resultContainer.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.createAnalysis(token, analysisRequest)
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val analysis = response.body()!!
                    textViewResult.text = analysis.result
                    resultContainer.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao calcular a análise",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
