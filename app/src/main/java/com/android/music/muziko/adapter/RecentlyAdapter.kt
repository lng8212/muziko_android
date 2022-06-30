package com.android.music.muziko.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.databinding.ItemRecentlyBinding
import com.android.music.muziko.activity.MainActivity
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.helper.Coordinator
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RoomRepository
import com.android.music.muziko.utils.ImageUtils
import com.android.music.ui.fragments.LibraryFragment

class RecentlyAdapter (private var listSong : ArrayList<Song>, val context: Context) : RecyclerView.Adapter<RecentlyAdapter.RecentlyViewHolder>() {
    var position = 0
    inner class RecentlyViewHolder(binding: ItemRecentlyBinding): RecyclerView.ViewHolder(binding.root){
        var title = binding.txtRecently
        var image = binding.imgRecently
        var itemRcn = binding.recentllyContainer
        fun bind(song: Song){
            title.text = song.title
            song.image?.let {
                ImageUtils.loadImgToImgViewNotRound(
                    context = context,
                    imageView = image,
                    image = it
                )
            }
            if(song.image == null){
                ImageUtils.loadImgToImgViewNotRound(
                    context = context,
                    imageView = image,
                    image =  BitmapFactory.decodeResource(context.resources,R.drawable.muziko)
                )
            }
        }
        fun onClickItem(){
            itemRcn.setOnClickListener {

                AnimationHelper.scaleAnimation(it, object : VoidCallback {
                    override fun execute() {
                        upDatePosition(adapterPosition)
                        Coordinator.sourceOfSelectedSong = "songs"
                        Coordinator.currentDataSource = listSong
                        Coordinator.playSelectedSong(listSong[adapterPosition])
                        RoomRepository.addSongToRecently(listSong[adapterPosition].id!!.toLong())
                        MainActivity.activity.updateVisibility(listSong[adapterPosition])

                        Handler().postDelayed({
                            LibraryFragment.recViewModel.updateData()
                            listSong = LibraryFragment.recViewModel.getDataset()
                        },500)
                    }
                }, 0.95f)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyAdapter.RecentlyViewHolder {
        val binding = ItemRecentlyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentlyViewHolder, position: Int) {
        val song = listSong[position]
        this.position = position
        holder.apply {
            bind(song)
            onClickItem()
        }
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    fun upDatePosition(newPosition: Int){
        position = newPosition
    }


}