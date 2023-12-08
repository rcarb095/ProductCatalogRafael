package com.example.productcatalog;



import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


@RunWith(AndroidJUnit4.class)
    @LargeTest
    public class FirstTest {

        private String mStringToBetyped;
        private String mNumberToBetyped;
        @Rule
        public ActivityScenarioRule<MainActivity> mActivityRule = new ActivityScenarioRule<MainActivity>(
                MainActivity.class);

        @Before
        public void initValidString() {
            // Specify a valid string.
            mStringToBetyped = "Espresso";
            mNumberToBetyped = "1234";
        }

        @Test
        public void changeText_sameActivity() {
            // Type text and then press the button.
            onView(withId(R.id.editTextName))
                    .perform(typeText(mStringToBetyped), closeSoftKeyboard());
            onView(withId(R.id.editTextPrice))
                    .perform(typeText(mNumberToBetyped), closeSoftKeyboard());

            onView(withId(R.id.addButton)).perform(click());

            // Check that the text was changed.
            //onView(withId(R.id.listViewProducts))
              //      .check(matches(withText(mStringToBetyped)));
            onData(anything()).inAdapterView(withId(R.id.listViewProducts)).atPosition(0).onChildView(withId(R.id.textViewName)).check(matches(withText("Espresso")));

        }
    }
