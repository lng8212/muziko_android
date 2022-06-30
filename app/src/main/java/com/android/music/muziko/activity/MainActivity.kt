package com.android.music.muziko.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.music.R
import com.android.music.databinding.ActivityMainBinding
import com.android.music.muziko.PermissionProvider
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.helper.Coordinator
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RoomRepository
import com.android.music.muziko.utils.ImageUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        var permissionsGranted: Boolean = false
        lateinit var activity: MainActivity

    }


    fun updateVisibility(song : Song) {
        if(Coordinator.isPlaying()){
            binding.btnPlay.visibility = View.GONE
            binding.btnPause.visibility = View.VISIBLE
        }
        else{
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
        }
        binding.layoutOnCollapsed.visibility = View.VISIBLE
        binding.txtArtistOnHeader.text = song.artist
        binding.txtTitleOnHeader.text = song.title
        song.image?.let {
            ImageUtils.loadImageToImageView(
                context = applicationContext,
                imageView = binding.imgOnHeader,
                image = it
            )
        }
    }

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private fun layoutCollapsedListener(){
        binding.layoutOnCollapsed.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    val intent = Intent(this@MainActivity, PlayerPanelActivity::class.java)
                    startActivity(intent)
                }
            }, 0.95f)

        }
        binding.playPauseLayout.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    if (Coordinator.isPlaying()) {
                        Coordinator.pause()
                    } else {
                        Coordinator.resume()
                    }
                    switchPlayPauseButton()
                }
            }, 0.95f)
        }
    }

    private fun switchPlayPauseButton() {
        if (Coordinator.isPlaying()) {
            binding.btnPlay.visibility = View.GONE
            binding.btnPause.visibility = View.VISIBLE
        } else {
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Coordinator.setup(baseContext) // set up media player
        RoomRepository.createDatabase()



        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val navHostFragment  = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
        checkForPermissions()
        layoutCollapsedListener()
    }

    private fun checkForPermissions() {
        val permissionProvider = PermissionProvider()
        permissionProvider.askForPermission(this, permissions)
    }

    override fun onResume() {
        super.onResume()
        Coordinator.currentPlayingSong?.let { updateVisibility(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        Coordinator.mediaPlayerAgent.stop()
    }

}