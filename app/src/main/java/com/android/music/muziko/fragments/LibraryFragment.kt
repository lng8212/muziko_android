package com.android.music.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.music.R
import com.android.music.databinding.FragmentLibraryBinding
import com.android.music.muziko.adapter.RecentlyAdapter
import com.android.music.muziko.model.Song
import com.android.music.muziko.viewmodel.RecentlyViewModel
import com.android.music.ui.SongViewModel

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    lateinit var recAdapter : RecentlyAdapter
    companion object{
        var listSongRecently: ArrayList<*>? = null
        const val DELETE_REQUEST_CODE = 2
        lateinit var viewModel: SongViewModel
        lateinit var recViewModel: RecentlyViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
        recViewModel = ViewModelProvider(this).get(RecentlyViewModel::class.java)
        activity?.runOnUiThread {
            context?.let { viewModel.sendDataToFragment(it) }
            context?.let { recViewModel.sendDataToFragment() }
            listSongRecently = recViewModel.getDataset()
            recViewModel.updateData()
        }
        recViewModel.dataset.observe(viewLifecycleOwner, recSongsObserver)
        recAdapter = RecentlyAdapter(listSongRecently as ArrayList<Song>, requireContext())

        val recyclerView = binding.recyclerviewLibrary
        recyclerView.apply {
            adapter = recAdapter
            layoutManager = GridLayoutManager(context,2)
        }

        binding.layoutPlaylistsLibrary.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_library_to_playlistsFragment)
        }

        binding.layoutArtistsLibrary.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_library_to_artistsFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val recSongsObserver = Observer<ArrayList<*>> {
            listSongRecently = it
            recAdapter.notifyDataSetChanged()
    }


}