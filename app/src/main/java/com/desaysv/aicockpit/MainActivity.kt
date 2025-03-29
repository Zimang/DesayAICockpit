package com.desaysv.aicockpit

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.desaysv.aicockpit.business.navigate.MainNavigation
import com.desaysv.aicockpit.ui.component.AppPermissionGate
import com.desaysv.aicockpit.ui.theme.DesayAICockpitTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        // 隐藏状态栏和导航栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        setContent {
            DesayAICockpitTheme {
                AppPermissionGate({}) {
                    MainNavigation()
                }
            }
        }
        printScreenScaleParams(this)



    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DesayAICockpitTheme {
        Greeting("Android")
    }
}
@Preview
@Composable
fun MyText() {
    val context = LocalContext.current
    val density = context.resources.displayMetrics.density
    val scaledDensity = context.resources.displayMetrics.scaledDensity
    var textSize by remember { mutableStateOf(IntSize.Zero) }

    // 将布局大小从像素转换为 dp
    val widthDp = textSize.width / density
    val heightDp = textSize.height / density

    Text(
        text = "灵感",
        fontSize = 32.sp,
//        fontSize = 20.64.sp,
        modifier = Modifier.onSizeChanged { size ->
            textSize = size
        }
    )

    // 打印 Text 组件的实际大小
    println("Text的大小 $textSize  我们需要的大小(设计图纸给出的) 61.54dp 29.66dp")
    println("Text的大小dp $widthDp $heightDp  我们需要的大小 61.54dp 29.66dp")
}
fun printScreenScaleParams(context: Context) {
    val metrics = context.resources.displayMetrics
    Log.d("ScreenInfo", "density: ${metrics.density}, scaledDensity: ${metrics.scaledDensity}")
}
