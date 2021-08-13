package com.kfadli.travelcar.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kfadli.core.account.User
import com.kfadli.core.repositories.UserRepository
import com.kfadli.travelcar.TravelCarApplication
import com.kfadli.travelcar.model.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val scope = CoroutineScope(Dispatchers.Main)

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

        scope.launch {
            repository.save(newUser)
            // reassign newUser to current User
            this@AccountViewModel.user = newUser

            _state.postValue(UIState.Success(newUser))
            _state.postValue(UIState.NavigationAction.ReadForm)
        }
    }
}