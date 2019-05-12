package com.example.gconnect.di

import com.Util.ApiClient
import com.movieapp.VmMovieDetail
import com.movieapp.VmMovieList
import com.movieapp.VmMovieSearch
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module

val syncModule = module {
    //    factory { (lifecycleOwner: LifecycleOwner) ->  SyncObserver(lifecycleOwner , get(), get()) }
}

val mainModule = module {
    //    viewModel<MainViewModel>()
}

val remoteModule = module {
    //    viewModel<RemoteContactViewModel>()
}

val detailsModule = module {
    //    scope<DetailViewModel>(DETAIL_SCOPE)
}

val vmNowPlaying = module {
    viewModel<VmMovieList>()
}

val vmMovieDetail = module {
    viewModel<VmMovieDetail>()
}

val vmMovieSearch = module {
    viewModel<VmMovieSearch>()
}


//val viewModelDashboard = module {
//    viewModel<ViewModelDashboard>()
//}
//
//val viewModelRunningData = module {
//    viewModel<ViewModelRunningData>()
//}


val apiClient = module {
    ApiClient.create()
}

val disposable2 = module {

    factory { CompositeDisposable() }
}

//val compositeDisposabl = module {
//    factory<CompositeDisposable>()
//}


val activityModules = listOf(mainModule, detailsModule, remoteModule, syncModule, vmNowPlaying, vmMovieDetail, vmMovieSearch, disposable2, apiClient)

object ModuleConstants {

    const val DETAIL_SCOPE = "com.igweze.ebi.details_scope"
}