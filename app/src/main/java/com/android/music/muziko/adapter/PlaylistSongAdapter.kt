package com.android.music.muziko.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.databinding.ItemPlaylistSongBinding
import com.android.music.muziko.activity.MainActivity
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.fragments.PlaylistSongsFragment
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.helper.Coordinator
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RoomRepository
import com.android.music.muziko.utils.ImageUtils

class PlaylistSongAdapter (var listSong: ArrayList<Song>, var context: Activity): RecyclerView.Adapter<PlaylistSongAdapter.PlaylistSongViewHolder>(){

    var dataset: ArrayList<Song> = listSong
    var position = 0


    inner class PlaylistSongViewHolder(var binding: ItemPlaylistSongBinding): RecyclerView.ViewHolder(binding.root){
        var name = binding.txtNameItemPlaylistSong
        var artist = binding.txtArtistItemPlaylistSong
        var imageSong = binding.imgItemPlaylistSong
        fun bind(song: Song){
            name.text = song.title
            artist.text = song.artist
            song.image?.let {
                ImageUtils.loadImageToImageView(
                    context = context,
                    imageView = imageSong,
                    image = it
                )
            }

        }
        fun onClickItem(){
            binding.songContainer.setOnClickListener{
                AnimationHelper.scaleAnimation(it, object : VoidCallback {
                    override fun execute() {
                        upDatePosition(adapterPosition)
                        Coordinator.sourceOfSelectedSong = "playlist"
                        Coordinator.currentDataSource = listSong
                        Coordinator.playSelectedSong(listSong[adapterPosition])
                        RoomRepository.addSongToRecently(listSong[adapterPosition].id!!.toLong())
                        MainActivity.activity.updateVisibility(listSong[adapterPosition])
                    }
                }, 0.95f)
            }
        }
    }

    private fun upDatePosition(adapterPosition: Int) {
        position = adapterPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongViewHolder {
        val binding = ItemPlaylistSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistSongViewHolder, position: Int) {
        holder.bind(listSong[position])
        holder.onClickItem()
        holder.binding.imgMenuItemPlaylistSong.setOnClickListener {
            val popUpMenu = PopupMenu(context, it)
            popUpMenu.inflate(R.menu.songs_in_playlist_popup_menu)

            popUpMenu.setOnMenuItemClickListener {
                val id = dataset[position].id
                dataset.remove(dataset[position])
                Log.e("id", id.toString())
                return@setOnMenuItemClickListener handleMenuButtonClickListener(
                    it.itemId,
                    id.toString()
                )
            }
            popUpMenu.show()
        }
    }

    private fun handleMenuButtonClickListener(itemId: Int, songId: String): Boolean {
        when (itemId) {
            R.id.delete_song_from_playlist_menu_item -> {
                PlaylistSongsFragment.viewModel?.removeSongFromPlaylist(songId)
                PlaylistSongsFragment.viewModel?.updateData()
            }
            else -> return false
        }
        return true
    }

    override fun getItemCount(): Int {
        return listSong.size
    }
}