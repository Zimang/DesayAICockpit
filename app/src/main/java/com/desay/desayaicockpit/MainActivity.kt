package com.desay.desayaicockpit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.desay.desayaicockpit.navigate.MainNavigation
import com.desay.desayaicockpit.ui.screen.demo.ProjectListScreen
import com.desay.desayaicockpit.ui.theme.DesayAICockpitTheme
import com.desay.desayaicockpit.viewmodel.ProjectViewModel
import com.desay.desayaicockpit.viewmodel.ProjectViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesayAICockpitTheme {
                MainNavigation()
                //测试数据流
//                ProjectListScreen()
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