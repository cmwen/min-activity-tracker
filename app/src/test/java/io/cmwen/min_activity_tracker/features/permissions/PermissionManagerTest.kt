package io.cmwen.min_activity_tracker.features.permissions

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PermissionManagerTest {

    private lateinit var context: Context
    private lateinit var appOpsManager: AppOpsManager
    private lateinit var permissionManager: PermissionManager

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        appOpsManager = mockk(relaxed = true)
        mockkStatic(ContextCompat::class)
        every { context.getSystemService(Context.APP_OPS_SERVICE) } returns appOpsManager
        permissionManager = PermissionManager(context)
    }

    @Test
    fun `isUsageStatsPermissionGranted returns true when permission is granted`() {
        every {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                0,
                ""
            )
        } returns AppOpsManager.MODE_ALLOWED

        assertTrue(permissionManager.isUsageStatsPermissionGranted())
    }

    @Test
    fun `isUsageStatsPermissionGranted returns false when permission is not granted`() {
        every {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                0,
                ""
            )
        } returns AppOpsManager.MODE_ERRORED

        assertFalse(permissionManager.isUsageStatsPermissionGranted())
    }

    @Test
    fun `hasAllPermissions returns true when all permissions are granted`() {
        PermissionManager.allPermissions.forEach { permission ->
            every {
                ContextCompat.checkSelfPermission(context, permission)
            } returns PackageManager.PERMISSION_GRANTED
        }
        every {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                0,
                ""
            )
        } returns AppOpsManager.MODE_ALLOWED

        assertTrue(permissionManager.hasAllPermissions())
    }

    @Test
    fun `hasAllPermissions returns false when some permissions are not granted`() {
        every {
            ContextCompat.checkSelfPermission(context, PermissionManager.allPermissions[0])
        } returns PackageManager.PERMISSION_DENIED
        every {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                0,
                ""
            )
        } returns AppOpsManager.MODE_ALLOWED

        assertFalse(permissionManager.hasAllPermissions())
    }
}
