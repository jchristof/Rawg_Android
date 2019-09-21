package com.theobviousexit.rawg

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.theobviousexit.rawg.ui.main.SearchViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.concurrent.TimeUnit


class RawgApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RawgApplication)
            modules(
                module {
                    single<Retrofit> {
                        Retrofit
                            .Builder()
                            .baseUrl("https://api.rawg.io/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                    }

                    viewModel { SearchViewModel(get()) }
                }
            )
        }
    }
}

