package com.rachidbs.todo.userInfo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.rachidbs.todo.BuildConfig
import com.rachidbs.todo.databinding.ActivityUserInfoBinding
import com.rachidbs.todo.network.Api
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@ExperimentalSerializationApi
class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding;
    private val cameraPermissionMsg: String = "On a besoin de la caméra sivouplé ! 🥺"
    private val galleryPermissionMsg: String = "On a besoin de la galerie sivouplé ! 🥺"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater);
        setContentView(binding.root)
        fetUserInfo()
    }

    private fun fetUserInfo() = lifecycleScope.launch {
        val userInfo = Api.USER_WEB_SERVICE.getInfo().body()!!
        binding.imageView.load(userInfo.avatar) {
            transformations(CircleCropTransformation())
        };
    }

    fun takePictureClick(view: View) {
        askCameraPermissionAndOpenCamera()
    }

    fun pickInGalleryClick(view: View) {
        askGalleryPermissionAndPickInGallery()
    }

    private val photoUri by lazy {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".fileprovider",
            File.createTempFile("avatar", ".jpeg", externalCacheDir)
        )
    }


    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) handleImage(photoUri)
            else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
        }

    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            lifecycleScope.launch {
                if (uri != null)
                    Api.USER_WEB_SERVICE.updateAvatar(convert(uri)).body()!!;
                    finish();
            }
        }

    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )

    private fun handleImage(uri: Uri) {
        lifecycleScope.launch {
            Api.USER_WEB_SERVICE.updateAvatar(convert(uri)).body()!!;
            finish();
        }
    }

    private fun openCamera() = takePicture.launch(photoUri)

    private fun pickPicture() = pickInGallery.launch("image/*")


    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) openCamera()
            else showCameraExplanationDialog()
        }

    private val requestGalleyPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) pickPicture()
            else showGalleryExplanationDialog()
        }

    private fun requestCameraPermission() =
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun requestGalleryPermission() =
        requestGalleyPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showCameraExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun askGalleryPermissionAndPickInGallery() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> pickPicture()
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showGalleryExplanationDialog()
            else -> requestGalleryPermission()
        }
    }

    private fun showGalleryExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage(galleryPermissionMsg)
            setPositiveButton("Bon, ok") { _, _ ->
                requestGalleryPermission()
            }
            setCancelable(true)
            show()
        }
    }

    private fun showCameraExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage(cameraPermissionMsg)
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }
}