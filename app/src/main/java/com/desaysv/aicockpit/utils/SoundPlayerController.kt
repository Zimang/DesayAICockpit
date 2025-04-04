package com.desaysv.aicockpit.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
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


    /**
     * 播放 assets/audio 下的文件（如 "b_1_h.mp3"）
     */
    @OptIn(UnstableApi::class)
    fun playFromAssets(fileName: String) {
        val assetPath = "audio/$fileName" // assets/audio 下的路径

        val dataSourceFactory = DataSource.Factory {
            AssetDataSource(context)
        }

        val mediaItem = MediaItem.Builder()
            .setUri("asset:///$assetPath") // 特殊的 asset:// URI
            .build()

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        player.apply {
            stop()
            setMediaSource(mediaSource)
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
