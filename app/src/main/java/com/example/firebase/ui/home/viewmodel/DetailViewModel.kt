package com.example.firebase.ui.home.viewmodel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.model.Mahasiswa
import com.example.firebase.repository.RepositoryMhs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailUiState {
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    object Error : DetailUiState()
    object Loading : DetailUiState()
}
class DetailViewModel(private val mhs: RepositoryMhs) : ViewModel() {
    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getMhs(nim: String) {
        viewModelScope.launch {
            _detailUiState.value = DetailUiState.Loading
            _detailUiState.value = try {
                val mahasiswa = mhs.getMhs(nim)
                DetailUiState.Success(mahasiswa)
            } catch (e: IOException) {
                DetailUiState.Error
            } catch (e: HttpException) {
                DetailUiState.Error
            }
        }
    }
}