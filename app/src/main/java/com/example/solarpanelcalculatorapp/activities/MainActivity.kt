package com.example.solarpanelcalculatorapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.fragments.*
import com.example.solarpanelcalculatorapp.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (sessionManager.fetchAuthToken() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment(), "Página Inicial")
                R.id.nav_calculate -> loadFragment(CalculateFragment(), "Calcular Painéis Solares")
                R.id.nav_appliances -> loadFragment(AppliancesFragment(), "Eletrodomésticos")
                R.id.nav_analyses -> loadFragment(AnalysesFragment(), "Histórico de Análises")
                R.id.nav_logout -> {
                    sessionManager.clearAuthToken()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            true
        }

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        val titleTextView = findViewById<TextView>(R.id.textViewPageTitle)
        titleTextView.text = title
    }
}
