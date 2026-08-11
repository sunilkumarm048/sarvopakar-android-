package com.sarvopakar.provider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * One-tap jumps to the phone screens a provider must configure for reliable,
 * loud order alerts. Android does not allow apps to flip these switches
 * silently (by design — imagine spam apps enabling their own lock-screen
 * popups), so the best possible UX is deep-linking straight to the right
 * screen with clear instructions.
 */
@CapacitorPlugin(name = "DeviceSettings")
public class DeviceSettingsPlugin extends Plugin {

    /** The app's notification settings — where channel sound lives. */
    @PluginMethod
    public void openNotificationSettings(PluginCall call) {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
        getContext().startActivity(intent);
        call.resolve();
    }

    /**
     * Ask Android to exempt us from battery optimization — this one IS a real
     * system dialog the user just accepts. Returns { already: true } when the
     * exemption is already granted.
     */
    @PluginMethod
    public void requestIgnoreBatteryOptimizations(PluginCall call) {
        JSObject out = new JSObject();
        PowerManager pm = (PowerManager) getContext().getSystemService(android.content.Context.POWER_SERVICE);
        String pkg = getContext().getPackageName();
        if (pm != null && pm.isIgnoringBatteryOptimizations(pkg)) {
            out.put("already", true);
            call.resolve(out);
            return;
        }
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            .setData(Uri.parse("package:" + pkg));
        getContext().startActivity(intent);
        out.put("already", false);
        call.resolve(out);
    }

    /**
     * MIUI's "Other permissions" editor (Show on lock screen / open in
     * background). Falls back to the generic app-details page on other ROMs.
     */
    @PluginMethod
    public void openOtherPermissions(PluginCall call) {
        String pkg = getContext().getPackageName();
        try {
            Intent miui = new Intent("miui.intent.action.APP_PERM_EDITOR")
                .setClassName("com.miui.securitycenter",
                              "com.miui.permcenter.permissions.PermissionsEditorActivity")
                .putExtra("extra_pkgname", pkg);
            getContext().startActivity(miui);
        } catch (Exception e) {
            Intent details = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + pkg));
            getContext().startActivity(details);
        }
        call.resolve();
    }
}
