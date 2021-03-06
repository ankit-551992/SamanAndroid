package com.algorepublic.saman;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.qtech.saman.R;
import com.qtech.saman.ui.activities.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void ensureFieldsNotEmpty() {

        onView(withId(R.id.button_login)).perform(scrollTo(),click());
        delay(1000);

        onView(withText("Okay")).perform(scrollTo(),click());
        delay(1000);

        onView(withId(R.id.editText_email)).perform(typeText("naeem.ibrahim@algorepublic.com"), closeSoftKeyboard());
        delay(1500);

        onView(withId(R.id.button_login)).perform(scrollTo(),click());
        delay(1000);

        onView(withText("Okay")).perform(scrollTo(),click());
        delay(1000);

        onView(withId(R.id.editText_password)).perform(typeText("12345678987"), closeSoftKeyboard());
        delay(1500);

        onView(withId(R.id.button_login)).perform(scrollTo(),click());
        delay(2000);

        onView(withText("Try Again")).perform(scrollTo(),click());

        delay(4000);

    }


    private void delay(long milliSeconds){
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
