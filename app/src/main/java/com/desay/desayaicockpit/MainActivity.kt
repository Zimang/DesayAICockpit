package com.desay.desayaicockpit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.desay.desayaicockpit.navigate.NavigationBar
import com.desay.desayaicockpit.navigate.NavigationContent
import com.desay.desayaicockpit.ui.theme.DesayAICockpitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesayAICockpitTheme {
                val navController = rememberNavController()

                Row(modifier = Modifier.fillMaxSize()) {
                    // 固定左侧导航栏
                    NavigationBar(navController)

                    // 右侧内容区域
                    NavigationContent(navController)
                }
//                MainRun(Modifier)
            }
        }
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