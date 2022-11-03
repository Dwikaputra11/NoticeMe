package com.example.noticeme.helper

import androidx.lifecycle.LiveData
import com.example.noticeme.model.Note

sealed class Resource<out T>(
    open val data: LiveData<List<Note>>?,
    open val loading: Boolean
) {
    class Success<T>(
        data: LiveData<List<Note>>
    ) : Resource<T>(
        data = data,
        loading = false
    )

    class Loading<T>(
        cache: LiveData<List<Note>>? = null
    ) : Resource<T>(
        data = cache,
        loading = true
    )
}
