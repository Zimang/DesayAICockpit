package com.desaysv.aicockpit.ui.screen

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.pxToDp
import com.desaysv.aicockpit.viewmodel.ThemeItemViewModel
import com.desaysv.aicockpit.viewmodel.ThemeItemViewModelFactory
import kotlinx.coroutines.launch

/**
 * 灵感屏幕
 */

@Composable
fun ThemeCard(themeItemData: ThemeItemData,
              isApplied:Boolean,
              onApply:(Int)->Unit={},
              onDelete:(ThemeItemData)->Unit={},
              ){

    val t=if (isApplied) ResourceManager.getApplying() else ResourceManager.getApply()
    val c=if (isApplied) colorResource(R.color.choosen) else colorResource(R.color.n_choosen)
    val img=if (isApplied) painterResource(R.drawable.app_1) else painterResource(R.drawable.app_2)


    Column(verticalArrangement = Arrangement.spacedBy(16f.pxToDp())) {

        Box(){
            Image(painterResource(R.drawable.el_1),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(
                    472f.pxToDp()
                ))
            Text(text = themeItemData.themeName, style = TextStyle(
                color = c, fontSize = 32.getSP()
            ), modifier = Modifier.align(Alignment.BottomStart)
                .padding(start = 40.06f.pxToDp(), bottom =40.32f.pxToDp() ))
        }

        if (themeItemData.isDefault){
            Box(modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                if(!isApplied){
                    onApply(themeItemData.id)
                }
            }){
                Image(painterResource(R.drawable.r_1),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(
                        472f.pxToDp(),72f.pxToDp()
                    ))
                Text(text = t!!, style = TextStyle(
                    fontSize = 24.getSP(),
                    color = c
                ), modifier = Modifier.align(Alignment.Center))
            }
        }else{
            Row (horizontalArrangement = Arrangement.spacedBy(16f.pxToDp())){

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                ).clickable {
                    if (!isApplied){
                        onApply(themeItemData.id)
                    }
                }){
                    Image(img,contentDescription = null,
                        modifier = Modifier.size(
                            228.pxToDp(),72f.pxToDp()
                        ))
                    Text(text = t!!, style = TextStyle(
                        color = c, fontSize = 24.getSP()
                    ), modifier = Modifier.align(Alignment.Center))
                }

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                ).clickable {
                    onDelete(themeItemData)
                }){
                    Image(painterResource(R.drawable.app_2),contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                    Text(
                        text = ResourceManager.getDeleted()!!,
                        style = TextStyle(
                        color = colorResource(R.color.n_choosen),
                        fontSize = 24.getSP(),
                    ), modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 468,
    heightDp = 262
)
@Composable
fun ThemeCards_(){
    LazyRow(horizontalArrangement = Arrangement.spacedBy(64f.pxToDp())) {
    }
}

/**
 * 灵感屏幕
 */
@Composable
fun InspiratonScreen(){


    val context = LocalContext.current
    val viewModel = remember {
        val app = context.applicationContext as MyApplication
        ViewModelProvider(
            owner = (context as ComponentActivity),
            factory = ThemeItemViewModelFactory(app.themeRepository)
        )[ThemeItemViewModel::class.java]
    }

    val themes by viewModel.themes.collectAsState(initial = emptyList())

    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            ThemeChangeButtons(ScreenTag.INS)
        }
        Box(modifier = Modifier
            .size(width = (207+1286+143+284+1636).pxToDp(), height = 720f.pxToDp())){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 200f.pxToDp())){
//                BigPanel(modifier = Modifier)
                ThemeCards_()
            }
        }
    }
}

/**
 * 灵感屏幕
 */
@Composable
fun InspiratonScreen(onChange: (ScreenTag) -> Unit={},viewModel: ThemeItemViewModel){
    val contextApp=(LocalContext.current as? Activity)
    val scope = rememberCoroutineScope()

    val themes by viewModel.themes.collectAsState(initial = emptyList())

    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            ThemeChangeButtons(ScreenTag.INS, onChange =onChange, onExit = {
                scope.launch {
                    contextApp?.finish()
                }
            })
        }

        Box(modifier = Modifier
            .size(width = (207+1286+143+284+1636).pxToDp(), height = 720f.pxToDp())){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 200f.pxToDp())){

                LazyRow(horizontalArrangement = Arrangement.spacedBy(64f.pxToDp())) {
                    items(themes){ theme->
                        ThemeCard(
                            theme,theme.isApplied,
                            onApply = {scope.launch {
                                viewModel.switchAppliedTheme(it)
                            }},
                            onDelete = {
                                scope.launch {
                                    viewModel.deleteTheme(it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * final screen
 */
@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 1396,
    heightDp = 262
)
@Composable
fun FinalScreenINSPage(){
    InspiratonScreen()
}
