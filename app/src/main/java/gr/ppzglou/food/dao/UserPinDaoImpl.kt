package gr.ppzglou.food.dao

import javax.inject.Inject

class UserPinDaoImpl @Inject constructor(
    private val userPinDao: UserPinDao
) : UserPinDao {

    override suspend fun getAll(): MutableList<UserPinEntity> {
        return userPinDao.getAll()
    }

    override suspend fun insert(userPinEntity: UserPinEntity) {
        userPinDao.insert(userPinEntity)
    }

    override fun findById(id: String): UserPinEntity {
        return userPinDao.findById(id)
    }

    override suspend fun delete(userPinEntity: UserPinEntity) {
        userPinDao.delete(userPinEntity)
    }

    override suspend fun deleteAll() {
        userPinDao.deleteAll()
    }
}