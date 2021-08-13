package com.kfadli.travelcar.model

sealed class UIState<out R> {
    data class Success<T>(val data: T) : UIState<T>()
    data class Failure(val exception: Throwable) : UIState<Nothing>()
    object Loading : UIState<Nothing>()

    sealed class NavigationAction : UIState<Nothing>() {
        object ReadForm : NavigationAction()
    }
}