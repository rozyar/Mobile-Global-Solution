package com.example.solarpanelcalculatorapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.activities.LoginActivity
import com.example.solarpanelcalculatorapp.utils.SessionManager

class HomeFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        sessionManager = SessionManager(requireContext())

        val buttonCalculate = view.findViewById<LinearLayout>(R.id.buttonCalculateSolarPanels)
        val buttonViewAnalyses = view.findViewById<LinearLayout>(R.id.buttonViewAnalyses)
        val buttonManageAppliances = view.findViewById<LinearLayout>(R.id.buttonManageAppliances)
        val buttonLogout = view.findViewById<LinearLayout>(R.id.buttonLogout)

        buttonCalculate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalculateFragment())
                .addToBackStack(null)
                .commit()
            activity?.findViewById<TextView>(R.id.textViewPageTitle)?.text = "Calcular Painéis Solares"
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
                ?.selectedItemId = R.id.nav_calculate
        }

        buttonViewAnalyses.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AnalysesFragment())
                .addToBackStack(null)
                .commit()
            activity?.findViewById<TextView>(R.id.textViewPageTitle)?.text = "Histórico de Análises"
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
                ?.selectedItemId = R.id.nav_analyses
        }

        buttonManageAppliances.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AppliancesFragment())
                .addToBackStack(null)
                .commit()
            activity?.findViewById<TextView>(R.id.textViewPageTitle)?.text = "Eletrodomésticos"
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
                ?.selectedItemId = R.id.nav_appliances
        }

        buttonLogout.setOnClickListener {
            sessionManager.clearAuthToken()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}
