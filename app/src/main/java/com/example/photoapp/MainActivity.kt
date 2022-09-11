package com.example.photoapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.photoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var urlList = ArrayList<Uri>()
    private var listSize = 50

    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter(this)

        binding.selectPictureLayout.setOnClickListener {
            urlList.clear()
            val bottomSheet = UserInputBottomSheet {
                listSize = it
                openGalleryForImages()
            }
            bottomSheet.show(supportFragmentManager, TAG)
        }

        setUpRecyclerView()
    }

    private fun openGalleryForImages() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        resultLauncher.launch(intent)

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // if multiple images are selected
                if (data?.clipData != null) {
                    var count = data.clipData?.itemCount

                    if (count != 2) {
                        Toast.makeText(this, "Please select two pictures", Toast.LENGTH_SHORT)
                            .show()
                        return@registerForActivityResult
                    } else {
                        for (i in 0 until count) {
                            val imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                            urlList.add(imageUri)
                        }
                        getTriangularSequence()
                    }
                } else if (data?.data != null) {
                    Toast.makeText(this, "Please select two pictures", Toast.LENGTH_SHORT).show()
                }
            }
        }


    private fun setUpRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    }


    private fun getTriangularSequence() {
        if (urlList.size != 2) {
            Toast.makeText(this, "Please select two pictures", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val triangleSequenceList = ArrayList<Int>()
        val allSequenceList = ArrayList<TriangleGallery>()
        for (i in 1..listSize) {
            triangleSequenceList.add(i * (i + 1) / 2)
        }
        for (i in 0..listSize) {
            if (triangleSequenceList.contains(i)) {
                allSequenceList.add(TriangleGallery(i, uri = urlList[0]))
            } else {
                allSequenceList.add(TriangleGallery(i, uri = urlList[1]))
            }
        }
        allSequenceList.sortBy { it.index }
        allSequenceList.toList().toSet().toList()
        internalStoragePhotoAdapter.submitList(allSequenceList)
    }
}