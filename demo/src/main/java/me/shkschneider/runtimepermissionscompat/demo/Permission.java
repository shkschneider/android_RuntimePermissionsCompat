package me.shkschneider.runtimepermissionscompat.demo;

import android.content.pm.PermissionInfo;

public class Permission {

    public String name;
    public int protectionLevel;

    public Permission(final String name, final int protectionLevel) {
        this.name = name;
        this.protectionLevel = protectionLevel;
    }

    public String name() {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    @SuppressWarnings("deprecation")
    public String protectionLevel() {
        switch (this.protectionLevel) {
            case PermissionInfo.PROTECTION_NORMAL:
                return "PROTECTION_NORMAL";
            case PermissionInfo.PROTECTION_DANGEROUS:
                return "PROTECTION_DANGEROUS";
            case PermissionInfo.PROTECTION_SIGNATURE:
                return "PROTECTION_SIGNATURE";
            case PermissionInfo.PROTECTION_SIGNATURE | PermissionInfo.PROTECTION_FLAG_PRIVILEGED:
                return "PROTECTION_PRIVILEGED";
            case PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM:
                return "PROTECTION_SYSTEM";
            default:
                return "<UNKNOWN>";
        }
    }

}
