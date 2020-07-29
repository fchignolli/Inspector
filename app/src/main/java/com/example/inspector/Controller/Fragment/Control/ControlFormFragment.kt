package com.example.inspector.Controller.Fragment.Control

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Controller.Adapter.FormImageAdapter
import com.example.inspector.Model.Control
import com.example.inspector.Model.Image
import com.example.inspector.R
import com.example.inspector.Utils.CameraAndGallery
import com.example.inspector.Utils.Utils
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ControlFormFragment : Fragment() {
    val TAG = "ControlFormFragment"
    // Firebase pre sets
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var collectionReference: CollectionReference
    private lateinit var cameraAndGallery: CameraAndGallery
    private var imagesList: ArrayList<Image> = ArrayList()
    private var imagesReference: ArrayList<StorageReference> = ArrayList()
    // Edit texts
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
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var cameraTextView: TextView
    private lateinit var galleryTextView: TextView
    private lateinit var sendControlButton: Button
    // CONSTANTS
    private val currentDate = Calendar.getInstance().time
    private val CAMERA_REQUEST = 101
    private val GALLERY_REQUEST = 102


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_control_form, container, false)
        mDatabase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storageReference = Firebase.storage.reference.child("users").child(auth.currentUser?.uid.toString()).child("control_form_images")
        collectionReference = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("control_form")
        cameraAndGallery = CameraAndGallery(this)

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
        imageRecyclerView = root.findViewById(R.id.imageRecyclerView)
        //Buttons
        cameraTextView = root.findViewById(R.id.cameraTextView)
        galleryTextView = root.findViewById(R.id.galleryTextView)
        sendControlButton = root.findViewById(R.id.sendControlButton)
        return root
    }

    override fun onStart() {
        super.onStart()
        configureDate()
        configureSpinner(integritySpinner)
        clickEvents()
    }

    private fun setupImageRecyclerView() {
        imageRecyclerView.layoutManager = GridLayoutManager(context, 2)
        imageRecyclerView.adapter = FormImageAdapter(imagesList, imagesReference, context)
        imageRecyclerView.setHasFixedSize(true)
    }

    private fun clickEvents() {
        sendControlButton.setOnClickListener {
            validateData()
            saveControlForm(getControlData())
        }
        // take photo
        cameraTextView.setOnClickListener {
            cameraAndGallery.takePhoto(CAMERA_REQUEST)
        }

        // catch image from gallery
        galleryTextView.setOnClickListener {
            cameraAndGallery.openGallery(GALLERY_REQUEST)
        }
    }

    private fun saveControlForm(control: Control) {
        val documentReference = collectionReference.document()
        control.id = documentReference.id
        documentReference.set(control)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.alert("Formulário de controle para ${control.placeName} foi salvo com sucesso!!")
                } else {
                    Log.w(TAG, "Erro ao salvar no banco")
                }
            }
        activity?.supportFragmentManager?.popBackStack()
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
            observationTextLayout.editText?.text.toString(),
            imagesList
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

    private fun configureDate() {
        val sdf: DateFormat = SimpleDateFormat.getDateTimeInstance()
        dateTextLayout.editText?.setText(sdf.format(currentDate))
        dateTextLayout.isEnabled = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraAndGallery.goToCamera(CAMERA_REQUEST)
                }
            }
            GALLERY_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraAndGallery.goToGallery(GALLERY_REQUEST)
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    CAMERA_REQUEST -> {
                        val filename = cameraAndGallery.currentFilename
                        val file = File(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)
                        val uri: Uri = FileProvider.getUriForFile(context!!, "com.example.android.fileprovider", file)
                        val imageRef = storageReference.child(uri.lastPathSegment!!)
                        val uploadTask = imageRef.putFile(uri)
                        uploadImage(uploadTask, imageRef, uri.lastPathSegment!!)
                    }
                    GALLERY_REQUEST -> {
                        if (data == null || data.data == null) {
                            return
                        }
                        val uri = data.data!!
                        val imageRef = storageReference.child(uri.lastPathSegment!!)
                        val uploadTask = imageRef.putFile(uri)
                        uploadImage(uploadTask, imageRef, uri.lastPathSegment!!)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun uploadImage(uploadTask: UploadTask, storageReference: StorageReference, fileName: String) {
        uploadTask
            .addOnSuccessListener { taskSnapShot ->
                val downloadUri = taskSnapShot.metadata?.reference!!.downloadUrl
                downloadUri.addOnSuccessListener {uri ->
                    val image = Image(
                        UUID.randomUUID().toString(),
                        fileName,
                        currentDate,
                        "",
                        uri.toString()
                    )
                    imagesList.add(image)
                    imagesReference.add(storageReference)
                    setupImageRecyclerView()
                }
            }
            .addOnFailureListener {
                Utils.alert("Error ao fazer upload na image")
            }
    }
}
