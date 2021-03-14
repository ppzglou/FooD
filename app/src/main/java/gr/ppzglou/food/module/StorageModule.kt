package gr.ppzglou.food.module

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
import gr.ppzglou.food.data.SetupDataSourceImpl
import gr.ppzglou.food.data.SetupRepository
import gr.ppzglou.food.domain.SetupDataSource
import gr.ppzglou.food.framework.RepositoryImpl
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
        phoneAuthOptions: PhoneAuthOptions.Builder
    ): SetupRepository {
        return RepositoryImpl(firebaseAuth, fireStoreDB, storage, phoneAuthOptions)
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
}
