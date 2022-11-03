package com.example.noticeme.helper

import com.example.noticeme.R
import com.example.noticeme.model.OnBoarding

class DummyData {

    companion object {
        val onBoardingItem = arrayListOf(
            OnBoarding(
                title = "Make Note",
                desc = "Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet",
                img = R.drawable.ic_on_boarding_1
            ),
            OnBoarding(
                title = "Make Note Easy",
                desc = "Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet",
                img = R.drawable.ic_on_boarding_2
            ),
            OnBoarding(
                title = "Make Note Easy",
                desc = "Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet",
                img = R.drawable.ic_on_boarding_1
            ),
        )
    }
}

enum class QueryMenu{
    ADD,
    EDIT,
    DELETE,
}