package com.desaysv.aicockpit.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.io.File

class SoundPlayerController(
    private val context: Context
) {

    private var player: ExoPlayer = ExoPlayer.Builder(context).build()

    fun play(path: String) {
        val file = File(path)
        if (!file.exists()) return

        val mediaItem = MediaItem.fromUri(Uri.fromFile(file))
        player.apply {
            stop() // 停止当前播放
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    fun release() {
        player.release()
    }
}

@Composable
fun rememberSoundPlayerController(context: Context = LocalContext.current): SoundPlayerController {
    return remember {
        SoundPlayerController(context)
    }
}
