package gr.ppzglou.food.dao.fav

import javax.inject.Inject

class FavDaoImpl @Inject constructor(
    private val favDao: FavDao
) : FavDao {

    override suspend fun getAll(): MutableList<FavEntity> {
        return favDao.getAll()
    }

    override suspend fun insert(favEntity: FavEntity) {
        favDao.insert(favEntity)
    }

    override suspend fun findById(id: String): FavEntity {
        return favDao.findById(id)
    }

    override suspend fun delete(favEntity: FavEntity) {
        favDao.delete(favEntity)
    }

    override suspend fun deleteAll() {
        favDao.deleteAll()
    }
}