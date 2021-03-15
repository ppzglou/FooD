package gr.ppzglou.food.di.module

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.BuildConfig
import gr.ppzglou.food.data.SetupDataSourceImpl
import gr.ppzglou.food.data.SetupRepository
import gr.ppzglou.food.domain.SetupDataSource
import gr.ppzglou.food.framework.EdamamApi
import gr.ppzglou.food.framework.RepositoryImpl
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(ActivityComponent::class)
object StorageModule {

    @ActivityScoped
    @Provides
    fun provideRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        fireStoreDB: FirebaseFirestore,
        storage: StorageReference,
        phoneAuthOptions: PhoneAuthOptions.Builder,
        edamamApi: EdamamApi
    ): SetupRepository {
        return RepositoryImpl(firebaseAuth, fireStoreDB, storage, phoneAuthOptions, edamamApi)
    }

    @ActivityScoped
    @Provides
    fun provideSetupDataSourceImpl(repository: SetupRepository): SetupDataSource {
        return SetupDataSourceImpl(repository)
    }

    //Firebase
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorage(): StorageReference =
        FirebaseStorage.getInstance().reference


    @Provides
    fun providePhoneOtp(
        auth: FirebaseAuth,
        activity: Activity
    ): PhoneAuthOptions.Builder {
        return PhoneAuthOptions.newBuilder(auth)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity)
    }


    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(EdamamInterceptor())
            .addInterceptor(logger)
        val okHttp = okHttpClient.build()

        if (BuildConfig.DEBUG) {
            okHttpClient.addNetworkInterceptor(logger)
        }
        return okHttp
    }


    @Provides
    fun provideConvectorFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        adapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(adapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): EdamamApi {
        return retrofit.create(EdamamApi::class.java)
    }
}
