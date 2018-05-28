package com.mpetroiu.uniapplication;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.*;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 24)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MainTestClass {

    private static final String TAG = "MainTestClass";
    private static final String APP_PACKAGE = "com.mpetroiu.uniapplication";
    private static final int LAUNCH_TIMEOUT = 5000;
    private FirebaseAuth mAuth;
    private UiDevice mDevice;
    private Context context;

    /**
     * Method userd for registering users
     * with a random number
     */
    private String randomInt() {
        return String.valueOf(((new Random()).nextInt(100000)));
    }

    /**
     * First Test : Open Application
     * 1.Press Home
     * 2.Opens Application after Package name
     */
    @Test
    public void testA1_startApp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        mDevice.pressHome();

        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(APP_PACKAGE);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * Second Test : Test Logout function.
     * 1.Press Home
     * 2.Open Application
     * 3.Verify if user is logged
     * 4.Press on Navigation Button
     * 5.Press on Logout Button
     */
    @Test
    public void testA2_verifyLogOut() throws UiObjectNotFoundException {
        testA1_startApp();
        mDevice.waitForIdle();

        UiObject navButton = mDevice.findObject(new UiSelector()
                .className("android.widget.ImageButton")
                .descriptionContains("Navigate up"));

        UiObject logoutBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.CheckedTextView")
                .resourceId("com.mpetroiu.uniapplication:id/design_menu_item_text")
                .text("Logout"));

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            navButton.click();
            mDevice.waitForIdle();
            logoutBtn.click();
            mDevice.waitForIdle();
        } else {
            mDevice.waitForIdle();
        }
    }

    /**
     * Third Test: Opens Login Option Activity
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Verify login option activity opened
     */
    @Test
    public void testA3_goToLoginOptions() throws UiObjectNotFoundException {
        testA2_verifyLogOut();

        UiObject loginBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/loginBtn")
                .text("LOGIN"));

        UiObject emailLogin = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/emailLogin")
                .text("Login with email"));

        mDevice.waitForIdle();
        loginBtn.click();
        assertTrue("Account was not deleted", emailLogin.exists() );
        mDevice.waitForIdle();
    }

    /**
     * Fourth test: Go to Login with Email
     * 1.Open Application
     *  2.Verify if user is logged
     *  2.1.Press on Navigation Button
     * 2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with email
     * 5.Verify that Login page opened
     */
    @Test
    public void testA4_goToLogin() throws UiObjectNotFoundException {
        testA3_goToLoginOptions();
        mDevice.waitForIdle();

        UiObject emailLogin = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/emailLogin")
                .text("Login with email"));

        UiObject verifyLogin =mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("com.mpetroiu.uniapplication:id/requiredText")
                .text("Enter your details to log in to your account"));

        emailLogin.click();
        assertTrue("Login with email did not open",verifyLogin.exists());
        mDevice.waitForIdle();

    }

    /**
     * Fifth Test: Verify EditText are visible.
     * 1.Open Application
     *  2.Verify if user is logged
     *  2.1.Press on Navigation Button
     * 2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with email
     * 5.Verify email EditText exists
     * 6.Verify password EditText exists
     */
    @Test
    public void testA5_EditTextAreCreated() throws UiObjectNotFoundException {
        testA4_goToLogin();
        mDevice.waitForIdle();

        UiObject emailInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/inputEmail")
                .text("Email"));

        UiObject passInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/inputPass")
                .text("Password"));

        assertNotNull(emailInput);
        assertNotNull(passInput);
    }

    /**
     * Sixth Test: Verify User can Login with Email & Password
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with email
     * 5.Verify Email and Password EditText exists
     * 5.Complete Email and
     * 6.Press on Login Button
     * 7.Verify Login was successful
     */
    @Test
    public void testA6_LoginWithEmail() throws UiObjectNotFoundException {
        String email = "moise@yahoo.com";
        String pass = "123456";

        testA4_goToLogin();
        mDevice.waitForIdle();

        UiObject emailInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/inputEmail")
                .text("Email"));

        UiObject passInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/inputPass")
                .text("Password"));

        UiObject signInBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/signIn")
                .text("Login"));

        emailInput.clearTextField();
        passInput.clearTextField();
        assertTrue("Email EditText not found ", emailInput.exists());

        emailInput.setText(email);
        mDevice.waitForIdle();

        assertTrue("Password EditText not found ", passInput.exists());

        passInput.setText(pass);
        mDevice.waitForIdle();

        signInBtn.click();
        mDevice.waitForIdle();
    }

    /**
     * Seventh Test: Verify new user can be created
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on New here? Create an account
     * 5.Verify Name, Email and Password EditText exists
     * 5.Complete Name, Email and Password
     * 6.Press on Create Account Button
     * 7.Verify Account was created successful
     */
    @Test
    public void testA7_CreateAcc() throws UiObjectNotFoundException {
        String name = "nume" + randomInt();
        String email = "user" + randomInt() + "@example.com";
        String pass = "password" + randomInt();

        testA3_goToLoginOptions();
        mDevice.waitForIdle();

        UiObject createAccLink = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("com.mpetroiu.uniapplication:id/newUser")
                .text("New here? Create an account"));

        UiObject nameInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/username")
                .text("Name"));

        UiObject emailInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/inputEmail")
                .text("Email"));

        UiObject passInput = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText")
                .resourceId("com.mpetroiu.uniapplication:id/inputPass")
                .text("Password"));

        UiObject createAccBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/signUp")
                .text("Create account"));

        createAccLink.click();
        mDevice.waitForIdle();

        assertTrue("Name EditText not found ", nameInput.exists());
        nameInput.setText(name);
        mDevice.waitForIdle();

        assertTrue("Email EditText not found ", emailInput.exists());
        emailInput.setText(email);
        mDevice.waitForIdle();

        assertTrue("Password EditText not found ", passInput.exists());
        passInput.setText(pass);
        mDevice.waitForIdle();

        createAccBtn.click();
        mDevice.waitForIdle();

    }

    /**
     * Eighth Test: Verify User can login with google
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with Google
     * 5.Verify Account selection is opened
     * 6.Select Account
     * 7.Verify Login was successful
     */
    @Test
    public void testA8_signInWithGoogle() throws UiObjectNotFoundException {
        testA3_goToLoginOptions();
        mDevice.waitForIdle();

        UiObject withGoogle = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/googleLogin")
                .text("Login with Google"));

        UiObject chooseGoogleAcc = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("com.google.android.gms:id/title")
                .text("Choose account for Search My City"));

        UiObject selectAcc = mDevice.findObject(new UiSelector()
                .packageName("com.google.android.gms")
                .className("android.widget.LinearLayout")
                .index(0));

        withGoogle.click();
        mDevice.waitForIdle();

        assertTrue("Login with Google Failed", chooseGoogleAcc.exists());
        selectAcc.click();
        mDevice.waitForIdle();
    }

    /**
     * Ninth Test: Verify User can login with Facebook
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with Facebook
     * 5.Verify Login was successful
     */
    @Test
    public void testA9_signInWithFacebook() throws UiObjectNotFoundException {
        testA3_goToLoginOptions();
        mDevice.waitForIdle();

        UiObject withFacebook = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/facebookLogin")
                .text("Login with Facebook"));

        withFacebook.click();
        mDevice.waitForIdle();
    }

    /**
     * Tenth Test: Verify Navigation Drawer is opening
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with Email
     * 5.Enter Email and Pass
     * 6.Press on Login
     * 6.Press on Navigation Button
     * 7.Verify Navigation drawer is opened
     */
    @Test
    public void testB1_verifyNavBar() throws UiObjectNotFoundException {
        testA6_LoginWithEmail();
        mDevice.waitForIdle();

        UiObject navButton = mDevice.findObject(new UiSelector()
                .className("android.widget.ImageButton")
                .descriptionContains("Navigate up"));

        UiObject actionTV = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .text("Actions"));

        assertTrue("Navigation drawer was not opened", actionTV.exists());
        navButton.click();
        mDevice.waitForIdle();
    }

    /**
     * Eleventh Test: Verify Settings is opening
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with Email
     * 5.Enter Email and Pass
     * 6.Press on Login
     * 6.Press on Navigation Button
     * 7.Press on Settings
     * 8.Verify Settings is opening
     */
    @Test
    public void testB2_goToSettings() throws UiObjectNotFoundException {
        testB1_verifyNavBar();
        mDevice.waitForIdle();

        UiObject settingBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.CheckedTextView")
                .resourceId("com.mpetroiu.uniapplication:id/design_menu_item_text")
                .text("Settings"));
        UiObject actionTV = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .text("Actions"));

        assertTrue("Navigation drawer was not opened", actionTV.exists());
        settingBtn.click();
        mDevice.waitForIdle();
    }

    /**
     * Twelfth Test: Verify Settings is opening
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with Email
     * 5.Enter Email and Pass
     * 6.Press on Login
     * 6.Press on Navigation Button
     * 7.Press on Settings
     * 8.Press on Account
     * 9.Verify Account is opened
     * 10.Press on Application
     * 11.Verify Application is opened
     * 12.Press on Password
     * 13.Verify Password is opening
     */
    @Test
    public void testB3_verifyTabsAreWorking() throws UiObjectNotFoundException {
        testB2_goToSettings();
        mDevice.waitForIdle();

        UiObject tabLayout = mDevice.findObject(new UiSelector()
                .className("android.widget.HorizontalScrollView")
                .resourceId("com.mpetroiu.uniapplication:id/tabLayout"));

        UiObject deleteAcc = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/deleteAccount")
                .text("Delete Account"));

        UiObject notifSettings = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("com.mpetroiu.uniapplication:id/notifcationSeparator")
                .text("Notification settings"));

        UiObject changePass = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .resourceId("com.mpetroiu.uniapplication:id/changePass")
                .text("Change Password"));




        tabLayout.getChild(new UiSelector().text("Account")).click();
        mDevice.waitForIdle();
        assertTrue("Account Tab was not selected",deleteAcc.exists());

        tabLayout.getChild(new UiSelector().text("Application")).click();
        mDevice.waitForIdle();
        assertTrue("Application Tab was not selected ",notifSettings.exists());

        tabLayout.getChild(new UiSelector().text("Password")).click();
        mDevice.waitForIdle();
        assertTrue("Password Tab was not selected",changePass.exists());
    }

    /**
     * Thirteenth  Test: Verify Settings is opening
     * 1.Open Application
     * 2.Verify if user is logged
     *  2.1.Press on Navigation Button
     *  2.2.Press on Logout Button
     * 3.Press on Login Button
     * 4.Press on Login with Email
     * 5.Enter Email and Pass
     * 6.Press on Login
     * 6.Press on Navigation Button
     * 7.Press on Settings
     * 8.Press on Account
     * 9.Verify Account is opened
     * 10.Press on Delete Account
     * 11.Verify Account is deleted.
     */
    @Test
    public void testB4_verifyDeleteAccBtn() throws UiObjectNotFoundException {
        testA8_signInWithGoogle();
        mDevice.waitForIdle();

        UiObject navButton = mDevice.findObject(new UiSelector()
                .className("android.widget.ImageButton")
                .descriptionContains("Navigate up"));

        UiObject tabLayout = mDevice.findObject(new UiSelector()
                .className("android.widget.HorizontalScrollView")
                .resourceId("com.mpetroiu.uniapplication:id/tabLayout"));

        UiObject deleteAcc = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/deleteAccount")
                .text("Delete Account"));

        UiObject settingBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.CheckedTextView")
                .resourceId("com.mpetroiu.uniapplication:id/design_menu_item_text")
                .text("Settings"));

        UiObject actionTV = mDevice.findObject(new UiSelector()
                .className("android.widget.TextView")
                .text("Actions"));

        UiObject emailLogin = mDevice.findObject(new UiSelector()
                .className("android.widget.Button")
                .resourceId("com.mpetroiu.uniapplication:id/emailLogin")
                .text("Login with email"));

        navButton.click();
        mDevice.waitForIdle();
        assertTrue("Navigation drawer was not opened", actionTV.exists());
        System.out.println("Navigation drawer was opened" + actionTV.exists());

        settingBtn.click();
        mDevice.waitForIdle();
        assertTrue("Settings was not opened", tabLayout.exists());

        tabLayout.getChild(new UiSelector().text("Account")).click();
        mDevice.waitForIdle();
        assertTrue("Account Tab was not selected",deleteAcc.exists());

        deleteAcc.click();
        mDevice.waitForIdle();
        assertTrue("Account was not deleted", emailLogin.exists() );
    }
}