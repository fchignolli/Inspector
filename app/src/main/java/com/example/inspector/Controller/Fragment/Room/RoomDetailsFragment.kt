package com.example.inspector.Controller.Fragment.Room

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
import com.example.inspector.Model.Control
import com.example.inspector.Model.Room
import com.example.inspector.R

/**
 * Created by Lucas Alves dos Santos on 29/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class RoomDetailsFragment : Fragment() {
    private val TAG = "RoomDetailsFragment"
    private lateinit var dateEditText: EditText
    private lateinit var placeNameEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var securityLightCheckBox: CheckBox
    private lateinit var electronicDeviceCheckBox: CheckBox
    private lateinit var communicateDeviceCheckBox: CheckBox
    private lateinit var openDeviceCheckBox: CheckBox
    private lateinit var openExternalDeviceCheckBox: CheckBox
    private lateinit var lightingDeviceCheckBox: CheckBox
    private lateinit var buttonsCheckBox: CheckBox
    private lateinit var symbolsCheckBox: CheckBox
    private lateinit var informationCheckbox: CheckBox
    private lateinit var actionPlanCheckBox: CheckBox
    private lateinit var observationEditText: EditText
    private lateinit var imageRecyclerView: RecyclerView

    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room = arguments?.getSerializable("room") as Room
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_room_details, container, false)
        dateEditText = root.findViewById(R.id.dateRoomDetEditText)
        placeNameEditText = root.findViewById(R.id.placeNameRoomDetEditText)
        nameEditText = root.findViewById(R.id.nameRoomDetEditText)
        securityLightCheckBox = root.findViewById(R.id.securityLightRoomDetCheckBox)
        electronicDeviceCheckBox = root.findViewById(R.id.electronicDeviceRoomDetCheckBox)
        communicateDeviceCheckBox = root.findViewById(R.id.communicateDeviceRoomDetCheckBox)
        openDeviceCheckBox = root.findViewById(R.id.openDeviceRoomDetCheckBox)
        openExternalDeviceCheckBox = root.findViewById(R.id.openExtDeviceRoomDetCheckBox)
        lightingDeviceCheckBox = root.findViewById(R.id.lighthingDeviceRoomDetCheckBox)
        buttonsCheckBox = root.findViewById(R.id.ButtonsShouldRoomDetCheckBox)
        symbolsCheckBox = root.findViewById(R.id.symbolsRoomDetCheckBox)
        informationCheckbox = root.findViewById(R.id.infoRoomDetCheckBox)
        actionPlanCheckBox = root.findViewById(R.id.actionPlanRoomDetCheckBox)
        observationEditText = root.findViewById(R.id.observationRoomDetEditText)
        imageRecyclerView = root.findViewById(R.id.imageRoomDetRecyclerView)
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
        imageRecyclerView.adapter = FormImageAdapter(room.images, ArrayList(), context, canRemove = false)
        imageRecyclerView.setHasFixedSize(true)
    }

    private fun setValuesOnView() {
        val date = "${room.getUserDate()}  ${room.getUserTime()}"
        dateEditText.setText(date)
        placeNameEditText.setText(room.placeName)
        nameEditText.setText(room.inspectedName)
        securityLightCheckBox.isChecked = room.hasSecurityLight
        electronicDeviceCheckBox.isChecked = room.hasElectronicDevice
        communicateDeviceCheckBox.isChecked = room.hasCommunicateDevice
        openDeviceCheckBox.isChecked = room.hasOpenDevice
        openExternalDeviceCheckBox.isChecked = room.hasOpenExternalDevice
        lightingDeviceCheckBox.isChecked = room.hasLightingDevice
        buttonsCheckBox.isChecked = room.hasButtons
        symbolsCheckBox.isChecked = room.hasSymbol
        informationCheckbox.isChecked = room.hasInformation
        actionPlanCheckBox.isChecked = room.hasActionPlan
        observationEditText.setText(room.observation)
    }

    private fun getArrayOfEditTexts(): ArrayList<EditText> {
        return arrayListOf(
            dateEditText,
            placeNameEditText,
            nameEditText,
            observationEditText
        )
    }

    private fun getArrayOfCheckBoxes(): ArrayList<CheckBox> {
        return arrayListOf(
            securityLightCheckBox,
            electronicDeviceCheckBox,
            communicateDeviceCheckBox,
            openDeviceCheckBox,
            openExternalDeviceCheckBox,
            lightingDeviceCheckBox,
            buttonsCheckBox,
            symbolsCheckBox,
            informationCheckbox,
            actionPlanCheckBox
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
