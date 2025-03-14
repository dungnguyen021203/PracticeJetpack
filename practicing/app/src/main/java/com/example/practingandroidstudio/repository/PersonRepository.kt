package com.example.practingandroidstudio.repository

import com.example.practingandroidstudio.data.Person
import com.example.practingandroidstudio.data.PersonDao
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {
    val readAll = personDao.readAll()

}