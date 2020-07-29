package com.example.inspector.Controller.Fragment.Tool

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Controller.Adapter.FormImageAdapter
import com.example.inspector.Model.Tool
import com.example.inspector.R

/**
 * Created by Lucas Alves dos Santos on 29/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class ToolDetailsFragment : Fragment() {
    private val TAG = "RoomDetailsFragment"
    private lateinit var dateEditText: EditText
    private lateinit var placeNameEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var barometerEditText: EditText
    private lateinit var thermometerEditText: EditText
    private lateinit var rulerEditText: EditText
    private lateinit var simulatorCheckbox: CheckBox
    private lateinit var laserCheckbox: CheckBox
    private lateinit var stabilityCheckBox: CheckBox
    private lateinit var observationEditText: EditText
    private lateinit var imageRecyclerView: RecyclerView

    private lateinit var tool: Tool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tool = arguments?.getSerializable("tool") as Tool
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tool_details, container, false)
        dateEditText = root.findViewById(R.id.dateToolDetEditText)
        placeNameEditText = root.findViewById(R.id.placeNameToolDetEditText)
        nameEditText = root.findViewById(R.id.nameToolDetEditText)
        barometerEditText = root.findViewById(R.id.barometerDetailsEditText)
        thermometerEditText = root.findViewById(R.id.thermometerDetEditText)
        rulerEditText = root.findViewById(R.id.rulerDetEditText)
        simulatorCheckbox = root.findViewById(R.id.simuladorToolDetCheckbox)
        laserCheckbox = root.findViewById(R.id.laserToolDetCheckbox)
        stabilityCheckBox = root.findViewById(R.id.stabilityToolDetCheckBox)
        observationEditText = root.findViewById(R.id.observationToolDetEditText)
        imageRecyclerView = root.findViewById(R.id.imageToolDetRecyclerView)
        return root
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    private fun setupUI() {
        val editTexts = getArrayOfEditTexts()
        val checkBoxes = getArrayOfCheckBoxes()
        configureEditTexts(editTexts, false)
        configureCheckBoxes(checkBoxes, false)
        setValuesOnView()
        setupImageRecyclerView()
    }

    private fun setupImageRecyclerView() {
        imageRecyclerView.layoutManager = GridLayoutManager(context, 2)
        imageRecyclerView.adapter = FormImageAdapter(tool.images, ArrayList(), context, canRemove = false)
        imageRecyclerView.setHasFixedSize(true)
    }

    private fun setValuesOnView() {
        val date = "${tool.getUserDate()}  ${tool.getUserTime()}"
        dateEditText.setText(date)
        placeNameEditText.setText(tool.placeName)
        nameEditText.setText(tool.inspectedName)
        barometerEditText.setText(tool.barometerResolution)
        thermometerEditText.setText(tool.thermometerResolution)
        rulerEditText.setText(tool.rulerResolution)
        simulatorCheckbox.isChecked = tool.hasSimulatorDosimetry
        laserCheckbox.isChecked = tool.hasLaserAligner
        stabilityCheckBox.isChecked = tool.hasStabilityChecker
        observationEditText.setText(tool.observation)
    }

    private fun getArrayOfEditTexts(): ArrayList<EditText> {
        return arrayListOf(
            dateEditText,
            placeNameEditText,
            nameEditText,
            barometerEditText,
            thermometerEditText,
            rulerEditText,
            observationEditText
        )
    }

    private fun getArrayOfCheckBoxes(): ArrayList<CheckBox> {
        return arrayListOf(
            simulatorCheckbox,
            laserCheckbox,
            stabilityCheckBox
        )
    }

    private fun configureCheckBoxes(checkboxes: ArrayList<CheckBox>, isEnabled: Boolean) {
        for(checkbox in checkboxes) {
            checkbox.isClickable = isEnabled
        }
    }

    private fun configureEditTexts(editTexts: ArrayList<EditText>, isEnabled: Boolean) {
        for(editText in editTexts) {
            editText.isEnabled = isEnabled
        }
    }

}
