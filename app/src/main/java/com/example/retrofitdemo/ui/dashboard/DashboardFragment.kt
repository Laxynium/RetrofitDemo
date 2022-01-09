package com.example.retrofitdemo.ui.dashboard

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.retrofitdemo.R
import com.example.retrofitdemo.RetroInstance
import com.example.retrofitdemo.databinding.FragmentDashboardBinding
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

class DashboardFragment : Fragment(), UploadRequestBody.UploadCallback {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    private var progressBar: ProgressBar? = null

    private val todosService: TodosService = RetroInstance.getRetroInstance().create(TodosService::class.java)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
//        val uploadBtn  = view.findViewById<Button>(R.id.upload_file_btn)
//        uploadBtn.setOnClickListener {
//            imagePicker()
//        }
        imagePicker()

        return root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            val mediaFiles: ArrayList<MediaFile>? = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            val mediaFile = mediaFiles!![0]
            sendFile(mediaFile)
        } else {
            Toast.makeText(context, "something went wrong when choosing file", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendFile(mediaFile: MediaFile) {
        val file = File(mediaFile.path)
        val upload = UploadRequestBody(file, "*", this)
//        val r = RequestBody.create(MediaType.parse("*/*"), file)
        val filePart = MultipartBody.Part.create(upload)
        val call = this.todosService.uploadImage(filePart)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    Toast.makeText(context, "Successfully sent file. ${response.body().string()}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to send file. Error: ${response.errorBody().string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Failed to send file. Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onProgressUpdate(percentage: Int) {
        this.progressBar?.progress = percentage
    }
}
