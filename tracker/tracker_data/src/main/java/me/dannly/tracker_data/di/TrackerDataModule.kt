package me.dannly.tracker_data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dannly.tracker_data.local.TrackerDatabase
import me.dannly.tracker_data.remote.OpenFoodApi
import me.dannly.tracker_data.repository.TrackerRepositoryImpl
import me.dannly.tracker_domain.repository.TrackerRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    @Provides
    @Singleton
    fun provideOpenFoodApi(okHttpClient: OkHttpClient): OpenFoodApi =
        Retrofit.Builder().baseUrl(OpenFoodApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).client(okHttpClient).build()
            .create()

    @Provides
    @Singleton
    fun provideTrackerDatabase(app: Application): TrackerDatabase = Room.databaseBuilder(
        app,
        TrackerDatabase::class.java, "tracker_db"
    ).build()

    @Provides
    @Singleton
    fun provideTrackerRepository(openFoodApi: OpenFoodApi, trackerDatabase: TrackerDatabase): TrackerRepository = TrackerRepositoryImpl(
        dao = trackerDatabase.trackerDao, api = openFoodApi
    )
}