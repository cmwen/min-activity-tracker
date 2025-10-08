package io.cmwen.min_activity_tracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.cmwen.min_activity_tracker.di.AppModule
import io.cmwen.min_activity_tracker.features.permissions.PermissionManager
import io.mockk.every
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class PermissionFlowTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var permissionManager: PermissionManager

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testPermissionScreenIsShownWhenPermissionsAreNotGranted() {
        every { permissionManager.hasAllPermissions() } returns false
        composeTestRule.onNodeWithText("Permissions Required").assertIsDisplayed()
    }

    @Test
    fun testMainScreenIsShownWhenPermissionsAreGranted() {
        every { permissionManager.hasAllPermissions() } returns true
        composeTestRule.onNodeWithText("Dashboard").assertIsDisplayed()
    }
}
