package com.example.gconnect.di

import io.reactivex.disposables.CompositeDisposable
import org.koin.dsl.module.module


private val appModule = module {

    //    single {
//        if (BuildConfig.DEBUG) Timber.DebugTree()
//        else ProductionTree()
//    }

}

private val dataModule = module {

    //    single { AppDatabase.getInstance(androidContext()) }
//
//    single { get<AppDatabase>().contactDao }
//
//    single<SharedPreferenceManager>()
//
//    single<ContactManager>()
//
//    single<ContactServerManager>()

}

val disposable1 = module {
    CompositeDisposable()
}


val appModules = listOf(appModule, dataModule, disposable1)