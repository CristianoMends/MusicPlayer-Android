package com.mendesapps.musicplayer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.setMargins

class MusicView(context:Context) {
    val layout:LinearLayout
    val logo:TextView
    val musicName:TextView
    val cover:ImageView
    val time:TextView
    val progress:ProgressBar
    val btnContainer:LinearLayout
    val btnPlay:ImageButton
    val btnPrevious:ImageButton
    val btnNext:ImageButton
    val btnFiles:ImageButton
    val playlistContainer:LinearLayout
    val scrollView:ScrollView

    val musicEngine:MusicEngine

    init{
        scrollView = ScrollView(context)
        layout = LinearLayout(context)
        layout.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT)
        layout.setBackgroundColor(Color.BLACK)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(25,25,25,25)

        logo = TextView(context)
        logo.text = "Music Player"
        logo.setTextColor(Color.RED)
        logo.gravity = Gravity.CENTER
        layout.addView(logo)
        logo.layoutParams.height = 150

        musicName = TextView(context)
        musicName.text = "SongName"
        musicName.setTextColor(Color.WHITE)
        layout.addView(musicName)
        musicName.layoutParams.height = 150

        cover = ImageView(context)
        cover.setImageResource(R.drawable.vinil)
        layout.addView(cover)
        cover.layoutParams.height = 500
        cover.layoutParams.width = LayoutParams.MATCH_PARENT

        time = TextView(context)
        time.text = "00:00 / 00:00"
        layout.addView(time)

        progress = ProgressBar(context,null, android.R.attr.progressBarStyleHorizontal)
        progress.progressTintList = ColorStateList.valueOf(Color.RED)
        progress.progressBackgroundTintList = ColorStateList.valueOf(Color.WHITE)
        layout.addView(progress)
        progress.layoutParams.width = LayoutParams.MATCH_PARENT

        btnContainer = LinearLayout(context)
        btnContainer.orientation = LinearLayout.HORIZONTAL
        layout.addView(btnContainer)
        btnContainer.layoutParams.width = LayoutParams.MATCH_PARENT
        btnContainer.gravity = Gravity.CENTER

        btnPrevious = ImageButton(context)
        btnPrevious.setImageResource(android.R.drawable.ic_media_previous)
        btnContainer.addView(btnPrevious)
        btnPrevious.layoutParams.height = 100
        btnPrevious.layoutParams.width = 100

        btnPlay = ImageButton(context)
        btnPlay.setImageResource(android.R.drawable.ic_media_play)
        btnContainer.addView(btnPlay)
        btnPlay.layoutParams.height = 100
        btnPlay.layoutParams.width = 100

        btnNext = ImageButton(context)
        btnNext.setImageResource(android.R.drawable.ic_media_next)
        btnContainer.addView(btnNext)
        btnNext.layoutParams.height = 100
        btnNext.layoutParams.width = 100

        val roundedDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 12f // Raio do arredondamento das bordas
            setColor(Color.WHITE) // Cor de fundo do LinearLayout
        }

        val scrow = ScrollView(context)
        playlistContainer = LinearLayout(context)
        playlistContainer.setBackgroundColor(Color.WHITE)
        playlistContainer.orientation = LinearLayout.VERTICAL
        scrow.addView(playlistContainer)
        playlistContainer.layoutParams.height = LayoutParams.MATCH_PARENT
        playlistContainer.layoutParams.width = LayoutParams.MATCH_PARENT
        playlistContainer.setPadding(25,25,25,25)
        layout.addView(scrow)
        playlistContainer.background = roundedDrawable

        btnFiles = ImageButton(context)
        btnFiles.setImageResource(android.R.drawable.ic_menu_search)
        btnContainer.addView(btnFiles)
        btnFiles.layoutParams.height = 100
        btnFiles.layoutParams.width = 100

        musicEngine = MusicEngine(context,cover,musicName,time,btnPlay,btnNext,btnPrevious,btnFiles,playlistContainer,progress)

        scrollView.addView(layout)
    }
}