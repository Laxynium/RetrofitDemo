package com.example.retrofitdemo.ui.dashboard

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.retrofitdemo.R
import com.example.retrofitdemo.RetroInstance
import com.example.retrofitdemo.model.UploadRequestBody
import com.example.retrofitdemo.service.TodosService
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FileUploadFragment : Fragment(), UploadRequestBody.UploadCallback {

    private var progressBar: ProgressBar? = null
    private var uploadBtn: Button? = null
    private var fileNameTxt: TextView? = null

    private var selectedFile: MediaFile? = null

    private val todosService: TodosService = RetroInstance.getRetroInstance().create(TodosService::class.java)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_fileupload, container, false)
        progressBar = view.findViewById(R.id.progress_bar)
        fileNameTxt = view.findViewById(R.id.selected_file_name)
        val selectFileBtn  = view.findViewById<Button>(R.id.select_file_btn)
        uploadBtn  = view.findViewById(R.id.upload_file_btn)


        selectFileBtn.setOnClickListener {
            imagePicker()
        }
        uploadBtn!!.setOnClickListener {
            this.sendFile()
        }

        return view
    }

    private fun setFileToUpload(file: MediaFile) {
        this.selectedFile = file
        this.uploadBtn!!.isEnabled = true
        this.fileNameTxt!!.text = file.name
    }

    private fun imagePicker() {
        val intent = Intent(activity, FilePickerActivity::class.java)
        intent.putExtra(FilePickerActivity.CONFIGS, Configurations.Builder()
            .setCheckPermission(true)
            .setShowImages(true)
            .setShowVideos(false)
            .setShowFiles(true)
            .enableImageCapture(true)
            .setMaxSelection(1)
//            .setSuffixes("txt", "pdf", "png", "jpeg")
            .setSkipZeroSizeFiles(true)
            .build()
        )
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            val mediaFiles: ArrayList<MediaFile>? = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            if (!mediaFiles.isNullOrEmpty()) {
                val mediaFile = mediaFiles!![0]
                setFileToUpload(mediaFile)
            }
        } else {
            Toast.makeText(context, "something went wrong when choosing file", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendFile() {
        val file = File(this.selectedFile!!.path)
        val upload = UploadRequestBody(file, "*", this)
        val filePart = MultipartBody.Part.createFormData("file", file.name, upload)
        val call = this.todosService.uploadImage(filePart)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    Toast.makeText(context, "Successfully sent file. ${response.body()?.string()}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to send file. Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Failed to send file. Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onProgressUpdate(percentage: Int) {
        this.progressBar?.setProgress(percentage, true)
    }
}
