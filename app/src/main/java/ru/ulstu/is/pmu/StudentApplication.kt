package ru.ulstu.`is`.pmu

import android.app.Application
import ru.ulstu.`is`.pmu.database.AppContainer
import ru.ulstu.`is`.pmu.database.AppDataContainer

class StudentApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}