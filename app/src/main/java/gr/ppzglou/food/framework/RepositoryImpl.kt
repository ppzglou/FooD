package gr.ppzglou.food.framework

import android.provider.DocumentsContract
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import gr.ppzglou.food.data.SetupRepository
import kotlinx.coroutines.tasks.await
import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.collections.HashMap


class RepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStoreDB: FirebaseFirestore,
    private val storage: StorageReference,
    private val phoneAuthOptionsBuilder: PhoneAuthOptions.Builder
) : SetupRepository {


}