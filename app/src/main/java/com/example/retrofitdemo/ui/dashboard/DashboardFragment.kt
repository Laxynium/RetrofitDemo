package com.example.retrofitdemo.ui.dashboard

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.retrofitdemo.R
import com.example.retrofitdemo.databinding.FragmentDashboardBinding
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
//        val uploadBtn  = view.findViewById<Button>(R.id.upload_file_btn)
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
            val mediaFiles: Array<Parcelable>? = data.getParcelableArrayExtra(FilePickerActivity.MEDIA_FILES)
            val path = mediaFiles!![0]
            Toast.makeText(context, path.toString(), Toast.LENGTH_LONG)
        } else {
            Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG)
        }
    }
}
