package com.example.firebase.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.model.Mahasiswa
import com.example.firebase.repository.RepositoryMhs
import kotlinx.coroutines.launch

class InsertViewModel(
    private val repositoryMhs: RepositoryMhs
): ViewModel(){
    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set

    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    fun updateState(mahasiswaEvent: MahasiswaEvent){
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent,
        )
    }

    fun validateFields(): Boolean{
        val event = uiEvent.insertUiEvent
        val errorState = FormErrorState(
            nim = if(event.nim.isEmpty()) "NIM Tidak Boleh Kosong" else null,
            nama = if(event.nama.isEmpty()) "Nama Tidak Boleh Kosong" else null,
            jenisKelamin = if(event.jenisKelamin.isEmpty()) "Jenis Kelamin Tidak Boleh Kosong" else null,
            alamat = if(event.alamat.isEmpty()) "Alamat Tidak Boleh Kosong" else null,
            kelas = if(event.kelas.isEmpty()) "Kelas Tidak Boleh Kosong" else null,
            angkatan = if(event.angkatan.isEmpty()) "Angkatan Tidak Boleh Kosong" else null,
            judul = if(event.judul.isEmpty()) "Judul Tidak Boleh Kosong" else null,
            dospem1 = if(event.dospem1.isEmpty()) "Dospem1 Tidak Boleh Kosong" else null,
            dospem2 = if(event.dospem2.isEmpty()) "Dospem2 Tidak Boleh Kosong" else null
        )
        uiEvent = uiEvent.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun insertMhs(){
        if(validateFields()){
            viewModelScope.launch {
                uiState = FormState.Loading
                try {
                    repositoryMhs.insertMhs(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data berhasil disimpan")
                }catch (e: Exception) {
                    uiState = FormState.Error("Data gagal disimpan")
                }
            }
        } else{
            uiState = FormState.Error("Data tidak valid")
        }
    }

    fun resetForm(){
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }

    fun resetSnackBarMessage(){
        uiState = FormState.Idle
    }
}

sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}

data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
    val judul: String? = null,
    val dospem1: String? = null,
    val dospem2: String? = null
){
    fun isValid():Boolean{
        return nim == null &&
                nama == null &&
                jenisKelamin == null &&
                alamat == null &&
                kelas == null &&
                angkatan == null &&
                judul == null &&
                dospem1 == null &&
                dospem2 == null
    }
}

data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val jenisKelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "",
    val judul: String = "",
    val dospem1: String = "",
    val dospem2: String = ""
)

fun MahasiswaEvent.toMhsModel(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    jenisKelamin = jenisKelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan,
    judul = judul,
    dospem1 = dospem1,
    dospem2 = dospem2
)