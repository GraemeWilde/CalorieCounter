package com.wilde.caloriecounter2.di

import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import com.wilde.caloriecounter2.BuildConfig
import com.wilde.caloriecounter2.data.food.FoodWebService
import com.wilde.caloriecounter2.data.food.ProductAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@InstallIn(SingletonComponent::class)
@Module()
class WebServices {

    @Provides
    fun provideFoodWebService(): FoodWebService {

        return Retrofit.Builder()
            .baseUrl("https://ca.openfoodfacts.org/")
            /*.addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setExclusionStrategies(object : ExclusionStrategy {
                            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                                return if (f == null) true else {
                                    val annotation = f.getAnnotation(Exclude::class.java)
                                    annotation != null
                                }
                            }

                            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                                return false
                            }
                        })
                        .registerTypeAdapterFactory(ProductAdapterFactory())
                        .create()
                )
            )*/
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(ProductAdapter()).build()))
            .client(OkHttpClient().newBuilder().addInterceptor(HeaderInterceptor()).build())
            .build()
            .create(FoodWebService::class.java)
    }
}




/*class FlattenJsonAdapter<T>(private val delegate: ) : JsonAdapter<T>() {

    override fun fromJson(reader: JsonReader): T? {
        return delegate.fromJson(reader).data
    }

    override fun toJson(writer: JsonWriter, value: T?) {
        delegate.toJson(writer, new Flatten<>(value))
    }
}*/

/*class ExcludeAdapter<T> : JsonAdapter<T>() {
    override fun fromJson(reader: JsonReader): T? {
        TODO("Not yet implemented")
    }

    override fun toJson(writer: JsonWriter, value: T?) {
        TODO("Not yet implemented")
    }

}*/

/*class ExcludeFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {

    }
}*/

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("User-Agent", "Calorie Counter - Android - Version ${BuildConfig.VERSION_NAME}")
            .build()

        return chain.proceed(newRequest)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@JsonQualifier
annotation class Exclude