package com.desaysv.aicockpit

import android.app.Application
import androidx.room.Room
import com.desaysv.aicockpit.business.navigate.requestApplyingTheme
import com.desaysv.aicockpit.business.navigate.sendApplyingTheme
import com.desaysv.aicockpit.data.db.AppDatabase
import com.desaysv.aicockpit.data.loader.GlobalInspirationReceiverHolder
import com.desaysv.aicockpit.data.repository.ElectricityRepository
import com.desaysv.aicockpit.data.repository.SoundRepository
import com.desaysv.aicockpit.data.repository.ThemeRepository
import com.desaysv.aicockpit.utils.LocaleManager
import com.desaysv.aicockpit.utils.Log
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.StoragePermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class MyApplication : Application() {
    // 通过 lazy 延迟初始化数据库（线程安全）
    val database by lazy {
//        AppDatabase.getInstance(this)
        Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"proj"
        ).build()
    }



    // 初始化 Repository（依赖数据库 DAO）
//    val repository by lazy { ProjectRepository(database.projectDao()) }
    val themeRepository by lazy { ThemeRepository(database.themeItemDao(),this) }
    val elesRepository by lazy { ElectricityRepository(database.electricityItemDao(),this) }
    val soundRepository by lazy { SoundRepository(database.soundItemDao(),this) }

    override fun onCreate() {
        super.onCreate()

        ResourceManager.init(this)
        LocaleManager.init(this)

        GlobalInspirationReceiverHolder.register(this)

        // 启动时检查默认主题
        CoroutineScope(Dispatchers.IO).launch {
//            themeRepository.ensureDefaultThemeExists()
            //确保至少存在一个默认壁纸
            themeRepository.ensureThemeExists()
            //确保至少存在一个默认“电”
            elesRepository.ensureEleExists()

            //更新主题到Launcher或者接收Launcher的主题
            val tname=requestApplyingTheme(this@MyApplication)
            if(tname.isEmpty()){
                val apTheme=themeRepository.getCurrentApplyingTheme()
                if (apTheme==null){
                    //这一种情况就是，config加载了主题,没有一个主题是applied
                    //这种情况不可能出现
                    themeRepository.switchAppliedTheme(1)
                    sendApplyingTheme(this@MyApplication,"默认主题")
                }else{
                    sendApplyingTheme(this@MyApplication,apTheme.themeName)
                }
            }else{
                val apTheme=themeRepository.getByName(tname)
                if(apTheme!=null){
                    themeRepository.switchAppliedTheme(apTheme.id)
                }else{
                    Log.d("ERROR!!! 找不到当前应用的主题")
                }

            }
        }


        CoroutineScope(SupervisorJob()+ Dispatchers.IO).launch {
            while (isActive) {
                Log.d("aicockpit heartbeat: alive at ${System.currentTimeMillis()}")
                delay(10_000)
            }
        }



    }

}