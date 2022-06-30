package com.android.music.muziko.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.music.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : Fragment() {
    private lateinit var binding:FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navView: BottomNavigationView = binding.navView

        val navHostFragment  = activity?.supportFragmentManager?.findFragmentById(com.android.music.R.id.nav_host_fragment_activity_main) as? NavHostFragment
        val navController = navHostFragment?.navController
        if (navController != null) {
            navView.setupWithNavController(navController)
        }
    }
}