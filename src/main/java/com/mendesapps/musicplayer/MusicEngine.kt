package com.mendesapps.musicplayer

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import java.io.File

class MusicEngine(
    context:Context,
    val cover: ImageView,
    val songName:TextView,
    val time:TextView,
    val btnPlay:ImageButton,
    btnNext:ImageButton,
    btnPrevious:ImageButton,
    btnFiles:ImageButton,
    val playlistContainer:LinearLayout,
    val progress: ProgressBar){

    private var mediaPlayer:MediaPlayer
    private var audioFiles:List<File>
    private val playlist = mutableListOf<TextView>()

    private val handler = Handler(Looper.getMainLooper())
    private var currentSong = 0
    private val externalStorageDirectory = Environment.getExternalStorageDirectory()
    private var alert = AlertDialog.Builder(context)
    val load : ProgressDialog

    init{
        load = ProgressDialog(context)
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            // Lidar com o evento de erro do MediaPlayer aqui
            Log.e(TAG, "Error occurred: $what, $extra")
            true // Retorna true para indicar que o erro foi tratado
        }

        audioFiles = searchAudioFiles(externalStorageDirectory)

        btnPlay.setOnClickListener {
            if(audioFiles.isNotEmpty()){
                if(mediaPlayer.isPlaying){
                    btnPlay.setImageResource(android.R.drawable.ic_media_play)
                    mediaPlayer.pause()
                }else if(mediaPlayer.currentPosition == 0){
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause)
                    playSong(context)
                    update(context)
                }else{
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause)
                    mediaPlayer.start()
                }
            }else{
                alert.setMessage("Adicione musicas a lista antes de reproduzir")
                alert.create().show()
            }

        }

        btnFiles.setOnClickListener {
            load.setMessage("Carregando...")
            load.setCancelable(false)
            load.show()
            audioFiles = searchAudioFiles(externalStorageDirectory)
            for(i in audioFiles.indices){
                val musicDesc = TextView(context)
                musicDesc.id = i

                if(audioFiles.get(i).name.length > 40){
                    val name:String
                    name = audioFiles.get(i).name.substring(0,40) + "..."
                    musicDesc.text = "${i + 1} : ${name}"
                }else{
                    musicDesc.text = "${i + 1} : ${audioFiles.get(i).name}"
                }

                playlistContainer.addView(musicDesc)
                playlist.add(musicDesc)

                musicDesc.setOnClickListener {
                    if(currentSong != musicDesc.id){
                        for(music in playlist){
                            music.setBackgroundColor(Color.WHITE)
                        }
                        musicDesc.setBackgroundColor(Color.RED)
                        currentSong = musicDesc.id
                        mediaPlayer.release()
                        btnPlay.setImageResource(android.R.drawable.ic_media_pause)
                        playSong(context)
                        update(context)
                    }
                }
            }
            load.dismiss()
            message()
        }

        btnNext.setOnClickListener {
            currentSong++
            if(currentSong >= audioFiles.size){
                currentSong = 0
            }
            mediaPlayer.release()
            playSong(context)
            update(context)
        }
        btnPrevious.setOnClickListener {
            currentSong--
            if(currentSong < 0){
                currentSong = audioFiles.size - 1
            }
            mediaPlayer.release()
            playSong(context)
            update(context)
        }

    }
    private fun update(context: Context) {
        if(mediaPlayer.isPlaying){
            cover.apply { rotation += 10 }
        }

        var position = mediaPlayer.currentPosition / 1000
        val fullDuration = mediaPlayer.duration / 1000
        val minutesPosition = position / 60
        val secondsPosition = position % 60
        val minutesDuration = fullDuration / 60
        val secondsDuration = fullDuration % 60



        val formattedTime = String.format("%02d:%02d / %02d:%02d", minutesPosition, secondsPosition, minutesDuration, secondsDuration)

        time.text = formattedTime
        progress.max = fullDuration
        progress.progress = position

        if(position == fullDuration){
            currentSong++
            if(currentSong >= audioFiles.size){
                currentSong = 0
            }
            mediaPlayer.release()
            playSong(context)
        }
        handler.postDelayed({ update(context) }, 250)    }

    fun playSong(context: Context){
            songName.text = "${currentSong+1} : ${audioFiles.get(currentSong).name}"
            mediaPlayer = MediaPlayer.create(context,audioFiles.get(currentSong).toUri())
            mediaPlayer.start()
    }


    private fun searchAudioFiles(directory: File): List<File> {

        val audioFiles = mutableListOf<File>()

        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {

                    val isAndroidFolder = file.name.equals("android", ignoreCase = true)
                    val isAlarmRingtoneFolder = file.absolutePath.contains("/alarms") ||
                            file.absolutePath.contains("/ringtones",ignoreCase = true) ||
                            file.absolutePath.contains("/notifications",ignoreCase = true) ||
                            file.absolutePath.contains("/preload",ignoreCase = true)
                    if (!isAndroidFolder && !isAlarmRingtoneFolder) {
                        audioFiles.addAll(searchAudioFiles(file))
                    }
                } else if (file.isFile && file.extension.equals("mp3", ignoreCase = true)) {
                    audioFiles.add(file)
                }
            }
        }

        return audioFiles
    }

    private fun message(){
        if(audioFiles.isNotEmpty()){
            alert.setMessage("${audioFiles.size} arquivos mp3 encontrados!")
            alert.create().show()
        }else{
            alert.setMessage("Nenhum arquivo mp3 encontrado em ${Environment.getExternalStorageDirectory().absolutePath}")
            alert.create().show()
        }
    }




}