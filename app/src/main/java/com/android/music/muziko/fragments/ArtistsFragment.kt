package com.android.music.muziko.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.music.R
import com.android.music.databinding.FragmentArtistsBinding
import com.android.music.muziko.adapter.ArtistAdapter
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.model.Song
import com.android.music.muziko.viewmodel.ArtistsViewModel

class ArtistsFragment : Fragment(), ArtistAdapter.OnItemClickListener {

    private lateinit var binding: FragmentArtistsBinding
    private lateinit var artistsAdapter: ArtistAdapter

    companion object{
        var viewModel: ArtistsViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistsBinding.inflate(inflater, container, false)

        binding.constraintArtistsBackLibrary.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    findNavController().navigate(R.id.action_artistsFragment_to_navigation_library)
                }
            }, 0.95f)

        }

        setupViewModel()

        artistsAdapter =
            activity?.let { viewModel?.let { it1 -> ArtistAdapter( it1.getDataset(), it, this) } }!!

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ArtistsViewModel::class.java)
        context?.let { viewModel!!.sendDataToFragment(it) }
        viewModel!!.dataset.observe(viewLifecycleOwner, artistsUpdateObserver)
    }

    private val artistsUpdateObserver = Observer<ArrayList<*>>{
        artistsAdapter.dataset = it as ArrayList<Song>
        binding.recyclerviewArtistsLibrary.adapter = artistsAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = binding.recyclerviewArtistsLibrary
        recyclerview.apply {
            adapter = artistsAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    override fun onItemClick(position: Int) {
        val artist = artistsAdapter.dataset[position]
        val action = artist.image?.let {
            ArtistsFragmentDirections.actionArtistsFragmentToArtistSongsFragment(artist.artist.toString(),
                it
            )
        }
        if (action != null) {
            findNavController().navigate(action)
        }
    }
}