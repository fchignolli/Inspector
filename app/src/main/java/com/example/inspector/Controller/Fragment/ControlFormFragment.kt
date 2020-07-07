package com.example.inspector.Controller.Fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.inspector.Model.Control
import com.example.inspector.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ControlFormFragment : Fragment() {
    val TAG = "ControlFormFragment"
    // Firebase pre sets
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var collectionReference: CollectionReference

    private lateinit var dateTextLayout: TextInputLayout
    private lateinit var inspectedNameTextLayout: TextInputLayout
    private lateinit var placeNameTextLayout: TextInputLayout
    private lateinit var workLoadTextLayout: TextInputLayout
    private lateinit var usageFactorsTextLayout: TextInputLayout
    private lateinit var occupationFacTextLayout: TextInputLayout
    private lateinit var primaryAttenuationTextLayout: TextInputLayout
    private lateinit var integritySpinner: Spinner
    private lateinit var assessmentPrimaryMultText: EditText
    private lateinit var assessmentSecondMultText: EditText
    private lateinit var angulationDataEditText: EditText
    private lateinit var recommendedActionEditText: EditText
    private lateinit var observationTextLayout: TextInputLayout
    private lateinit var sendControlButton: Button

    // CONSTANTS
    private val currentDate = Calendar.getInstance().time

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_control_form, container, false)
        mDatabase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        collectionReference = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("controlForm")

        dateTextLayout = root.findViewById(R.id.dateTextLayout)
        inspectedNameTextLayout = root.findViewById(R.id.inpectedNameTextLayout)
        placeNameTextLayout = root.findViewById(R.id.namePlaceTextLayout)
        workLoadTextLayout = root.findViewById(R.id.workLoadTextLayout)
        usageFactorsTextLayout = root.findViewById(R.id.usageFactorsTextLayout)
        occupationFacTextLayout = root.findViewById(R.id.occupationFacTextLayout)
        primaryAttenuationTextLayout = root.findViewById(R.id.primaryAtenuationTextLayout)
        integritySpinner = root.findViewById(R.id.IntegritySpinner)
        assessmentPrimaryMultText = root.findViewById(R.id.assessmentPrimaryShieldMuiltText)
        assessmentSecondMultText = root.findViewById(R.id.assessmentSecondShieldMuiltText)
        angulationDataEditText = root.findViewById(R.id.agulationDataEditText)
        recommendedActionEditText = root.findViewById(R.id.recommendedActionsEditText)
        observationTextLayout = root.findViewById(R.id.observationTextLayout)
        sendControlButton = root.findViewById(R.id.sendControlButton)

        configureDate()
        configureSpinner(integritySpinner)
        clickEvents()
        return root
    }

    private fun clickEvents() {
        sendControlButton.setOnClickListener {
            validateData()
            saveControlForm(getControlData())
        }
    }

    private fun saveControlForm(control: Control) {
        val documentReference = collectionReference.document()
        control.id = documentReference.id
        documentReference.set(control)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Formulário preenchido com sucesso!")
                } else {
                    Log.w(TAG, "Erro ao salvar no banco")
                }
            }
    }

    private fun validateData() {
        //TODO: - Verificar quais são os que não podem ir null
    }

    private fun getControlData(): Control {
        return Control(
            "",
            currentDate,
            placeNameTextLayout.editText?.text.toString(),
            inspectedNameTextLayout.editText?.text.toString(),
            workLoadTextLayout.editText?.text.toString(),
            usageFactorsTextLayout.editText?.text.toString(),
            occupationFacTextLayout.editText?.text.toString(),
            primaryAttenuationTextLayout.editText?.text.toString(),
            integritySpinner.selectedItem.toString(),
            assessmentPrimaryMultText.text.toString(),
            assessmentSecondMultText.text.toString(),
            angulationDataEditText.text.toString(),
            recommendedActionEditText.text.toString(),
            observationTextLayout.editText?.text.toString()
        )
    }

    private fun configureSpinner(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            context,
            R.array.yes_no_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun configureDate() {
        val sdf: DateFormat = SimpleDateFormat.getDateInstance()
        dateTextLayout.editText?.setText(sdf.format(currentDate))
        dateTextLayout.isEnabled = false
    }
}
