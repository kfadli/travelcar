package com.kfadli.travelcar.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kfadli.core.account.User
import com.kfadli.core.repositories.UserRepository
import com.kfadli.travelcar.TravelCarApplication
import com.kfadli.travelcar.model.UIState

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = AccountViewModel::class.java.simpleName
    }

    private val repository: UserRepository =
        (application as TravelCarApplication).coreManager.userRepository

    private val _state = MutableLiveData<UIState<User?>>()
    private var user: User? = null

    val state: LiveData<UIState<User?>> = _state

    suspend fun getUser() {
        _state.postValue(UIState.Loading)

        this.user = repository.get()

        _state.postValue(
            UIState.Success(user)
        )
    }

    fun submit(newUser: User) {
        _state.postValue(UIState.Loading)

        //repository.save(newUser)

        // reassign newUser to current User
        this.user = newUser

        _state.postValue(UIState.Success(newUser))
        _state.postValue(UIState.NavigationAction.ReadForm)
    }
}