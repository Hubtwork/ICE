package com.example.ice.activities

import android.Manifest
import android.app.Activity
import android.content.Context
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
import com.example.ice.adapters.QuickFilterAdapter
import com.example.ice.fragments.QuickFilterFragment
import com.example.ice.models.CustomFilter
import com.example.ice.models.Filters
import com.example.ice.models.ResponseData
import com.example.ice.utils.DebugLogger
import com.example.ice.utils.PermissionManager
import com.example.ice.utils.RequestToServer
import com.example.ice.utils.RequestToServerOkHttp
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraActivity"
    }

    var filterSet = mutableListOf<CustomFilter>()

    private lateinit var previewView: PreviewView
    private lateinit var scanButton: ImageView
    private lateinit var filterButton: ImageView
    private lateinit var filterCheckButton: ImageView

    private lateinit var quickFilter: QuickFilterFragment

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

        // Load Dummies
        filterSet = loadDummies()

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

    private fun loadDummies(): MutableList<CustomFilter> {
        var dummyFilterLists = mutableListOf<CustomFilter>()
        dummyFilterLists.add(CustomFilter("Egg", R.drawable.fried_egg, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Milk", R.drawable.milk, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Flour", R.drawable.flour, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Beans", R.drawable.beans, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Shellfish", R.drawable.shrimp, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Fish", R.drawable.fish, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Nuts", R.drawable.walnut, false, arrayListOf()))
        dummyFilterLists.add(CustomFilter("Pork", R.drawable.sausages, false, arrayListOf()))


        return dummyFilterLists
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
            openQuickFilters()
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

    private fun sendImage(file: File) {
        val partBody : RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multiPartImage = MultipartBody.Part.createFormData("image", file.name, partBody)
        val imageRequest = RequestToServerOkHttp.service
            imageRequest
                .sendImage(image = multiPartImage)
                .enqueue(
                    object : Callback<ResponseData> {
                        override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                            DebugLogger.log("통신실패", "$t")
                        }

                        override fun onResponse(
                            call: Call<ResponseData>,
                            response: Response<ResponseData>
                        ) {
                            DebugLogger.log("통신성공", "$file / $response")
                            if (response.isSuccessful) {
                                DebugLogger.log("이미지 서버연결 성공", "${response.body()!!.result}")
                                if(response.body()!!.result.isNotEmpty())
                                {
                                    Toast.makeText(applicationContext, "이미지 서버 업로드 성공", Toast.LENGTH_LONG).show()
                                }

                            }

                        }
                    }
                )
    }

    private fun savePhoto() {
        imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yy-mm-dd", Locale.US).format(System.currentTimeMillis()) + ".png"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        sendImage(photoFile)

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
    private fun openQuickFilters() {
        val adapter = QuickFilterAdapter(this)
        quickFilter = QuickFilterFragment(adapter)
        adapter.setItem(filterSet)
        quickFilter.show(supportFragmentManager, "QUICK_FILTER")
    }

    private fun openFilterSettingIntent() {
        // Success Code = 100
        val intent = Intent(this, SettingActivity::class.java)
        intent.putExtra("filters", Filters(filterSet) )
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val filtersFromIntent = (data!!.getSerializableExtra("filters")) as Filters
                    if (!filtersFromIntent.filters.isNullOrEmpty()) {
                        filterSet = filtersFromIntent.filters
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