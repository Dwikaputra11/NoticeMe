package com.example.noticeme.dummy

import com.example.noticeme.R
import com.example.noticeme.data.Note
import com.example.noticeme.data.OnBoarding

class DummyData {

    companion object{
        val onBoardingItem = arrayListOf<OnBoarding>(
            OnBoarding(title = "Make Note", desc = "Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet", img = R.drawable.ic_on_boarding_1),
            OnBoarding(title = "Make Note Easy", desc = "Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet", img = R.drawable.ic_on_boarding_2),
            OnBoarding(title = "Make Note Easy", desc = "Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet Lorem Ipsum Dolor sit Amet", img = R.drawable.ic_on_boarding_1),
        )

        val noteList = listOf<Note>(
            Note(title = "Breakfast", "Eat My Breakfast", "health"),
            Note(title = "Lunch", "Eat My Lunch", "health"),
            Note(title = "Dinner", "Eat My Dinner", "health"),
            Note(title = "Football", "Playing Football", "sport"),
            Note(title = "Sleep", "Sleeping Well", "health"),

        )
    }

}