package com.example.inspector.Utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucas Alves dos Santos on 08/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class CameraAndGallery(private var fragment: Fragment) {
    lateinit var currentPhotoPath: String
    lateinit var currentFilename: String
    // CAMERA
    fun takePhoto(REQUEST_IMAGE_CAPTURE: Int) {
        if (ContextCompat.checkSelfPermission(fragment.context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission(REQUEST_IMAGE_CAPTURE)
        } else {
            goToCamera(REQUEST_IMAGE_CAPTURE)
        }
    }

    fun goToCamera(REQUEST_IMAGE_CAPTURE: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(fragment.context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        fragment.context!!,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat.getDateInstance().format(Date())
        val storageDir: File = fragment.activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            currentFilename = name
        }
    }

    private fun requestCameraPermission(REQUEST_IMAGE_CAPTURE: Int) {
        ActivityCompat.requestPermissions(fragment.activity!!, arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
    }

    // GALERIA
    fun openGallery(REQUEST_IMAGE_CAPTURE: Int) {
        if (ContextCompat.checkSelfPermission(fragment.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadPermission(REQUEST_IMAGE_CAPTURE)
        } else {
            goToGallery(REQUEST_IMAGE_CAPTURE)
        }
    }

    fun goToGallery(REQUEST_IMAGE_CAPTURE: Int) {
        Intent(Intent.ACTION_PICK).also { openGalleryIntent ->
            openGalleryIntent.resolveActivity(fragment.context!!.packageManager)?.also {
                openGalleryIntent.type = "image/*"
                fragment.startActivityForResult(openGalleryIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun requestReadPermission(REQUEST_OPEN_GALLERY: Int) {
        ActivityCompat.requestPermissions(fragment.activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_OPEN_GALLERY)
    }
}