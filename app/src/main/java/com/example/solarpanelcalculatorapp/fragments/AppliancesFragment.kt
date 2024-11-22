package com.example.solarpanelcalculatorapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.adapters.ApplianceAdapter
import com.example.solarpanelcalculatorapp.api.RetrofitInstance
import com.example.solarpanelcalculatorapp.models.Appliance
import com.example.solarpanelcalculatorapp.models.ApplianceRequest
import com.example.solarpanelcalculatorapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppliancesFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApplianceAdapter
    private lateinit var editTextApplianceName: EditText
    private lateinit var editTextPowerConsumption: EditText
    private lateinit var buttonAddAppliance: Button
    private lateinit var textViewEmptyMessage: TextView // Para mensagem amigável
    private val appliances = mutableListOf<Appliance>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_appliances, container, false)

        sessionManager = SessionManager(requireContext())
        recyclerView = view.findViewById(R.id.recyclerViewAppliances)
        editTextApplianceName = view.findViewById(R.id.editTextApplianceName)
        editTextPowerConsumption = view.findViewById(R.id.editTextPowerConsumption)
        buttonAddAppliance = view.findViewById(R.id.buttonAddAppliance)
        textViewEmptyMessage = view.findViewById(R.id.textViewEmptyMessage)

        adapter = ApplianceAdapter(appliances, ::onEditAppliance, ::onDeleteAppliance)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        buttonAddAppliance.setOnClickListener {
            val name = editTextApplianceName.text.toString()
            val powerConsumption = editTextPowerConsumption.text.toString()

            if (name.isNotEmpty() && powerConsumption.isNotEmpty()) {
                addAppliance(name, powerConsumption.toDouble())
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        fetchAppliances()

        return view
    }

    private fun fetchAppliances() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getAppliances(token)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val newAppliances = response.body()!!

                        adapter.updateList(newAppliances)

                        if (newAppliances.isEmpty()) {
                            textViewEmptyMessage.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        } else {
                            textViewEmptyMessage.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao obter os eletrodomésticos: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun addAppliance(name: String, powerConsumption: Double) {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        val applianceRequest = ApplianceRequest(name, powerConsumption)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.addAppliance(token, applianceRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        editTextApplianceName.text.clear()
                        editTextPowerConsumption.text.clear()
                        fetchAppliances()
                        Toast.makeText(requireContext(), "Eletrodoméstico adicionado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao adicionar eletrodoméstico: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun onEditAppliance(appliance: Appliance) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_appliance, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextApplianceName)
        val editTextPower = dialogView.findViewById<EditText>(R.id.editTextPowerConsumption)

        editTextName.setText(appliance.applianceName)
        editTextPower.setText(appliance.powerConsumption.toString())

        val dialog = android.app.AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            .setTitle("Editar Eletrodoméstico")
            .setView(dialogView)
            .setPositiveButton("Salvar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)

            // Personalizar botão "Salvar" (verde)
            positiveButton.setTextColor(requireContext().getColor(R.color.green))
            positiveButton.setOnClickListener {
                val newName = editTextName.text.toString()
                val newPower = editTextPower.text.toString().toDoubleOrNull()
                if (newName.isNotEmpty() && newPower != null) {
                    updateAppliance(appliance.id, newName, newPower)
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Dados inválidos", Toast.LENGTH_SHORT).show()
                }
            }

            // Personalizar botão "Cancelar" (vermelho)
            negativeButton.setTextColor(requireContext().getColor(R.color.red))
            negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }



    private fun updateAppliance(id: Long, name: String, powerConsumption: Double) {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        val applianceRequest = ApplianceRequest(name, powerConsumption)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.updateAppliance(token, id, applianceRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        fetchAppliances()
                        Toast.makeText(requireContext(), "Eletrodoméstico atualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao atualizar eletrodoméstico: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onDeleteAppliance(appliance: Appliance) {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.deleteAppliance(token, appliance.id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        fetchAppliances()
                        Toast.makeText(requireContext(), "Eletrodoméstico excluído", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao excluir eletrodoméstico: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
