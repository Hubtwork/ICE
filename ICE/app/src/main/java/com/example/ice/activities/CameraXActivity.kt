package com.example.ice.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.ice.R
import com.example.ice.models.Component
import com.example.ice.utils.DebugLogger
import com.example.ice.utils.PermissionManager
import com.google.rpc.Code
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraActivity"
    }

    private lateinit var filterSet: List<Component>

    private lateinit var previewView: PreviewView
    private lateinit var scanButton: ImageView
    private lateinit var filterButton: ImageView
    private lateinit var filterCheckButton: ImageView

    private lateinit var frameLayoutShutter: FrameLayout
    private lateinit var frameLayoutPreview: FrameLayout

    private lateinit var imageViewPreview: ImageView
    private lateinit var imageViewCancel: ImageView

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var cameraAnimationListener: Animation.AnimationListener

    private var savedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_main)

        filterSet = listOf()

        permissionCheck()
        findView()
        setListener()
        setCameraAnimationListener()

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun permissionCheck() {

        var permissionList =
            listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!PermissionManager.checkPermission(this, permissionList)) {
            PermissionManager.requestPermission(this, permissionList)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            DebugLogger.log(TAG, "승인")
            openCamera()
        } else {
            DebugLogger.log(TAG, "승인 거부")
            onBackPressed()
        }
    }

    private fun findView() {
        previewView = findViewById(R.id.previewView)
        scanButton = findViewById(R.id.scanButton)
        filterButton = findViewById(R.id.filterButton)
        filterCheckButton = findViewById(R.id.button_current_filter_check)

        frameLayoutShutter = findViewById(R.id.frameLayoutShutter)
        imageViewPreview = findViewById(R.id.imageViewPreview)
        frameLayoutPreview = findViewById(R.id.frameLayoutPreview)

        imageViewCancel = findViewById(R.id.imageViewCancel)
    }

    private fun setListener() {

        filterCheckButton.setOnClickListener {
            /**
                Check Current Filter Button
                - click : Get Current set-Filters
             */
            checkCurrentFilter()
        }

        scanButton.setOnClickListener {
            /**
                Scan Button
                - click : Saving Photo + Request API
                - Trigger : FrameLayoutShutter Fragment Showing
             */
            savePhoto()
        }

        filterButton.setOnClickListener {
            /**
                Filter Button
                - click : Open Filter Setting Intent
             */
            openFilterSettingIntent()
        }

        imageViewCancel.setOnClickListener {
            if(frameLayoutPreview.visibility == View.VISIBLE) {
                hideCaptureImage()
            }
        }
    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun openCamera() {
        DebugLogger.log(TAG, "openCamera")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                DebugLogger.log(TAG, "바인딩 성공")

            } catch (e: Exception) {
                DebugLogger.log(TAG, "바인딩 실패 $e")
            }
        }, ContextCompat.getMainExecutor(this))

    }

    private fun savePhoto() {
        imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yy-mm-dd", Locale.US).format(System.currentTimeMillis()) + ".png"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    DebugLogger.log(TAG, "savedUri : $savedUri")

                    val animation =
                        AnimationUtils.loadAnimation(this@CameraXActivity, R.anim.camera_shutter)
                    animation.setAnimationListener(cameraAnimationListener)
                    frameLayoutShutter.animation = animation
                    frameLayoutShutter.visibility = View.VISIBLE
                    frameLayoutShutter.startAnimation(animation)

                    DebugLogger.log(TAG, "imageCapture")
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    onBackPressed()
                }

            })

    }

    private fun setCameraAnimationListener() {
        cameraAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                frameLayoutShutter.visibility = View.GONE
                showCaptureImage()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        }
    }

    private fun showCaptureImage(): Boolean {
        if (frameLayoutPreview.visibility == View.GONE) {
            frameLayoutPreview.visibility = View.VISIBLE
            imageViewPreview.setImageURI(savedUri)
            return false
        }

        return true

    }

    private fun hideCaptureImage() {
        imageViewPreview.setImageURI(null)
        frameLayoutPreview.visibility = View.GONE

    }

    // SideEffects
    private fun checkCurrentFilter() {
        var currentFilter : String = ""
        currentFilter = if (filterSet.isEmpty()) {
            "Empty Filter Set"
        } else {
            filterSet.joinToString("\n")
        }
        Toast.makeText(this, currentFilter, Toast.LENGTH_LONG).show()
    }

    private fun openFilterSettingIntent() {
        // Success Code = 100
        val intent = Intent(this, SettingActivity::class.java)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val filtersFromIntent = data!!.getStringArrayListExtra("components")
                    if (!filtersFromIntent.isNullOrEmpty()) {
                        var tempFilters = mutableListOf<Component>()
                        filtersFromIntent.map {
                            tempFilters.add( Component(it, R.drawable.chemistry, "component") )
                        }
                        filterSet = tempFilters
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (showCaptureImage()) {
            DebugLogger.log(TAG, "CaptureImage true")
            hideCaptureImage()
        } else {
            onBackPressed()
            DebugLogger.log(TAG, "CaptureImage false")

        }
    }

}