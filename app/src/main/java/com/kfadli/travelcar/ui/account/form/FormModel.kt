package com.kfadli.travelcar.ui.account.form

import android.graphics.Bitmap
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.kfadli.core.account.User
import com.kfadli.travelcar.BR
import java.util.*

class FormModel(
    user: User?
) : BaseObservable() {

    @get:Bindable
    var firstName: String = user?.firstName ?: ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    var lastName: String = user?.lastName ?: ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    var address: String = user?.address ?: ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.address)
        }

    @get:Bindable
    var birthday: Date? = user?.date
        set(value) {
            field = value
            notifyPropertyChanged(BR.birthday)
        }

    @get:Bindable
    var thumbnail: Bitmap? = user?.thumbnail
        set(value) {
            field = value
            notifyPropertyChanged(BR.thumbnail)
        }

    @get:Bindable
    val isValid: Boolean
        get() {
            return firstName.isNotBlank() && lastName.isNotBlank()
        }


    fun createUser(): User = User(
        firstName = firstName,
        lastName = lastName,
        address = address,
        date = birthday,
        thumbnail = thumbnail
    )

}