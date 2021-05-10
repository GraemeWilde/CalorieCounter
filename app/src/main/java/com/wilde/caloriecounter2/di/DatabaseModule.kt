package com.wilde.caloriecounter2.di

import android.content.Context
import com.wilde.caloriecounter2.data.AppDatabase
import com.wilde.caloriecounter2.data.food.FoodDao
import com.wilde.caloriecounter2.data.weight.WeightDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module()
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideWeightDAO(appDatabase: AppDatabase): WeightDao {
        return appDatabase.weightDAO()
    }

    @Provides
    fun provideFoodDAO(appDatabase: AppDatabase): FoodDao {
        return appDatabase.foodDAO()
    }

    /*@Provides
    fun provideFoodWebService() : FoodWebService {
        return Retrofit.Builder()
            .baseUrl("https://ca.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodWebService::class.java)
    }*/
}