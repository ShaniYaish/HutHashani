package com.idz.huthashani.respage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idz.huthashani.utils.RequestStatus

class ResPageViewModel : ViewModel() {
    private val _uploadProfileImageResult = MutableLiveData<RequestStatus>()
    private val _changeResult = MutableLiveData<RequestStatus>()
    val uploadProfileImageResult: LiveData<RequestStatus> get() = _uploadProfileImageResult
    val changeResult: LiveData<RequestStatus> get() = _changeResult

    fun editPost(postId : String){

    }

}