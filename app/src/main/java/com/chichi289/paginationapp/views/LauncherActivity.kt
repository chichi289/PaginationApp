package com.chichi289.paginationapp.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chichi289.paginationapp.databinding.ActivityLauncherBinding

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnList.setOnClickListener {
            ListActivity.start(this)
        }
        binding.btnPagingList.setOnClickListener {
            PagingListActivity.start(this, false)
        }
        binding.btnPagingListGrid.setOnClickListener {
            PagingListActivity.start(this, true)
        }
        binding.btnPagingListDb.setOnClickListener {
            PagingListDbActivity.start(this)
        }

    }
}