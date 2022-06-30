package com.android.music.muziko.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.databinding.ItemPlaylistsLibraryBinding
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.fragments.PlaylistsFragment
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.model.Playlist
import com.android.music.muziko.repository.PlaylistRepository

class PlaylistAdapter(
    var arrayList: ArrayList<Playlist>,
    val context: Activity,
    val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    var dataset: ArrayList<Playlist> = arrayList
    lateinit var dataSend: PlaylistAdapter.OnDataSend

    inner class PlaylistViewHolder(var binding: ItemPlaylistsLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var name_playlist = binding.txtNamePlaylist
        fun bind(playlist: Playlist) {
            name_playlist.text = playlist.name
        }
    }

    private fun handleMenuButtonClickListener(itemId: Int, playlistId: String): Boolean {
        when (itemId) {
            R.id.deletePlaylist_menu_item -> {
                PlaylistsFragment.viewModel?.playlistRepository?.removePlaylist(playlistId)
                dataSend.onSend(context, playlistId)
            }
            else -> return false
        }
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistsLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val pos = position
        val playlist = dataset[pos]
        holder.apply {
            bind(playlist)
        }

        val playlistRepository = PlaylistRepository(context)

        holder.itemView.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    onItemClickListener(pos)
                }
            }, 0.95f)
        }

        holder.binding.imgNextSongPlaylist.setOnClickListener {
            val popUpMenu = PopupMenu(context, it)
            popUpMenu.inflate(R.menu.playlists_popup_menu)

            popUpMenu.setOnMenuItemClickListener {
                val id = playlistRepository.getPlaylists()[pos].id

                return@setOnMenuItemClickListener handleMenuButtonClickListener(
                    it.itemId,
                    id
                )
            }
            popUpMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    interface OnDataSend {
        fun onSend(context: Activity, id: String)
    }

    fun onDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }
}