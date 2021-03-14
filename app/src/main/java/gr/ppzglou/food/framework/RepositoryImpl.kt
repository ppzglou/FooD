package gr.ppzglou.food.framework

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
import gr.ppzglou.food.util.ResultWrapper
import kotlinx.coroutines.tasks.await


class RepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStoreDB: FirebaseFirestore,
    private val storage: StorageReference,
    private val phoneAuthOptionsBuilder: PhoneAuthOptions.Builder
) : SetupRepository {

    private val USERS = "users"
    private val DELETED_USERS = "deleted_users"
    private val DATA = "data"
    private val MESSAGES = "messages"
    private val PERSONAL = "personal"
    private val ADDRESS = "address"
    private val AM_CODES = "amCodes"
    private val TAXIS = "taxis"
    private val INCOME = "income"
    private val MEMBERS = "members"
    private val FILES = "files"
    private val MORE_INFO = "moreInfo"


    override suspend fun currentUserRemote(): ResultWrapper<CurrentUserResponse> {
        val user = firebaseAuth.currentUser ?: throw BaseException(ERROR_GENERAL)
        return ResultWrapper.Success((CurrentUserResponse(user.uid, user.isEmailVerified)))
    }


    override suspend fun logoutRemote(): ResultWrapper<Boolean> {
        firebaseAuth.signOut()
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

        val personalHash: MutableMap<String, Any> = HashMap()
        personalHash["name"] = request.fname + " " + request.sname

        val userData = fireStoreDB.collection(USERS).document(user.uid).collection(DATA)

        userData.document(PERSONAL).set(personalHash).await()

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

}