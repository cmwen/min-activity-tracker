package io.cmwen.min_activity_tracker.features.permissions

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.cmwen.min_activity_tracker.ui.theme.MinactivitytrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for the Permissions Screen
 * Tests the permission request flows and UI states
 */
@RunWith(AndroidJUnit4::class)
class PermissionsScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun permissionsScreen_displaysTitle() {
        // Given
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = {}
                )
            }
        }
        
        // Then
        composeTestRule
            .onNodeWithText("Permissions")
            .assertExists()
            .assertIsDisplayed()
    }
    
    @Test
    fun permissionsScreen_displaysUsageAccessPermission() {
        // Given
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = {}
                )
            }
        }
        
        // Then
        composeTestRule
            .onNodeWithText("Usage Access")
            .assertExists()
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Required for tracking app usage")
            .assertExists()
            .assertIsDisplayed()
    }
    
    @Test
    fun permissionsScreen_displaysLocationPermission() {
        // Given
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = {}
                )
            }
        }
        
        // Then
        composeTestRule
            .onNodeWithText("Location")
            .assertExists()
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Optional - adds location context to sessions")
            .assertExists()
            .assertIsDisplayed()
    }
    
    @Test
    fun permissionsScreen_hasEnableButtons() {
        // Given
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = {}
                )
            }
        }
        
        // Then - should have enable buttons for permissions
        composeTestRule
            .onAllNodesWithText("Enable")
            .assertCountEquals(3) // Usage Access, Location, Notifications
    }
    
    @Test
    fun permissionsScreen_clickEnableUsageAccess_triggersAction() {
        // Given
        var clickedUsageAccess = false
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = {}
                )
            }
        }
        
        // When - Click the first Enable button (Usage Access)
        composeTestRule
            .onAllNodesWithText("Enable")[0]
            .performClick()
        
        // Then - verify click was handled (no crash)
        composeTestRule
            .onNodeWithText("Usage Access")
            .assertExists()
    }
    
    @Test
    fun permissionsScreen_hasBackButton() {
        // Given
        var backClicked = false
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = { backClicked = true }
                )
            }
        }
        
        // When
        composeTestRule
            .onNodeWithContentDescription("Navigate back")
            .performClick()
        
        // Then
        assert(backClicked)
    }
    
    @Test
    fun permissionsScreen_scrollable() {
        // Given
        composeTestRule.setContent {
            MinactivitytrackerTheme {
                PermissionsScreen(
                    onNavigateBack = {}
                )
            }
        }
        
        // When - Scroll to bottom
        composeTestRule
            .onNodeWithText("Notifications")
            .performScrollTo()
        
        // Then - Bottom content should be visible
        composeTestRule
            .onNodeWithText("Notifications")
            .assertIsDisplayed()
    }
}
