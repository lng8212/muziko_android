package com.android.music.muziko.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.databinding.FragmentAddPlaylistsBinding
import com.android.music.muziko.adapter.AddPlaylistAdapter
import com.android.music.muziko.appInterface.PassDataForSelectSong
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.dialogs.AddSongToPlaylistDialog
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RoomRepository
import com.android.music.muziko.utils.KeyboardUtils.hideKeyboard
import com.android.music.muziko.utils.SwipeToDelete
import com.android.music.ui.SongsRepository

class AddPlaylistsFragment : Fragment(), PassDataForSelectSong {

    private lateinit var binding: FragmentAddPlaylistsBinding
    lateinit var songsRepository: SongsRepository
    lateinit var selectedSongs: ArrayList<Song>
    var addPlaylistAdapter: AddPlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlaylistsBinding.inflate(inflater, container, false)
        selectedSongs = ArrayList()
        binding.txtCancelAddPlaylistsFragment.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    try {
                        selectedSongs.clear()
                    } catch (exception: Exception) {
                    }
                    findNavController().navigate(R.id.action_addPlaylistsFragment_to_playlistsFragment)
                }
            }, 0.95f)
        }

        binding.txtDoneAddPlaylistsFragment.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    if (binding.editTxtNamePlaylistsAddFragment.text.toString().trim().isEmpty()) {
                        binding.editTxtNamePlaylistsAddFragment.error = "Please enter a name"
                    } else if (isUnique(
                            binding.editTxtNamePlaylistsAddFragment.text.toString().lowercase()
                        )
                    ) {
                        binding.editTxtNamePlaylistsAddFragment.error = "Duplicate name"
                    } else {
                        var res = ""
                        for (song in selectedSongs) {
                            res += "${song.id},"
                        }
                        val s = binding.editTxtNamePlaylistsAddFragment.text.toString().trim().lowercase()
                            .split(" ")
                        var name = ""
                        for (i in s) {
                            name += i.substring(0, 1).uppercase() + i.substring(1, i.length) + " "
                        }

                        PlaylistsFragment.viewModel?.playlistRepository?.createPlaylist(
                            name,
                            selectedSongs.size,
                            res
                        )
                        PlaylistsFragment.viewModel?.updateData()

                        try {
                            selectedSongs.clear()
                        } catch (exception: Exception) {

                        }

                        Toast.makeText(context, "Done", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_addPlaylistsFragment_to_playlistsFragment)
                    }
                }
            }, 0.95f)

        }

        binding.constraintAddMusicAddFragment.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    createDialogToSelectPlaylist()
                }
            }, 0.95f)
        }

        binding.layoutAddPlaylist.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    hideKeyboard(requireActivity())
                }
            }, 0.95f)
        }

        return binding.root
    }

    private fun createDialogToSelectPlaylist() {
        songsRepository = context?.let { SongsRepository(it) }!!
        val listSongs: ArrayList<Song> = ArrayList()
        for (i in songsRepository.getListOfSongs()) {
            var ok = true
            for (j in selectedSongs) {
                if (i.id == j.id) {
                    ok = false
                    break
                }
            }
            if (ok) listSongs.add(i)
        }
        val addSongToPlaylistDialog = AddSongToPlaylistDialog(listSongs)

        addSongToPlaylistDialog.setTargetFragment(this, 0)
        this.fragmentManager?.let { it1 -> addSongToPlaylistDialog.show(it1, "pl") }
    }

    private fun isUnique(name: String): Boolean {
        for (playlist in RoomRepository.cachedPlaylistArray) {
            if (playlist.name.trim().lowercase() == name)
                return true
        }
        return false
    }

    override fun passDataToInvokingFragment(songs: ArrayList<Song>) {
        selectedSongs = songs

        addPlaylistAdapter = activity?.let {
            AddPlaylistAdapter(
                it,
                selectedSongs
            )
        }
        swipeToDelete(binding.recyclerviewAddPlaylistsFragment)
        binding.recyclerviewAddPlaylistsFragment.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewAddPlaylistsFragment.adapter = addPlaylistAdapter

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = addPlaylistAdapter!!.dataset[viewHolder.adapterPosition]
                selectedSongs.remove(deletedItem)
                addPlaylistAdapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(context, "delete", Toast.LENGTH_LONG).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}