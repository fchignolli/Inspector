package com.example.inspector.Controller.Fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Controller.Adapter.FormImageAdapter
import com.example.inspector.Model.Image
import com.example.inspector.Model.Room
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

class RoomFromFragment : Fragment() {
    val TAG = "RoomFromFragment"
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var collectionReference: CollectionReference
    private lateinit var cameraAndGallery: CameraAndGallery
    private var imagesList: ArrayList<Image> = ArrayList()
    private var imagesReference: ArrayList<StorageReference> = ArrayList()
    // initial data
    private lateinit var dateTextLayout: TextInputLayout
    private lateinit var placeNameTextLayout: TextInputLayout
    private lateinit var inspectedNameTextLayout: TextInputLayout
    // layout
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
    private lateinit var observationsEditText: EditText
    private lateinit var imagesRecyclerView: RecyclerView
    //buttons
    private lateinit var cameraTextView: TextView
    private lateinit var galleryTextView: TextView
    private lateinit var sendButton: Button
    // constans
    private val currentDate = Calendar.getInstance().time
    private val CAMERA_REQUEST = 101
    private val GALLERY_REQUEST = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()
        storageReference = Firebase.storage.reference.child("users").child(auth.currentUser?.uid.toString()).child("room_form_images")
        collectionReference = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("room_form")
        cameraAndGallery = CameraAndGallery(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_room_from, container, false)
        dateTextLayout = root.findViewById(R.id.dateRoomTextInput)
        placeNameTextLayout = root.findViewById(R.id.placeNameRoomTextInput)
        inspectedNameTextLayout = root.findViewById(R.id.inspectedNameRoomTextInput)
        securityLightCheckBox = root.findViewById(R.id.securityLightCheckBox)
        electronicDeviceCheckBox = root.findViewById(R.id.electronicDeviceCheckBox)
        communicateDeviceCheckBox = root.findViewById(R.id.communicateDeviceCheckBox)
        openDeviceCheckBox = root.findViewById(R.id.openDeviceCheckBox)
        openExternalDeviceCheckBox = root.findViewById(R.id.openExtDeviceCheckBox)
        lightingDeviceCheckBox = root.findViewById(R.id.lighthingDeviceCheckBox)
        buttonsCheckBox = root.findViewById(R.id.ButtonsShouldCheckBox)
        symbolsCheckBox = root.findViewById(R.id.symbolsCheckBox)
        informationCheckbox = root.findViewById(R.id.infoCheckBox)
        actionPlanCheckBox = root.findViewById(R.id.actionPlanCheckBox)
        observationsEditText = root.findViewById(R.id.observationRoomEditText)
        imagesRecyclerView = root.findViewById(R.id.imagesRoomRecyclerView)
        cameraTextView = root.findViewById(R.id.cameraRoomTextView)
        galleryTextView = root.findViewById(R.id.galleryRoomTextView)
        sendButton = root.findViewById(R.id.sendRoomButton)
        return root
    }

    override fun onStart() {
        super.onStart()
        clickEvents()
        configureDate()
    }

    private fun clickEvents() {
        sendButton.setOnClickListener {
            saveRoomForm(getRoomData())
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

    private fun configureDate() {
        val sdf: DateFormat = SimpleDateFormat.getDateTimeInstance()
        dateTextLayout.editText?.setText(sdf.format(currentDate))
        dateTextLayout.isEnabled = false
    }

    private fun saveRoomForm(room: Room) {
        val documentReference = collectionReference.document()
        room.id = documentReference.id
        documentReference.set(room)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.alert("Formulário de controle para ${room.placeName} foi salvo com sucesso!!")
                } else {
                    Log.w(TAG, "Erro ao salvar no banco")
                }
            }
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun getRoomData(): Room {
        return Room(
            "",
            currentDate,
            placeNameTextLayout.editText?.text.toString(),
            inspectedNameTextLayout.editText?.text.toString(),
            securityLightCheckBox.isChecked,
            electronicDeviceCheckBox.isChecked,
            communicateDeviceCheckBox.isChecked,
            openDeviceCheckBox.isChecked,
            openExternalDeviceCheckBox.isChecked,
            lightingDeviceCheckBox.isChecked,
            buttonsCheckBox.isChecked,
            symbolsCheckBox.isChecked,
            informationCheckbox.isChecked,
            actionPlanCheckBox.isChecked,
            observationsEditText.text.toString(),
            imagesList
        )
    }

    private fun setupImageRecyclerView() {
        imagesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        imagesRecyclerView.adapter = FormImageAdapter(imagesList, imagesReference, context)
        imagesRecyclerView.setHasFixedSize(true)
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
