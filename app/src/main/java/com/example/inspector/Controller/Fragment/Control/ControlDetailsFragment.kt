package com.example.inspector.Controller.Fragment.Control

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Controller.Adapter.FormImageAdapter
import com.example.inspector.Model.Control
import com.example.inspector.R

class ControlDetailsFragment : Fragment() {
    private val TAG = "ControlDetailsFragment"
    private lateinit var dateEditText: EditText
    private lateinit var placeNameEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var workLoadTextEditText: EditText
    private lateinit var usageFactorsEditText: EditText
    private lateinit var occupationFacEditText: EditText
    private lateinit var primaryAttenuationEditText: EditText
    private lateinit var integrityEditText: EditText
    private lateinit var assessmentPrimaryEditText: EditText
    private lateinit var assessmentSecondEditText: EditText
    private lateinit var angulationEditText: EditText
    private lateinit var observationEditText: EditText
    private lateinit var recommendedActionEditText: EditText
    private lateinit var imageRecyclerView: RecyclerView

    private lateinit var control: Control

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        control = arguments?.getSerializable("control") as Control
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_control_details, container, false)
        dateEditText = root.findViewById(R.id.dateEditText)
        placeNameEditText = root.findViewById(R.id.placeNameEditText)
        nameEditText = root.findViewById(R.id.nameEditText)
        workLoadTextEditText = root.findViewById(R.id.workLoadEditText)
        usageFactorsEditText = root.findViewById(R.id.usageFactEditText)
        occupationFacEditText = root.findViewById(R.id.occupationFactorsEditText)
        primaryAttenuationEditText = root.findViewById(R.id.primaryAtenuationEditText)
        integrityEditText = root.findViewById(R.id.integryEdttText)
        assessmentPrimaryEditText = root.findViewById(R.id.assessmentPrimaryEditText)
        assessmentSecondEditText = root.findViewById(R.id.assessmentSecondEditText)
        angulationEditText = root.findViewById(R.id.angulationEditText)
        observationEditText = root.findViewById(R.id.observationEditText)
        recommendedActionEditText = root.findViewById(R.id.recommendationEditText)
        imageRecyclerView = root.findViewById(R.id.imageRecyclerViewControlDetails)
        return root
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    private fun setupUI() {
        val editTexts = getArrayOfEditTexts()
        configureEditTexts(editTexts, false)
        setTextOnEditTexts()
        setupImageRecyclerView()
    }

    private fun configureEditTexts(editTexts: ArrayList<EditText>, isEnabled: Boolean) {
        for(editText in editTexts) {
            editText.isEnabled = isEnabled
        }
    }

    private fun setupImageRecyclerView() {
        imageRecyclerView.layoutManager = GridLayoutManager(context, 2)
        imageRecyclerView.adapter = FormImageAdapter(control.images, ArrayList(), context, canRemove = false)
        imageRecyclerView.setHasFixedSize(true)
    }

    private fun setTextOnEditTexts() {
        val date = "${control.getUserDate()}  ${control.getUserTime()}"
        dateEditText.setText(date)
        placeNameEditText.setText(control.placeName)
        nameEditText.setText(control.inspectedName)
        workLoadTextEditText.setText(control.workLoad)
        usageFactorsEditText.setText(control.usageFactors)
        occupationFacEditText.setText(control.occupation)
        primaryAttenuationEditText.setText(control.primaryAttenuation)
        integrityEditText.setText(control.integrity)
        assessmentPrimaryEditText.setText(control.assessmentPrimary)
        assessmentSecondEditText.setText(control.assessmentSecond)
        angulationEditText.setText(control.angulationData)
        observationEditText.setText(control.observation)
        recommendedActionEditText.setText(control.recommendedAction)
    }

    private fun getArrayOfEditTexts(): ArrayList<EditText> {
        return arrayListOf(
            dateEditText,
            placeNameEditText,
            nameEditText,
            workLoadTextEditText,
            usageFactorsEditText,
            occupationFacEditText,
            primaryAttenuationEditText,
            integrityEditText,
            assessmentPrimaryEditText,
            assessmentSecondEditText,
            angulationEditText,
            observationEditText,
            recommendedActionEditText
        )
    }
}
