package com.example.inspector.Controller.Fragment

import android.app.Activity
import android.app.AlertDialog
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
import com.bumptech.glide.Glide
import com.example.inspector.Controller.Activity.MainActivity
import com.example.inspector.R
import com.example.inspector.Utils.CameraAndGallery
import com.example.inspector.Utils.Strings
import com.example.inspector.Utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.io.File


class ProfileFragment : Fragment() {
    private val TAG = "ProfileFragment"

    private lateinit var auth: FirebaseAuth
    private lateinit var cameraAndGallery: CameraAndGallery
    private var photoURL: Uri? = null

    private lateinit var imageview: ImageView
    private lateinit var editProfileText: TextView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var progressBar: ProgressBar

    private val CAMERA_REQUEST = 101
    private val GALLERY_REQUEST = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        cameraAndGallery = CameraAndGallery(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        imageview = root.findViewById(R.id.profileImageView)
        editProfileText = root.findViewById(R.id.editProfileText)
        nameEditText = root.findViewById(R.id.nameEditTextProfile)
        emailEditText = root.findViewById(R.id.emailEditTextProfile)
        saveButton = root.findViewById(R.id.saveProfileButton)
        progressBar = root.findViewById(R.id.profileProgressBar)

        setupUI(auth.currentUser)
        changeProfileImage()
        saveUserInformation(auth.currentUser!!)
        return root
    }

    private fun setupUI(currentUser: FirebaseUser?) {
        nameEditText.setText(currentUser?.displayName)
        emailEditText.isEnabled = false
        emailEditText.setText(currentUser?.email)
        updatePhoto(currentUser?.photoUrl)

    }

    private fun updatePhoto(uri: Uri?) {
        Glide
            .with(this)
            .load(uri)
            .placeholder(R.drawable.loading_spinner)
            .error(R.drawable.ic_account_circle_24dp)
            .circleCrop()
            .into(imageview)
    }

    private fun changeProfileImage() {
        editProfileText.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setMessage("Por favor escolha uma das opções abaixo para atualizar a sua foto.")
                .setPositiveButton(R.string.camera) {_, _ ->
                    cameraAndGallery.takePhoto(CAMERA_REQUEST)
                }
                .setNeutralButton(R.string.gallery) {_, _->
                    cameraAndGallery.openGallery(GALLERY_REQUEST)
                }
            alertBuilder.show()
        }
    }

    private fun saveUserInformation(currentUser: FirebaseUser) {
        saveButton.setOnClickListener {
            if (nameEditText.text.isBlank()) {
                nameEditText.error = Strings.get(R.string.mandatory_default_field)
                return@setOnClickListener
            }
            val profilePhoto = if (photoURL != null) {
                photoURL
            } else {
                currentUser.photoUrl
            }
            val name = nameEditText.text.toString()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(profilePhoto)
                .build()
            showProgressBar()
            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Utils.alert("Perfil atualizado com sucesso!")
                        activity?.supportFragmentManager?.popBackStack()
                        hideProgressBar()
                    }
                }
                .addOnFailureListener {
                    Utils.alert("Houve um erro ao atualizar o perfil")
                    Log.w(TAG, "Erro ao atualizar perfil", it)
                    hideProgressBar()
                }
        }
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
                        photoURL = FileProvider.getUriForFile(context!!, "com.example.android.fileprovider", file)
                        updatePhoto(photoURL)

                    }
                    GALLERY_REQUEST -> {
                        if (data == null || data.data == null) {
                            return
                        }
                        photoURL = data.data!!
                        updatePhoto(photoURL)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
       progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}

