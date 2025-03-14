package com.example.practingandroidstudio

import androidx.lifecycle.ViewModel
import com.example.practingandroidstudio.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    personRepository: PersonRepository
): ViewModel() {
    val readAll = personRepository.readAll
}