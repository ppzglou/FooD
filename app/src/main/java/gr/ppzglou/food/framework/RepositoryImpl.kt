package gr.ppzglou.food.framework

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import gr.ppzglou.food.ERROR_GENERAL
import gr.ppzglou.food.ERROR_NOT_EMAIL_VALIDATED
import gr.ppzglou.food.data.SetupRepository
import gr.ppzglou.food.data.models.*
import gr.ppzglou.food.ext.BaseException
import gr.ppzglou.food.ext.handleApiFormat
import gr.ppzglou.food.ext.networkCall
import gr.ppzglou.food.util.ResultWrapper
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap


class RepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStoreDB: FirebaseFirestore,
    private val storage: StorageReference,
    private val phoneAuthOptionsBuilder: PhoneAuthOptions.Builder,
    private val edamamApi: EdamamApi
) : SetupRepository {

    private val USERS = "users"
    private val DELETED_USERS = "deleted_users"

    override suspend fun searchRecipeRemote(request: SearchRequest): ResultWrapper<SearchResponse> =
        networkCall {
            edamamApi.getSearchResult(request.txt, request.from, request.to).execute()
                .handleApiFormat()
        }

    override suspend fun recipeRemote(request: String): ResultWrapper<MutableList<Recipe>> =
        networkCall {
            edamamApi.getRecipeResult(request).execute().handleApiFormat()
        }

    override suspend fun currentUserRemote(): ResultWrapper<CurrentUserResponse> {
        return firebaseAuth.currentUser?.let {
            ResultWrapper.Success((CurrentUserResponse(it.uid, it.isEmailVerified)))
        } ?: throw BaseException(ERROR_GENERAL)
    }


    override suspend fun logoutRemote(): ResultWrapper<Boolean> {
        firebaseAuth.signOut()
        return ResultWrapper.Success(true)
    }

    override suspend fun authRemote(pass: String): ResultWrapper<Boolean> {
        if (firebaseAuth.currentUser?.email == null) throw BaseException(ERROR_GENERAL)

        firebaseAuth.signInWithEmailAndPassword(firebaseAuth.currentUser!!.email!!, pass).await()

        return ResultWrapper.Success(true)
    }

    override suspend fun loginRemote(request: LoginRequest): ResultWrapper<CurrentUserResponse> {
        if (request.email == null || request.password == null) {
            throw BaseException(ERROR_GENERAL)
        }
        firebaseAuth.signInWithEmailAndPassword(request.email, request.password).await()

        if (!firebaseAuth.currentUser?.isEmailVerified!!)
            throw BaseException(ERROR_NOT_EMAIL_VALIDATED)

        return currentUserRemote()
    }


    override suspend fun registerRemote(request: RegisterRequest): ResultWrapper<CurrentUserResponse> {
        if (request.email == null || request.password == null) {
            throw BaseException(ERROR_GENERAL)
        }
        firebaseAuth.createUserWithEmailAndPassword(request.email, request.password).await()
        return currentUserRemote()
    }


    override suspend fun updateDisplayNameRemote(request: UpdateDisplayNameRequest): ResultWrapper<Boolean> {
        if (request.name == null) {
            throw BaseException(ERROR_GENERAL)
        }
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)
        user.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(request.name).build()
        ).await()

        return ResultWrapper.Success(true)
    }


    override suspend fun addUserFirestoreRemote(request: AddUserDbRequest): ResultWrapper<Boolean> {
        if (request.fname == null || request.sname == null) throw BaseException(ERROR_GENERAL)

        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)

        val hash = hashMapOf(
            "name" to request.fname,
            "sname" to request.sname,
            "photo" to false
        )
        fireStoreDB.collection(USERS).document(user.uid).set(hash).await()

        return ResultWrapper.Success(true)
    }


    override suspend fun verificationEmailRemote(): ResultWrapper<Boolean> {
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)
        user.sendEmailVerification().await()
        return ResultWrapper.Success(true)
    }


    override suspend fun resetPassRemote(request: ResetPasswordRequest): ResultWrapper<Boolean> {
        if (request.email == null) throw BaseException(ERROR_GENERAL)

        firebaseAuth.sendPasswordResetEmail(request.email).await()
        return ResultWrapper.Success(true)
    }


    override suspend fun updatePassRemote(request: UpdatePassRequest): ResultWrapper<Boolean> {
        if (request.oldPass == null || request.newPass == null) throw BaseException(ERROR_GENERAL)
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)

        loginRemote(LoginRequest(user.email, request.oldPass))
        user.updatePassword(request.newPass).await()
        return ResultWrapper.Success(true)
    }

    override suspend fun updateEmailRemote(request: UpdateEmailRequest): ResultWrapper<Boolean> {
        if (request.email == null) throw BaseException(ERROR_GENERAL)
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)

        loginRemote(LoginRequest(user.email, request.pass))
        user.updateEmail(request.email).await()

        return ResultWrapper.Success(true)
    }

    override suspend fun phoneOtpRemote(request: OtpPhoneRequest): ResultWrapper<Boolean> {
        if (request.phone == null || request.callbacks == null) throw BaseException(ERROR_GENERAL)

        val phoneAuthOption = phoneAuthOptionsBuilder
            .setPhoneNumber(request.phone)
            .setCallbacks(request.callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOption)

        return ResultWrapper.Success(true)
    }


    override suspend fun updatePhoneCredentialRemote(request: UpdatePhoneCredentialRequest): ResultWrapper<Boolean> {
        if (request.code == null || request.verificationId == null) throw BaseException(
            ERROR_GENERAL
        )

        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)
        val credential = PhoneAuthProvider.getCredential(request.verificationId, request.code)
        user.updatePhoneNumber(credential).await()

        return ResultWrapper.Success(true)
    }

    override suspend fun getPhoneRemote(): ResultWrapper<String?> {
        val phone = firebaseAuth.currentUser?.phoneNumber
        return ResultWrapper.Success(phone)
    }

    override suspend fun userProfileRemote(): ResultWrapper<UserProfileResponse> {
        val uid = firebaseAuth.currentUser?.uid ?: throw BaseException(ERROR_GENERAL)
        var photo: String? = null
        val userDataDoc = fireStoreDB.collection(USERS).document(uid).get().await()

        if (userDataDoc.getBoolean("photo") == false)
            storage.child("profile_pics/$uid").downloadUrl
                .addOnSuccessListener {
                    photo = it.toString()
                }.await()

        val userProfile = UserProfileResponse(
            name = firebaseAuth.currentUser?.displayName,
            email = firebaseAuth.currentUser?.email,
            phone = firebaseAuth.currentUser?.phoneNumber,
            photo = photo
        )
        return ResultWrapper.Success(userProfile)
    }

    override suspend fun uploadFileToStorageRemote(request: UploadFileRequest): ResultWrapper<Boolean> {
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)
        if (request.uri == null)
            throw BaseException(ERROR_GENERAL)
        val storageName = user.uid

        val fileRef = storage.child("profile_pics/$storageName")
        fileRef.putFile(request.uri).await()

        fireStoreDB.collection(USERS).document(user.uid)
            .update(mutableMapOf<String, Any>("photo" to true)).await()
        return ResultWrapper.Success(true)
    }

    override suspend fun getPersonalDetailsRemote(): ResultWrapper<PersonalDetailsModel> {
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)

        val userDataDoc =
            fireStoreDB.collection(USERS).document(user.uid).get().await()

        val userDetails = PersonalDetailsModel(
            name = userDataDoc.getString("name"),
            sname = userDataDoc.getString("sname")
        )
        return ResultWrapper.Success(userDetails)
    }

    override suspend fun setPersonalDetailsRemote(request: PersonalDetailsModel): ResultWrapper<Boolean> {
        if (request.name == null || request.sname == null)
            throw BaseException(ERROR_GENERAL)

        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)

        val name = request.name.replace("\\s".toRegex(), "")
        val sname = request.sname.replace("\\s".toRegex(), "")
        val hash = hashMapOf<String, Any>(
            "name" to name,
            "sname" to sname
        )

        fireStoreDB.collection(USERS).document(user.uid).update(hash).await()
        updateDisplayNameRemote(UpdateDisplayNameRequest("$name $sname"))

        return ResultWrapper.Success(true)
    }

    override suspend fun deleteAccountRemote(request: DeleteAccountRequest): ResultWrapper<Boolean> {
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)
        if (request.password == null || request.description == null) throw BaseException(
            ERROR_GENERAL
        )

        val userHash: MutableMap<String, Any> = HashMap()
        userHash["name"] = user.displayName!!
        userHash["email"] = user.email.toString()
        userHash["deleted_at"] = Timestamp(Date(System.currentTimeMillis()))
        userHash["description"] = request.description

        loginRemote(LoginRequest(user.email, request.password))
        fireStoreDB.collection(DELETED_USERS).document(user.uid).set(userHash).await()
        fireStoreDB.collection(USERS).document(user.uid).delete().await()
        user.delete().await()

        return ResultWrapper.Success(true)
    }

}