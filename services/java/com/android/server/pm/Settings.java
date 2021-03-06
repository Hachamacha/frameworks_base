/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.pm;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
<<<<<<< HEAD
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
=======
>>>>>>> upstream/master

import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.JournaledFile;
import com.android.internal.util.XmlUtils;
import com.android.server.IntentResolver;
import com.android.server.pm.PackageManagerService.DumpState;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

<<<<<<< HEAD
import android.app.AppGlobals;
=======
>>>>>>> upstream/master
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PermissionInfo;
import android.content.pm.Signature;
<<<<<<< HEAD
import android.content.pm.UserInfo;
=======
>>>>>>> upstream/master
import android.content.pm.VerifierDeviceIdentity;
import android.os.Binder;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Process;
<<<<<<< HEAD
import android.os.RemoteException;
import android.os.UserId;
=======
>>>>>>> upstream/master
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
<<<<<<< HEAD
import java.util.List;
import java.util.Map;
=======
>>>>>>> upstream/master

import libcore.io.IoUtils;

/**
 * Holds information about dynamic settings.
 */
final class Settings {
    private static final String TAG = "PackageSettings";

    private static final boolean DEBUG_STOPPED = false;

<<<<<<< HEAD
    private static final String TAG_READ_EXTERNAL_STORAGE = "read-external-storage";
    private static final String ATTR_ENFORCEMENT = "enforcement";

    private static final String TAG_ITEM = "item";
    private static final String TAG_DISABLED_COMPONENTS = "disabled-components";
    private static final String TAG_ENABLED_COMPONENTS = "enabled-components";
    private static final String TAG_PACKAGE_RESTRICTIONS = "package-restrictions";
    private static final String TAG_PACKAGE = "pkg";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_NOT_LAUNCHED = "nl";
    private static final String ATTR_ENABLED = "enabled";
    private static final String ATTR_STOPPED = "stopped";

=======
>>>>>>> upstream/master
    private final File mSettingsFilename;
    private final File mBackupSettingsFilename;
    private final File mPackageListFilename;
    private final File mStoppedPackagesFilename;
    private final File mBackupStoppedPackagesFilename;
    final HashMap<String, PackageSetting> mPackages =
            new HashMap<String, PackageSetting>();
    // List of replaced system applications
<<<<<<< HEAD
    private final HashMap<String, PackageSetting> mDisabledSysPackages =
=======
    final HashMap<String, PackageSetting> mDisabledSysPackages =
>>>>>>> upstream/master
        new HashMap<String, PackageSetting>();

    // These are the last platform API version we were using for
    // the apps installed on internal and external storage.  It is
    // used to grant newer permissions one time during a system upgrade.
    int mInternalSdkPlatform;
    int mExternalSdkPlatform;

<<<<<<< HEAD
    Boolean mReadExternalStorageEnforced;

=======
>>>>>>> upstream/master
    /** Device identity for the purpose of package verification. */
    private VerifierDeviceIdentity mVerifierDeviceIdentity;

    // The user's preferred activities associated with particular intent
    // filters.
    final IntentResolver<PreferredActivity, PreferredActivity> mPreferredActivities =
                new IntentResolver<PreferredActivity, PreferredActivity>() {
        @Override
        protected String packageForFilter(PreferredActivity filter) {
            return filter.mPref.mComponent.getPackageName();
        }
        @Override
        protected void dumpFilter(PrintWriter out, String prefix,
                PreferredActivity filter) {
            filter.mPref.dump(out, prefix, filter);
        }
    };
    final HashMap<String, SharedUserSetting> mSharedUsers =
            new HashMap<String, SharedUserSetting>();
    private final ArrayList<Object> mUserIds = new ArrayList<Object>();
    private final SparseArray<Object> mOtherUserIds =
            new SparseArray<Object>();

    // For reading/writing settings file.
    private final ArrayList<Signature> mPastSignatures =
            new ArrayList<Signature>();

    // Mapping from permission names to info about them.
    final HashMap<String, BasePermission> mPermissions =
            new HashMap<String, BasePermission>();

    // Mapping from permission tree names to info about them.
    final HashMap<String, BasePermission> mPermissionTrees =
            new HashMap<String, BasePermission>();

    // Packages that have been uninstalled and still need their external
    // storage data deleted.
    final ArrayList<String> mPackagesToBeCleaned = new ArrayList<String>();
    
    // Packages that have been renamed since they were first installed.
    // Keys are the new names of the packages, values are the original
    // names.  The packages appear everwhere else under their original
    // names.
    final HashMap<String, String> mRenamedPackages = new HashMap<String, String>();
    
    final StringBuilder mReadMessages = new StringBuilder();

    /**
     * Used to track packages that have a shared user ID that hasn't been read
     * in yet.
     * <p>
     * TODO: make this just a local variable that is passed in during package
     * scanning to make it less confusing.
     */
    private final ArrayList<PendingPackage> mPendingPackages = new ArrayList<PendingPackage>();

<<<<<<< HEAD
    private final File mSystemDir;
    Settings() {
        this(Environment.getDataDirectory());
    }

    Settings(File dataDir) {
        mSystemDir = new File(dataDir, "system");
        mSystemDir.mkdirs();
        FileUtils.setPermissions(mSystemDir.toString(),
                FileUtils.S_IRWXU|FileUtils.S_IRWXG
                |FileUtils.S_IROTH|FileUtils.S_IXOTH,
                -1, -1);
        mSettingsFilename = new File(mSystemDir, "packages.xml");
        mBackupSettingsFilename = new File(mSystemDir, "packages-backup.xml");
        mPackageListFilename = new File(mSystemDir, "packages.list");
        // Deprecated: Needed for migration
        mStoppedPackagesFilename = new File(mSystemDir, "packages-stopped.xml");
        mBackupStoppedPackagesFilename = new File(mSystemDir, "packages-stopped-backup.xml");
=======
    Settings() {
        File dataDir = Environment.getDataDirectory();
        File systemDir = new File(dataDir, "system");
        systemDir.mkdirs();
        FileUtils.setPermissions(systemDir.toString(),
                FileUtils.S_IRWXU|FileUtils.S_IRWXG
                |FileUtils.S_IROTH|FileUtils.S_IXOTH,
                -1, -1);
        mSettingsFilename = new File(systemDir, "packages.xml");
        mBackupSettingsFilename = new File(systemDir, "packages-backup.xml");
        mPackageListFilename = new File(systemDir, "packages.list");
        mStoppedPackagesFilename = new File(systemDir, "packages-stopped.xml");
        mBackupStoppedPackagesFilename = new File(systemDir, "packages-stopped-backup.xml");
>>>>>>> upstream/master
    }

    PackageSetting getPackageLPw(PackageParser.Package pkg, PackageSetting origPackage,
            String realName, SharedUserSetting sharedUser, File codePath, File resourcePath,
            String nativeLibraryPathString, int pkgFlags, boolean create, boolean add) {
        final String name = pkg.packageName;
        PackageSetting p = getPackageLPw(name, origPackage, realName, sharedUser, codePath,
                resourcePath, nativeLibraryPathString, pkg.mVersionCode, pkgFlags, create, add);
        return p;
    }

    PackageSetting peekPackageLPr(String name) {
        return mPackages.get(name);
    }

    void setInstallStatus(String pkgName, int status) {
        PackageSetting p = mPackages.get(pkgName);
        if(p != null) {
            if(p.getInstallStatus() != status) {
                p.setInstallStatus(status);
            }
        }
    }

    void setInstallerPackageName(String pkgName,
            String installerPkgName) {
        PackageSetting p = mPackages.get(pkgName);
        if(p != null) {
            p.setInstallerPackageName(installerPkgName);
        }
    }

    SharedUserSetting getSharedUserLPw(String name,
            int pkgFlags, boolean create) {
        SharedUserSetting s = mSharedUsers.get(name);
        if (s == null) {
            if (!create) {
                return null;
            }
            s = new SharedUserSetting(name, pkgFlags);
<<<<<<< HEAD
            s.userId = newUserIdLPw(s);
=======
            if (PackageManagerService.MULTIPLE_APPLICATION_UIDS) {
                s.userId = newUserIdLPw(s);
            } else {
                s.userId = PackageManagerService.FIRST_APPLICATION_UID;
            }
>>>>>>> upstream/master
            Log.i(PackageManagerService.TAG, "New shared user " + name + ": id=" + s.userId);
            // < 0 means we couldn't assign a userid; fall out and return
            // s, which is currently null
            if (s.userId >= 0) {
                mSharedUsers.put(name, s);
            }
        }

        return s;
    }

    boolean disableSystemPackageLPw(String name) {
        final PackageSetting p = mPackages.get(name);
        if(p == null) {
            Log.w(PackageManagerService.TAG, "Package:"+name+" is not an installed package");
            return false;
        }
        final PackageSetting dp = mDisabledSysPackages.get(name);
        // always make sure the system package code and resource paths dont change
        if (dp == null) {
            if((p.pkg != null) && (p.pkg.applicationInfo != null)) {
                p.pkg.applicationInfo.flags |= ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
            }
            mDisabledSysPackages.put(name, p);

            // a little trick...  when we install the new package, we don't
            // want to modify the existing PackageSetting for the built-in
            // version.  so at this point we need a new PackageSetting that
            // is okay to muck with.
            PackageSetting newp = new PackageSetting(p);
            replacePackageLPw(name, newp);
            return true;
        }
        return false;
    }

    PackageSetting enableSystemPackageLPw(String name) {
        PackageSetting p = mDisabledSysPackages.get(name);
        if(p == null) {
            Log.w(PackageManagerService.TAG, "Package:"+name+" is not disabled");
            return null;
        }
        // Reset flag in ApplicationInfo object
        if((p.pkg != null) && (p.pkg.applicationInfo != null)) {
            p.pkg.applicationInfo.flags &= ~ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        }
        PackageSetting ret = addPackageLPw(name, p.realName, p.codePath, p.resourcePath,
<<<<<<< HEAD
                p.nativeLibraryPathString, p.appId, p.versionCode, p.pkgFlags);
=======
                p.nativeLibraryPathString, p.userId, p.versionCode, p.pkgFlags);
>>>>>>> upstream/master
        mDisabledSysPackages.remove(name);
        return ret;
    }

<<<<<<< HEAD
    boolean isDisabledSystemPackageLPr(String name) {
        return mDisabledSysPackages.containsKey(name);
    }

    void removeDisabledSystemPackageLPw(String name) {
        mDisabledSysPackages.remove(name);
    }

=======
>>>>>>> upstream/master
    PackageSetting addPackageLPw(String name, String realName, File codePath, File resourcePath,
            String nativeLibraryPathString, int uid, int vc, int pkgFlags) {
        PackageSetting p = mPackages.get(name);
        if (p != null) {
<<<<<<< HEAD
            if (p.appId == uid) {
=======
            if (p.userId == uid) {
>>>>>>> upstream/master
                return p;
            }
            PackageManagerService.reportSettingsProblem(Log.ERROR,
                    "Adding duplicate package, keeping first: " + name);
            return null;
        }
        p = new PackageSetting(name, realName, codePath, resourcePath, nativeLibraryPathString,
                vc, pkgFlags);
<<<<<<< HEAD
        p.appId = uid;
=======
        p.userId = uid;
>>>>>>> upstream/master
        if (addUserIdLPw(uid, p, name)) {
            mPackages.put(name, p);
            return p;
        }
        return null;
    }

    SharedUserSetting addSharedUserLPw(String name, int uid, int pkgFlags) {
        SharedUserSetting s = mSharedUsers.get(name);
        if (s != null) {
            if (s.userId == uid) {
                return s;
            }
            PackageManagerService.reportSettingsProblem(Log.ERROR,
                    "Adding duplicate shared user, keeping first: " + name);
            return null;
        }
        s = new SharedUserSetting(name, pkgFlags);
        s.userId = uid;
        if (addUserIdLPw(uid, s, name)) {
            mSharedUsers.put(name, s);
            return s;
        }
        return null;
    }

    // Transfer ownership of permissions from one package to another.
    void transferPermissionsLPw(String origPkg, String newPkg) {
        // Transfer ownership of permissions to the new package.
        for (int i=0; i<2; i++) {
            HashMap<String, BasePermission> permissions =
                    i == 0 ? mPermissionTrees : mPermissions;
            for (BasePermission bp : permissions.values()) {
                if (origPkg.equals(bp.sourcePackage)) {
                    if (PackageManagerService.DEBUG_UPGRADE) Log.v(PackageManagerService.TAG,
                            "Moving permission " + bp.name
                            + " from pkg " + bp.sourcePackage
                            + " to " + newPkg);
                    bp.sourcePackage = newPkg;
                    bp.packageSetting = null;
                    bp.perm = null;
                    if (bp.pendingInfo != null) {
                        bp.pendingInfo.packageName = newPkg;
                    }
                    bp.uid = 0;
                    bp.gids = null;
                }
            }
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> upstream/master
    private PackageSetting getPackageLPw(String name, PackageSetting origPackage,
            String realName, SharedUserSetting sharedUser, File codePath, File resourcePath,
            String nativeLibraryPathString, int vc, int pkgFlags, boolean create, boolean add) {
        PackageSetting p = mPackages.get(name);
        if (p != null) {
            if (!p.codePath.equals(codePath)) {
                // Check to see if its a disabled system app
                if ((p.pkgFlags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // This is an updated system app with versions in both system
                    // and data partition. Just let the most recent version
                    // take precedence.
<<<<<<< HEAD
                    Slog.w(PackageManagerService.TAG, "Trying to update system app code path from "
                            + p.codePathString + " to " + codePath.toString());
                } else {
                    // Just a change in the code path is not an issue, but
                    // let's log a message about it.
                    Slog.i(PackageManagerService.TAG, "Package " + name + " codePath changed from "
                            + p.codePath + " to " + codePath + "; Retaining data and using new");
=======
                    Slog.w(PackageManagerService.TAG, "Trying to update system app code path from " +
                            p.codePathString + " to " + codePath.toString());
                } else {
                    // Just a change in the code path is not an issue, but
                    // let's log a message about it.
                    Slog.i(PackageManagerService.TAG, "Package " + name + " codePath changed from " + p.codePath
                            + " to " + codePath + "; Retaining data and using new");
>>>>>>> upstream/master
                    /*
                     * Since we've changed paths, we need to prefer the new
                     * native library path over the one stored in the
                     * package settings since we might have moved from
                     * internal to external storage or vice versa.
                     */
                    p.nativeLibraryPathString = nativeLibraryPathString;
                }
            }
            if (p.sharedUser != sharedUser) {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Package " + name + " shared user changed from "
                        + (p.sharedUser != null ? p.sharedUser.name : "<nothing>")
                        + " to "
                        + (sharedUser != null ? sharedUser.name : "<nothing>")
                        + "; replacing with new");
                p = null;
            } else {
                if ((pkgFlags&ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // If what we are scanning is a system package, then
                    // make it so, regardless of whether it was previously
                    // installed only in the data partition.
                    p.pkgFlags |= ApplicationInfo.FLAG_SYSTEM;
                }
            }
        }
        if (p == null) {
            // Create a new PackageSettings entry. this can end up here because
            // of code path mismatch or user id mismatch of an updated system partition
            if (!create) {
                return null;
            }
            if (origPackage != null) {
                // We are consuming the data from an existing package.
                p = new PackageSetting(origPackage.name, name, codePath, resourcePath,
                        nativeLibraryPathString, vc, pkgFlags);
<<<<<<< HEAD
                if (PackageManagerService.DEBUG_UPGRADE) Log.v(PackageManagerService.TAG, "Package "
                        + name + " is adopting original package " + origPackage.name);
=======
                if (PackageManagerService.DEBUG_UPGRADE) Log.v(PackageManagerService.TAG, "Package " + name
                        + " is adopting original package " + origPackage.name);
>>>>>>> upstream/master
                // Note that we will retain the new package's signature so
                // that we can keep its data.
                PackageSignatures s = p.signatures;
                p.copyFrom(origPackage);
                p.signatures = s;
                p.sharedUser = origPackage.sharedUser;
<<<<<<< HEAD
                p.appId = origPackage.appId;
=======
                p.userId = origPackage.userId;
>>>>>>> upstream/master
                p.origPackage = origPackage;
                mRenamedPackages.put(name, origPackage.name);
                name = origPackage.name;
                // Update new package state.
                p.setTimeStamp(codePath.lastModified());
            } else {
                p = new PackageSetting(name, realName, codePath, resourcePath,
                        nativeLibraryPathString, vc, pkgFlags);
                p.setTimeStamp(codePath.lastModified());
                p.sharedUser = sharedUser;
                // If this is not a system app, it starts out stopped.
                if ((pkgFlags&ApplicationInfo.FLAG_SYSTEM) == 0) {
                    if (DEBUG_STOPPED) {
                        RuntimeException e = new RuntimeException("here");
                        e.fillInStackTrace();
                        Slog.i(PackageManagerService.TAG, "Stopping package " + name, e);
                    }
<<<<<<< HEAD
                    List<UserInfo> users = getAllUsers();
                    if (users != null) {
                        for (UserInfo user : users) {
                            p.setStopped(true, user.id);
                            p.setNotLaunched(true, user.id);
                            writePackageRestrictionsLPr(user.id);
                        }
                    }
                }
                if (sharedUser != null) {
                    p.appId = sharedUser.userId;
                } else {
=======
                    p.stopped = true;
                    p.notLaunched = true;
                }
                if (sharedUser != null) {
                    p.userId = sharedUser.userId;
                } else if (PackageManagerService.MULTIPLE_APPLICATION_UIDS) {
>>>>>>> upstream/master
                    // Clone the setting here for disabled system packages
                    PackageSetting dis = mDisabledSysPackages.get(name);
                    if (dis != null) {
                        // For disabled packages a new setting is created
                        // from the existing user id. This still has to be
                        // added to list of user id's
                        // Copy signatures from previous setting
                        if (dis.signatures.mSignatures != null) {
                            p.signatures.mSignatures = dis.signatures.mSignatures.clone();
                        }
<<<<<<< HEAD
                        p.appId = dis.appId;
                        // Clone permissions
                        p.grantedPermissions = new HashSet<String>(dis.grantedPermissions);
                        // Clone component info
                        List<UserInfo> users = getAllUsers();
                        if (users != null) {
                            for (UserInfo user : users) {
                                int userId = user.id;
                                p.setDisabledComponents(
                                        new HashSet<String>(dis.getDisabledComponents(userId)),
                                        userId);
                                p.setEnabledComponents(
                                        new HashSet<String>(dis.getEnabledComponents(userId)),
                                        userId);
                            }
                        }
                        // Add new setting to list of user ids
                        addUserIdLPw(p.appId, p, name);
                    } else {
                        // Assign new user id
                        p.appId = newUserIdLPw(p);
                    }
                }
            }
            if (p.appId < 0) {
=======
                        p.userId = dis.userId;
                        // Clone permissions
                        p.grantedPermissions = new HashSet<String>(dis.grantedPermissions);
                        // Clone component info
                        p.disabledComponents = new HashSet<String>(dis.disabledComponents);
                        p.enabledComponents = new HashSet<String>(dis.enabledComponents);
                        // Add new setting to list of user ids
                        addUserIdLPw(p.userId, p, name);
                    } else {
                        // Assign new user id
                        p.userId = newUserIdLPw(p);
                    }
                } else {
                    p.userId = PackageManagerService.FIRST_APPLICATION_UID;
                }
            }
            if (p.userId < 0) {
>>>>>>> upstream/master
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Package " + name + " could not be assigned a valid uid");
                return null;
            }
            if (add) {
                // Finish adding new package by adding it and updating shared
                // user preferences
                addPackageSettingLPw(p, name, sharedUser);
            }
        }
        return p;
    }

    void insertPackageSettingLPw(PackageSetting p, PackageParser.Package pkg) {
        p.pkg = pkg;
<<<<<<< HEAD
        // pkg.mSetEnabled = p.getEnabled(userId);
        // pkg.mSetStopped = p.getStopped(userId);
=======
        pkg.mSetEnabled = p.enabled;
        pkg.mSetStopped = p.stopped;
>>>>>>> upstream/master
        final String codePath = pkg.applicationInfo.sourceDir;
        final String resourcePath = pkg.applicationInfo.publicSourceDir;
        // Update code path if needed
        if (!codePath.equalsIgnoreCase(p.codePathString)) {
            Slog.w(PackageManagerService.TAG, "Code path for pkg : " + p.pkg.packageName +
                    " changing from " + p.codePathString + " to " + codePath);
            p.codePath = new File(codePath);
            p.codePathString = codePath;
        }
        //Update resource path if needed
        if (!resourcePath.equalsIgnoreCase(p.resourcePathString)) {
            Slog.w(PackageManagerService.TAG, "Resource path for pkg : " + p.pkg.packageName +
                    " changing from " + p.resourcePathString + " to " + resourcePath);
            p.resourcePath = new File(resourcePath);
            p.resourcePathString = resourcePath;
        }
        // Update the native library path if needed
        final String nativeLibraryPath = pkg.applicationInfo.nativeLibraryDir;
        if (nativeLibraryPath != null
                && !nativeLibraryPath.equalsIgnoreCase(p.nativeLibraryPathString)) {
            p.nativeLibraryPathString = nativeLibraryPath;
        }
        // Update version code if needed
<<<<<<< HEAD
        if (pkg.mVersionCode != p.versionCode) {
            p.versionCode = pkg.mVersionCode;
        }
        // Update signatures if needed.
        if (p.signatures.mSignatures == null) {
            p.signatures.assignSignatures(pkg.mSignatures);
        }
        // If this app defines a shared user id initialize
        // the shared user signatures as well.
        if (p.sharedUser != null && p.sharedUser.signatures.mSignatures == null) {
            p.sharedUser.signatures.assignSignatures(pkg.mSignatures);
        }
=======
         if (pkg.mVersionCode != p.versionCode) {
            p.versionCode = pkg.mVersionCode;
        }
         // Update signatures if needed.
         if (p.signatures.mSignatures == null) {
             p.signatures.assignSignatures(pkg.mSignatures);
         }
         // If this app defines a shared user id initialize
         // the shared user signatures as well.
         if (p.sharedUser != null && p.sharedUser.signatures.mSignatures == null) {
             p.sharedUser.signatures.assignSignatures(pkg.mSignatures);
         }
>>>>>>> upstream/master
        addPackageSettingLPw(p, pkg.packageName, p.sharedUser);
    }

    // Utility method that adds a PackageSetting to mPackages and
    // completes updating the shared user attributes
    private void addPackageSettingLPw(PackageSetting p, String name,
            SharedUserSetting sharedUser) {
        mPackages.put(name, p);
        if (sharedUser != null) {
            if (p.sharedUser != null && p.sharedUser != sharedUser) {
                PackageManagerService.reportSettingsProblem(Log.ERROR,
                        "Package " + p.name + " was user "
                        + p.sharedUser + " but is now " + sharedUser
                        + "; I am not changing its files so it will probably fail!");
                p.sharedUser.packages.remove(p);
<<<<<<< HEAD
            } else if (p.appId != sharedUser.userId) {
                PackageManagerService.reportSettingsProblem(Log.ERROR,
                    "Package " + p.name + " was user id " + p.appId
=======
            } else if (p.userId != sharedUser.userId) {
                PackageManagerService.reportSettingsProblem(Log.ERROR,
                    "Package " + p.name + " was user id " + p.userId
>>>>>>> upstream/master
                    + " but is now user " + sharedUser
                    + " with id " + sharedUser.userId
                    + "; I am not changing its files so it will probably fail!");
            }

            sharedUser.packages.add(p);
            p.sharedUser = sharedUser;
<<<<<<< HEAD
            p.appId = sharedUser.userId;
=======
            p.userId = sharedUser.userId;
>>>>>>> upstream/master
        }
    }

    /*
     * Update the shared user setting when a package using
     * specifying the shared user id is removed. The gids
     * associated with each permission of the deleted package
     * are removed from the shared user's gid list only if its
     * not in use by other permissions of packages in the
     * shared user setting.
     */
    void updateSharedUserPermsLPw(PackageSetting deletedPs, int[] globalGids) {
        if ((deletedPs == null) || (deletedPs.pkg == null)) {
            Slog.i(PackageManagerService.TAG,
                    "Trying to update info for null package. Just ignoring");
            return;
        }
        // No sharedUserId
        if (deletedPs.sharedUser == null) {
            return;
        }
        SharedUserSetting sus = deletedPs.sharedUser;
        // Update permissions
        for (String eachPerm : deletedPs.pkg.requestedPermissions) {
            boolean used = false;
            if (!sus.grantedPermissions.contains(eachPerm)) {
                continue;
            }
            for (PackageSetting pkg:sus.packages) {
                if (pkg.pkg != null &&
                        !pkg.pkg.packageName.equals(deletedPs.pkg.packageName) &&
                        pkg.pkg.requestedPermissions.contains(eachPerm)) {
                    used = true;
                    break;
                }
            }
            if (!used) {
                // can safely delete this permission from list
                sus.grantedPermissions.remove(eachPerm);
            }
        }
        // Update gids
        int newGids[] = globalGids;
        for (String eachPerm : sus.grantedPermissions) {
            BasePermission bp = mPermissions.get(eachPerm);
            if (bp != null) {
                newGids = PackageManagerService.appendInts(newGids, bp.gids);
            }
        }
        sus.gids = newGids;
    }

    int removePackageLPw(String name) {
        final PackageSetting p = mPackages.get(name);
        if (p != null) {
            mPackages.remove(name);
            if (p.sharedUser != null) {
                p.sharedUser.packages.remove(p);
                if (p.sharedUser.packages.size() == 0) {
                    mSharedUsers.remove(p.sharedUser.name);
                    removeUserIdLPw(p.sharedUser.userId);
                    return p.sharedUser.userId;
                }
            } else {
<<<<<<< HEAD
                removeUserIdLPw(p.appId);
                return p.appId;
=======
                removeUserIdLPw(p.userId);
                return p.userId;
>>>>>>> upstream/master
            }
        }
        return -1;
    }

    private void replacePackageLPw(String name, PackageSetting newp) {
        final PackageSetting p = mPackages.get(name);
        if (p != null) {
            if (p.sharedUser != null) {
                p.sharedUser.packages.remove(p);
                p.sharedUser.packages.add(newp);
            } else {
<<<<<<< HEAD
                replaceUserIdLPw(p.appId, newp);
=======
                replaceUserIdLPw(p.userId, newp);
>>>>>>> upstream/master
            }
        }
        mPackages.put(name, newp);
    }

    private boolean addUserIdLPw(int uid, Object obj, Object name) {
<<<<<<< HEAD
        if (uid > Process.LAST_APPLICATION_UID) {
            return false;
        }

        if (uid >= Process.FIRST_APPLICATION_UID) {
            int N = mUserIds.size();
            final int index = uid - Process.FIRST_APPLICATION_UID;
=======
        if (uid >= PackageManagerService.FIRST_APPLICATION_UID + PackageManagerService.MAX_APPLICATION_UIDS) {
            return false;
        }

        if (uid >= PackageManagerService.FIRST_APPLICATION_UID) {
            int N = mUserIds.size();
            final int index = uid - PackageManagerService.FIRST_APPLICATION_UID;
>>>>>>> upstream/master
            while (index >= N) {
                mUserIds.add(null);
                N++;
            }
            if (mUserIds.get(index) != null) {
                PackageManagerService.reportSettingsProblem(Log.ERROR,
                        "Adding duplicate user id: " + uid
                        + " name=" + name);
                return false;
            }
            mUserIds.set(index, obj);
        } else {
            if (mOtherUserIds.get(uid) != null) {
                PackageManagerService.reportSettingsProblem(Log.ERROR,
                        "Adding duplicate shared id: " + uid
                        + " name=" + name);
                return false;
            }
            mOtherUserIds.put(uid, obj);
        }
        return true;
    }

    public Object getUserIdLPr(int uid) {
<<<<<<< HEAD
        if (uid >= Process.FIRST_APPLICATION_UID) {
            final int N = mUserIds.size();
            final int index = uid - Process.FIRST_APPLICATION_UID;
=======
        if (uid >= PackageManagerService.FIRST_APPLICATION_UID) {
            final int N = mUserIds.size();
            final int index = uid - PackageManagerService.FIRST_APPLICATION_UID;
>>>>>>> upstream/master
            return index < N ? mUserIds.get(index) : null;
        } else {
            return mOtherUserIds.get(uid);
        }
    }

    private void removeUserIdLPw(int uid) {
<<<<<<< HEAD
        if (uid >= Process.FIRST_APPLICATION_UID) {
            final int N = mUserIds.size();
            final int index = uid - Process.FIRST_APPLICATION_UID;
=======
        if (uid >= PackageManagerService.FIRST_APPLICATION_UID) {
            final int N = mUserIds.size();
            final int index = uid - PackageManagerService.FIRST_APPLICATION_UID;
>>>>>>> upstream/master
            if (index < N) mUserIds.set(index, null);
        } else {
            mOtherUserIds.remove(uid);
        }
    }

    private void replaceUserIdLPw(int uid, Object obj) {
<<<<<<< HEAD
        if (uid >= Process.FIRST_APPLICATION_UID) {
            final int N = mUserIds.size();
            final int index = uid - Process.FIRST_APPLICATION_UID;
=======
        if (uid >= PackageManagerService.FIRST_APPLICATION_UID) {
            final int N = mUserIds.size();
            final int index = uid - PackageManagerService.FIRST_APPLICATION_UID;
>>>>>>> upstream/master
            if (index < N) mUserIds.set(index, obj);
        } else {
            mOtherUserIds.put(uid, obj);
        }
    }

<<<<<<< HEAD
    private File getUserPackagesStateFile(int userId) {
        return new File(mSystemDir,
                "users/" + userId + "/package-restrictions.xml");
    }

    private File getUserPackagesStateBackupFile(int userId) {
        return new File(mSystemDir,
                "users/" + userId + "/package-restrictions-backup.xml");
    }

    void writeAllUsersPackageRestrictionsLPr() {
        List<UserInfo> users = getAllUsers();
        if (users == null) return;

        for (UserInfo user : users) {
            writePackageRestrictionsLPr(user.id);
        }
    }

    void readAllUsersPackageRestrictionsLPr() {
        List<UserInfo> users = getAllUsers();
        if (users == null) {
            readPackageRestrictionsLPr(0);
            return;
        }

        for (UserInfo user : users) {
            readPackageRestrictionsLPr(user.id);
        }
    }

    void readPackageRestrictionsLPr(int userId) {
        FileInputStream str = null;
        File userPackagesStateFile = getUserPackagesStateFile(userId);
        File backupFile = getUserPackagesStateBackupFile(userId);
        if (backupFile.exists()) {
            try {
                str = new FileInputStream(backupFile);
                mReadMessages.append("Reading from backup stopped packages file\n");
                PackageManagerService.reportSettingsProblem(Log.INFO,
                        "Need to read from backup stopped packages file");
                if (userPackagesStateFile.exists()) {
                    // If both the backup and normal file exist, we
                    // ignore the normal one since it might have been
                    // corrupted.
                    Slog.w(PackageManagerService.TAG, "Cleaning up stopped packages file "
                            + userPackagesStateFile);
                    userPackagesStateFile.delete();
                }
            } catch (java.io.IOException e) {
                // We'll try for the normal settings file.
            }
        }

        try {
            if (str == null) {
                if (!userPackagesStateFile.exists()) {
                    mReadMessages.append("No stopped packages file found\n");
                    PackageManagerService.reportSettingsProblem(Log.INFO,
                            "No stopped packages file; "
                            + "assuming all started");
                    // At first boot, make sure no packages are stopped.
                    // We usually want to have third party apps initialize
                    // in the stopped state, but not at first boot.
                    for (PackageSetting pkg : mPackages.values()) {
                        pkg.setStopped(false, userId);
                        pkg.setNotLaunched(false, userId);
                    }
                    return;
                }
                str = new FileInputStream(userPackagesStateFile);
            }
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(str, null);

            int type;
            while ((type=parser.next()) != XmlPullParser.START_TAG
                       && type != XmlPullParser.END_DOCUMENT) {
                ;
            }

            if (type != XmlPullParser.START_TAG) {
                mReadMessages.append("No start tag found in package restrictions file\n");
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "No start tag found in package manager stopped packages");
                return;
            }

            int outerDepth = parser.getDepth();
            PackageSetting ps = null;
            while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
                   && (type != XmlPullParser.END_TAG
                           || parser.getDepth() > outerDepth)) {
                if (type == XmlPullParser.END_TAG
                        || type == XmlPullParser.TEXT) {
                    continue;
                }

                String tagName = parser.getName();
                if (tagName.equals(TAG_PACKAGE)) {
                    String name = parser.getAttributeValue(null, ATTR_NAME);
                    ps = mPackages.get(name);
                    if (ps == null) {
                        Slog.w(PackageManagerService.TAG, "No package known for stopped package: "
                                + name);
                        XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                    String enabledStr = parser.getAttributeValue(null, ATTR_ENABLED);
                    int enabled = enabledStr == null ? COMPONENT_ENABLED_STATE_DEFAULT
                            : Integer.parseInt(enabledStr);
                    ps.setEnabled(enabled, userId);
                    String stoppedStr = parser.getAttributeValue(null, ATTR_STOPPED);
                    boolean stopped = stoppedStr == null ? false : Boolean.parseBoolean(stoppedStr);
                    ps.setStopped(stopped, userId);
                    String notLaunchedStr = parser.getAttributeValue(null, ATTR_NOT_LAUNCHED);
                    boolean notLaunched = stoppedStr == null ? false
                            : Boolean.parseBoolean(notLaunchedStr);
                    ps.setNotLaunched(notLaunched, userId);

                    int packageDepth = parser.getDepth();
                    while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
                            && (type != XmlPullParser.END_TAG
                            || parser.getDepth() > packageDepth)) {
                        if (type == XmlPullParser.END_TAG
                                || type == XmlPullParser.TEXT) {
                            continue;
                        }
                        tagName = parser.getName();
                        if (tagName.equals(TAG_ENABLED_COMPONENTS)) {
                            HashSet<String> components = readComponentsLPr(parser);
                            ps.setEnabledComponents(components, userId);
                        } else if (tagName.equals(TAG_DISABLED_COMPONENTS)) {
                            HashSet<String> components = readComponentsLPr(parser);
                            ps.setDisabledComponents(components, userId);
                        }
                    }
                } else {
                    Slog.w(PackageManagerService.TAG, "Unknown element under <stopped-packages>: "
                          + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }

            str.close();

        } catch (XmlPullParserException e) {
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR,
                    "Error reading stopped packages: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager stopped packages", e);

        } catch (java.io.IOException e) {
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR, "Error reading settings: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager stopped packages", e);
        }
    }

    private HashSet<String> readComponentsLPr(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        HashSet<String> components = new HashSet<String>();
        int type;
        int outerDepth = parser.getDepth();
        String tagName;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG
                || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG
                    || type == XmlPullParser.TEXT) {
                continue;
            }
            tagName = parser.getName();
            if (tagName.equals(TAG_ITEM)) {
                String componentName = parser.getAttributeValue(null, ATTR_NAME);
                if (componentName != null) {
                    components.add(componentName);
                }
            }
        }
        return components;
    }

    void writePackageRestrictionsLPr(int userId) {
        // Keep the old stopped packages around until we know the new ones have
        // been successfully written.
        File userPackagesStateFile = getUserPackagesStateFile(userId);
        File backupFile = getUserPackagesStateBackupFile(userId);
        new File(userPackagesStateFile.getParent()).mkdirs();
        if (userPackagesStateFile.exists()) {
=======
    void writeStoppedLPr() {
        // Keep the old stopped packages around until we know the new ones have
        // been successfully written.
        if (mStoppedPackagesFilename.exists()) {
>>>>>>> upstream/master
            // Presence of backup settings file indicates that we failed
            // to persist packages earlier. So preserve the older
            // backup for future reference since the current packages
            // might have been corrupted.
<<<<<<< HEAD
            if (!backupFile.exists()) {
                if (!userPackagesStateFile.renameTo(backupFile)) {
                    Log.wtf(PackageManagerService.TAG, "Unable to backup user packages state file, "
=======
            if (!mBackupStoppedPackagesFilename.exists()) {
                if (!mStoppedPackagesFilename.renameTo(mBackupStoppedPackagesFilename)) {
                    Log.wtf(PackageManagerService.TAG, "Unable to backup package manager stopped packages, "
>>>>>>> upstream/master
                            + "current changes will be lost at reboot");
                    return;
                }
            } else {
<<<<<<< HEAD
                userPackagesStateFile.delete();
=======
                mStoppedPackagesFilename.delete();
>>>>>>> upstream/master
                Slog.w(PackageManagerService.TAG, "Preserving older stopped packages backup");
            }
        }

        try {
<<<<<<< HEAD
            final FileOutputStream fstr = new FileOutputStream(userPackagesStateFile);
            final BufferedOutputStream str = new BufferedOutputStream(fstr);

=======
            final FileOutputStream fstr = new FileOutputStream(mStoppedPackagesFilename);
            final BufferedOutputStream str = new BufferedOutputStream(fstr);

            //XmlSerializer serializer = XmlUtils.serializerInstance();
>>>>>>> upstream/master
            final XmlSerializer serializer = new FastXmlSerializer();
            serializer.setOutput(str, "utf-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

<<<<<<< HEAD
            serializer.startTag(null, TAG_PACKAGE_RESTRICTIONS);

            for (final PackageSetting pkg : mPackages.values()) {
                if (pkg.getStopped(userId)
                        || pkg.getNotLaunched(userId)
                        || pkg.getEnabled(userId) != COMPONENT_ENABLED_STATE_DEFAULT
                        || pkg.getEnabledComponents(userId).size() > 0
                        || pkg.getDisabledComponents(userId).size() > 0) {
                    serializer.startTag(null, TAG_PACKAGE);
                    serializer.attribute(null, ATTR_NAME, pkg.name);
                    boolean stopped = pkg.getStopped(userId);
                    boolean notLaunched = pkg.getNotLaunched(userId);
                    int enabled = pkg.getEnabled(userId);
                    HashSet<String> enabledComponents = pkg.getEnabledComponents(userId);
                    HashSet<String> disabledComponents = pkg.getDisabledComponents(userId);

                    if (stopped) {
                        serializer.attribute(null, ATTR_STOPPED, "true");
                    }
                    if (notLaunched) {
                        serializer.attribute(null, ATTR_NOT_LAUNCHED, "true");
                    }
                    if (enabled != COMPONENT_ENABLED_STATE_DEFAULT) {
                        serializer.attribute(null, ATTR_ENABLED, Integer.toString(enabled));
                    }
                    if (enabledComponents.size() > 0) {
                        serializer.startTag(null, TAG_ENABLED_COMPONENTS);
                        for (final String name : enabledComponents) {
                            serializer.startTag(null, TAG_ITEM);
                            serializer.attribute(null, ATTR_NAME, name);
                            serializer.endTag(null, TAG_ITEM);
                        }
                        serializer.endTag(null, TAG_ENABLED_COMPONENTS);
                    }
                    if (disabledComponents.size() > 0) {
                        serializer.startTag(null, TAG_DISABLED_COMPONENTS);
                        for (final String name : disabledComponents) {
                            serializer.startTag(null, TAG_ITEM);
                            serializer.attribute(null, ATTR_NAME, name);
                            serializer.endTag(null, TAG_ITEM);
                        }
                        serializer.endTag(null, TAG_DISABLED_COMPONENTS);
                    }
                    serializer.endTag(null, TAG_PACKAGE);
                }
            }

            serializer.endTag(null, TAG_PACKAGE_RESTRICTIONS);
=======
            serializer.startTag(null, "stopped-packages");

            for (final PackageSetting pkg : mPackages.values()) {
                if (pkg.stopped) {
                    serializer.startTag(null, "pkg");
                    serializer.attribute(null, "name", pkg.name);
                    if (pkg.notLaunched) {
                        serializer.attribute(null, "nl", "1");
                    }
                    serializer.endTag(null, "pkg");
                }
            }

            serializer.endTag(null, "stopped-packages");
>>>>>>> upstream/master

            serializer.endDocument();

            str.flush();
            FileUtils.sync(fstr);
            str.close();

            // New settings successfully written, old ones are no longer
            // needed.
<<<<<<< HEAD
            backupFile.delete();
            FileUtils.setPermissions(userPackagesStateFile.toString(),
                    FileUtils.S_IRUSR|FileUtils.S_IWUSR
                    |FileUtils.S_IRGRP|FileUtils.S_IWGRP,
=======
            mBackupStoppedPackagesFilename.delete();
            FileUtils.setPermissions(mStoppedPackagesFilename.toString(),
                    FileUtils.S_IRUSR|FileUtils.S_IWUSR
                    |FileUtils.S_IRGRP|FileUtils.S_IWGRP
                    |FileUtils.S_IROTH,
>>>>>>> upstream/master
                    -1, -1);

            // Done, all is good!
            return;
        } catch(java.io.IOException e) {
<<<<<<< HEAD
            Log.wtf(PackageManagerService.TAG,
                    "Unable to write package manager user packages state, "
=======
            Log.wtf(PackageManagerService.TAG, "Unable to write package manager stopped packages, "
>>>>>>> upstream/master
                    + " current changes will be lost at reboot", e);
        }

        // Clean up partially written files
<<<<<<< HEAD
        if (userPackagesStateFile.exists()) {
            if (!userPackagesStateFile.delete()) {
                Log.i(PackageManagerService.TAG, "Failed to clean up mangled file: "
                        + mStoppedPackagesFilename);
=======
        if (mStoppedPackagesFilename.exists()) {
            if (!mStoppedPackagesFilename.delete()) {
                Log.i(PackageManagerService.TAG, "Failed to clean up mangled file: " + mStoppedPackagesFilename);
>>>>>>> upstream/master
            }
        }
    }

    // Note: assumed "stopped" field is already cleared in all packages.
<<<<<<< HEAD
    // Legacy reader, used to read in the old file format after an upgrade. Not used after that.
=======
>>>>>>> upstream/master
    void readStoppedLPw() {
        FileInputStream str = null;
        if (mBackupStoppedPackagesFilename.exists()) {
            try {
                str = new FileInputStream(mBackupStoppedPackagesFilename);
                mReadMessages.append("Reading from backup stopped packages file\n");
<<<<<<< HEAD
                PackageManagerService.reportSettingsProblem(Log.INFO,
                        "Need to read from backup stopped packages file");
=======
                PackageManagerService.reportSettingsProblem(Log.INFO, "Need to read from backup stopped packages file");
>>>>>>> upstream/master
                if (mSettingsFilename.exists()) {
                    // If both the backup and normal file exist, we
                    // ignore the normal one since it might have been
                    // corrupted.
                    Slog.w(PackageManagerService.TAG, "Cleaning up stopped packages file "
                            + mStoppedPackagesFilename);
                    mStoppedPackagesFilename.delete();
                }
            } catch (java.io.IOException e) {
                // We'll try for the normal settings file.
            }
        }

        try {
            if (str == null) {
                if (!mStoppedPackagesFilename.exists()) {
                    mReadMessages.append("No stopped packages file found\n");
<<<<<<< HEAD
                    PackageManagerService.reportSettingsProblem(Log.INFO,
                            "No stopped packages file file; assuming all started");
=======
                    PackageManagerService.reportSettingsProblem(Log.INFO, "No stopped packages file file; "
                            + "assuming all started");
>>>>>>> upstream/master
                    // At first boot, make sure no packages are stopped.
                    // We usually want to have third party apps initialize
                    // in the stopped state, but not at first boot.
                    for (PackageSetting pkg : mPackages.values()) {
<<<<<<< HEAD
                        pkg.setStopped(false, 0);
                        pkg.setNotLaunched(false, 0);
=======
                        pkg.stopped = false;
                        pkg.notLaunched = false;
>>>>>>> upstream/master
                    }
                    return;
                }
                str = new FileInputStream(mStoppedPackagesFilename);
            }
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(str, null);

            int type;
            while ((type=parser.next()) != XmlPullParser.START_TAG
                       && type != XmlPullParser.END_DOCUMENT) {
                ;
            }

            if (type != XmlPullParser.START_TAG) {
                mReadMessages.append("No start tag found in stopped packages file\n");
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "No start tag found in package manager stopped packages");
                return;
            }

            int outerDepth = parser.getDepth();
            while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
                   && (type != XmlPullParser.END_TAG
                           || parser.getDepth() > outerDepth)) {
                if (type == XmlPullParser.END_TAG
                        || type == XmlPullParser.TEXT) {
                    continue;
                }

                String tagName = parser.getName();
<<<<<<< HEAD
                if (tagName.equals(TAG_PACKAGE)) {
                    String name = parser.getAttributeValue(null, ATTR_NAME);
                    PackageSetting ps = mPackages.get(name);
                    if (ps != null) {
                        ps.setStopped(true, 0);
                        if ("1".equals(parser.getAttributeValue(null, ATTR_NOT_LAUNCHED))) {
                            ps.setNotLaunched(true, 0);
                        }
                    } else {
                        Slog.w(PackageManagerService.TAG,
                                "No package known for stopped package: " + name);
=======
                if (tagName.equals("pkg")) {
                    String name = parser.getAttributeValue(null, "name");
                    PackageSetting ps = mPackages.get(name);
                    if (ps != null) {
                        ps.stopped = true;
                        if ("1".equals(parser.getAttributeValue(null, "nl"))) {
                            ps.notLaunched = true;
                        }
                    } else {
                        Slog.w(PackageManagerService.TAG, "No package known for stopped package: " + name);
>>>>>>> upstream/master
                    }
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    Slog.w(PackageManagerService.TAG, "Unknown element under <stopped-packages>: "
                          + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }

            str.close();

<<<<<<< HEAD
        } catch (XmlPullParserException e) {
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR,
                    "Error reading stopped packages: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager stopped packages", e);

        } catch (java.io.IOException e) {
=======
        } catch(XmlPullParserException e) {
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR, "Error reading stopped packages: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager stopped packages", e);

        } catch(java.io.IOException e) {
>>>>>>> upstream/master
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR, "Error reading settings: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager stopped packages", e);

        }
    }

    void writeLPr() {
        //Debug.startMethodTracing("/data/system/packageprof", 8 * 1024 * 1024);

        // Keep the old settings around until we know the new ones have
        // been successfully written.
        if (mSettingsFilename.exists()) {
            // Presence of backup settings file indicates that we failed
            // to persist settings earlier. So preserve the older
            // backup for future reference since the current settings
            // might have been corrupted.
            if (!mBackupSettingsFilename.exists()) {
                if (!mSettingsFilename.renameTo(mBackupSettingsFilename)) {
                    Log.wtf(PackageManagerService.TAG, "Unable to backup package manager settings, "
                            + " current changes will be lost at reboot");
                    return;
                }
            } else {
                mSettingsFilename.delete();
                Slog.w(PackageManagerService.TAG, "Preserving older settings backup");
            }
        }

        mPastSignatures.clear();

        try {
            FileOutputStream fstr = new FileOutputStream(mSettingsFilename);
            BufferedOutputStream str = new BufferedOutputStream(fstr);

            //XmlSerializer serializer = XmlUtils.serializerInstance();
            XmlSerializer serializer = new FastXmlSerializer();
            serializer.setOutput(str, "utf-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "packages");

            serializer.startTag(null, "last-platform-version");
            serializer.attribute(null, "internal", Integer.toString(mInternalSdkPlatform));
            serializer.attribute(null, "external", Integer.toString(mExternalSdkPlatform));
            serializer.endTag(null, "last-platform-version");
<<<<<<< HEAD

=======
            
>>>>>>> upstream/master
            if (mVerifierDeviceIdentity != null) {
                serializer.startTag(null, "verifier");
                serializer.attribute(null, "device", mVerifierDeviceIdentity.toString());
                serializer.endTag(null, "verifier");
            }

<<<<<<< HEAD
            if (mReadExternalStorageEnforced != null) {
                serializer.startTag(null, TAG_READ_EXTERNAL_STORAGE);
                serializer.attribute(
                        null, ATTR_ENFORCEMENT, mReadExternalStorageEnforced ? "1" : "0");
                serializer.endTag(null, TAG_READ_EXTERNAL_STORAGE);
            }

=======
>>>>>>> upstream/master
            serializer.startTag(null, "permission-trees");
            for (BasePermission bp : mPermissionTrees.values()) {
                writePermissionLPr(serializer, bp);
            }
            serializer.endTag(null, "permission-trees");

            serializer.startTag(null, "permissions");
            for (BasePermission bp : mPermissions.values()) {
                writePermissionLPr(serializer, bp);
            }
            serializer.endTag(null, "permissions");

            for (final PackageSetting pkg : mPackages.values()) {
                writePackageLPr(serializer, pkg);
            }

            for (final PackageSetting pkg : mDisabledSysPackages.values()) {
                writeDisabledSysPackageLPr(serializer, pkg);
            }

<<<<<<< HEAD
            writePreferredActivitiesLPr(serializer);

            for (final SharedUserSetting usr : mSharedUsers.values()) {
                serializer.startTag(null, "shared-user");
                serializer.attribute(null, ATTR_NAME, usr.name);
=======
            serializer.startTag(null, "preferred-activities");
            for (final PreferredActivity pa : mPreferredActivities.filterSet()) {
                serializer.startTag(null, "item");
                pa.writeToXml(serializer);
                serializer.endTag(null, "item");
            }
            serializer.endTag(null, "preferred-activities");

            for (final SharedUserSetting usr : mSharedUsers.values()) {
                serializer.startTag(null, "shared-user");
                serializer.attribute(null, "name", usr.name);
>>>>>>> upstream/master
                serializer.attribute(null, "userId",
                        Integer.toString(usr.userId));
                usr.signatures.writeXml(serializer, "sigs", mPastSignatures);
                serializer.startTag(null, "perms");
                for (String name : usr.grantedPermissions) {
<<<<<<< HEAD
                    serializer.startTag(null, TAG_ITEM);
                    serializer.attribute(null, ATTR_NAME, name);
                    serializer.endTag(null, TAG_ITEM);
=======
                    serializer.startTag(null, "item");
                    serializer.attribute(null, "name", name);
                    serializer.endTag(null, "item");
>>>>>>> upstream/master
                }
                serializer.endTag(null, "perms");
                serializer.endTag(null, "shared-user");
            }

            if (mPackagesToBeCleaned.size() > 0) {
                for (int i=0; i<mPackagesToBeCleaned.size(); i++) {
                    serializer.startTag(null, "cleaning-package");
<<<<<<< HEAD
                    serializer.attribute(null, ATTR_NAME, mPackagesToBeCleaned.get(i));
=======
                    serializer.attribute(null, "name", mPackagesToBeCleaned.get(i));
>>>>>>> upstream/master
                    serializer.endTag(null, "cleaning-package");
                }
            }
            
            if (mRenamedPackages.size() > 0) {
<<<<<<< HEAD
                for (Map.Entry<String, String> e : mRenamedPackages.entrySet()) {
=======
                for (HashMap.Entry<String, String> e : mRenamedPackages.entrySet()) {
>>>>>>> upstream/master
                    serializer.startTag(null, "renamed-package");
                    serializer.attribute(null, "new", e.getKey());
                    serializer.attribute(null, "old", e.getValue());
                    serializer.endTag(null, "renamed-package");
                }
            }
            
            serializer.endTag(null, "packages");

            serializer.endDocument();

            str.flush();
            FileUtils.sync(fstr);
            str.close();

            // New settings successfully written, old ones are no longer
            // needed.
            mBackupSettingsFilename.delete();
            FileUtils.setPermissions(mSettingsFilename.toString(),
                    FileUtils.S_IRUSR|FileUtils.S_IWUSR
<<<<<<< HEAD
                    |FileUtils.S_IRGRP|FileUtils.S_IWGRP,
=======
                    |FileUtils.S_IRGRP|FileUtils.S_IWGRP
                    |FileUtils.S_IROTH,
>>>>>>> upstream/master
                    -1, -1);

            // Write package list file now, use a JournaledFile.
            //
            File tempFile = new File(mPackageListFilename.toString() + ".tmp");
            JournaledFile journal = new JournaledFile(mPackageListFilename, tempFile);

            fstr = new FileOutputStream(journal.chooseForWrite());
            str = new BufferedOutputStream(fstr);
            try {
                StringBuilder sb = new StringBuilder();
                for (final PackageSetting pkg : mPackages.values()) {
                    ApplicationInfo ai = pkg.pkg.applicationInfo;
                    String dataPath = ai.dataDir;
                    boolean isDebug  = (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

                    // Avoid any application that has a space in its path
                    // or that is handled by the system.
                    if (dataPath.indexOf(" ") >= 0 || ai.uid <= Process.FIRST_APPLICATION_UID)
                        continue;

                    // we store on each line the following information for now:
                    //
                    // pkgName    - package name
                    // userId     - application-specific user id
                    // debugFlag  - 0 or 1 if the package is debuggable.
                    // dataPath   - path to package's data path
                    //
                    // NOTE: We prefer not to expose all ApplicationInfo flags for now.
                    //
                    // DO NOT MODIFY THIS FORMAT UNLESS YOU CAN ALSO MODIFY ITS USERS
                    // FROM NATIVE CODE. AT THE MOMENT, LOOK AT THE FOLLOWING SOURCES:
                    //   system/core/run-as/run-as.c
                    //
                    sb.setLength(0);
                    sb.append(ai.packageName);
                    sb.append(" ");
                    sb.append((int)ai.uid);
                    sb.append(isDebug ? " 1 " : " 0 ");
                    sb.append(dataPath);
                    sb.append("\n");
                    str.write(sb.toString().getBytes());
                }
                str.flush();
                FileUtils.sync(fstr);
                str.close();
                journal.commit();
            } catch (Exception e) {
                IoUtils.closeQuietly(str);
                journal.rollback();
            }

            FileUtils.setPermissions(mPackageListFilename.toString(),
                    FileUtils.S_IRUSR|FileUtils.S_IWUSR
<<<<<<< HEAD
                    |FileUtils.S_IRGRP|FileUtils.S_IWGRP,
                    -1, -1);

            writeAllUsersPackageRestrictionsLPr();
=======
                    |FileUtils.S_IRGRP|FileUtils.S_IWGRP
                    |FileUtils.S_IROTH,
                    -1, -1);

            writeStoppedLPr();

>>>>>>> upstream/master
            return;

        } catch(XmlPullParserException e) {
            Log.wtf(PackageManagerService.TAG, "Unable to write package manager settings, "
                    + "current changes will be lost at reboot", e);
        } catch(java.io.IOException e) {
            Log.wtf(PackageManagerService.TAG, "Unable to write package manager settings, "
                    + "current changes will be lost at reboot", e);
        }
        // Clean up partially written files
        if (mSettingsFilename.exists()) {
            if (!mSettingsFilename.delete()) {
<<<<<<< HEAD
                Log.wtf(PackageManagerService.TAG, "Failed to clean up mangled file: "
                        + mSettingsFilename);
=======
                Log.wtf(PackageManagerService.TAG, "Failed to clean up mangled file: " + mSettingsFilename);
>>>>>>> upstream/master
            }
        }
        //Debug.stopMethodTracing();
    }

<<<<<<< HEAD
    void writePreferredActivitiesLPr(XmlSerializer serializer)
            throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag(null, "preferred-activities");
        for (final PreferredActivity pa : mPreferredActivities.filterSet()) {
            serializer.startTag(null, TAG_ITEM);
            pa.writeToXml(serializer);
            serializer.endTag(null, TAG_ITEM);
        }
        serializer.endTag(null, "preferred-activities");
    }

    void writeDisabledSysPackageLPr(XmlSerializer serializer, final PackageSetting pkg)
            throws java.io.IOException {
        serializer.startTag(null, "updated-package");
        serializer.attribute(null, ATTR_NAME, pkg.name);
=======
    void writeDisabledSysPackageLPr(XmlSerializer serializer, final PackageSetting pkg)
            throws java.io.IOException {
        serializer.startTag(null, "updated-package");
        serializer.attribute(null, "name", pkg.name);
>>>>>>> upstream/master
        if (pkg.realName != null) {
            serializer.attribute(null, "realName", pkg.realName);
        }
        serializer.attribute(null, "codePath", pkg.codePathString);
        serializer.attribute(null, "ft", Long.toHexString(pkg.timeStamp));
        serializer.attribute(null, "it", Long.toHexString(pkg.firstInstallTime));
        serializer.attribute(null, "ut", Long.toHexString(pkg.lastUpdateTime));
        serializer.attribute(null, "version", String.valueOf(pkg.versionCode));
        if (!pkg.resourcePathString.equals(pkg.codePathString)) {
            serializer.attribute(null, "resourcePath", pkg.resourcePathString);
        }
        if (pkg.nativeLibraryPathString != null) {
            serializer.attribute(null, "nativeLibraryPath", pkg.nativeLibraryPathString);
        }
        if (pkg.sharedUser == null) {
<<<<<<< HEAD
            serializer.attribute(null, "userId", Integer.toString(pkg.appId));
        } else {
            serializer.attribute(null, "sharedUserId", Integer.toString(pkg.appId));
=======
            serializer.attribute(null, "userId", Integer.toString(pkg.userId));
        } else {
            serializer.attribute(null, "sharedUserId", Integer.toString(pkg.userId));
>>>>>>> upstream/master
        }
        serializer.startTag(null, "perms");
        if (pkg.sharedUser == null) {
            // If this is a shared user, the permissions will
            // be written there. We still need to write an
            // empty permissions list so permissionsFixed will
            // be set.
            for (final String name : pkg.grantedPermissions) {
                BasePermission bp = mPermissions.get(name);
                if (bp != null) {
                    // We only need to write signature or system permissions but
                    // this wont
                    // match the semantics of grantedPermissions. So write all
                    // permissions.
<<<<<<< HEAD
                    serializer.startTag(null, TAG_ITEM);
                    serializer.attribute(null, ATTR_NAME, name);
                    serializer.endTag(null, TAG_ITEM);
=======
                    serializer.startTag(null, "item");
                    serializer.attribute(null, "name", name);
                    serializer.endTag(null, "item");
>>>>>>> upstream/master
                }
            }
        }
        serializer.endTag(null, "perms");
        serializer.endTag(null, "updated-package");
    }

    void writePackageLPr(XmlSerializer serializer, final PackageSetting pkg)
            throws java.io.IOException {
        serializer.startTag(null, "package");
<<<<<<< HEAD
        serializer.attribute(null, ATTR_NAME, pkg.name);
=======
        serializer.attribute(null, "name", pkg.name);
>>>>>>> upstream/master
        if (pkg.realName != null) {
            serializer.attribute(null, "realName", pkg.realName);
        }
        serializer.attribute(null, "codePath", pkg.codePathString);
        if (!pkg.resourcePathString.equals(pkg.codePathString)) {
            serializer.attribute(null, "resourcePath", pkg.resourcePathString);
        }
        if (pkg.nativeLibraryPathString != null) {
            serializer.attribute(null, "nativeLibraryPath", pkg.nativeLibraryPathString);
        }
        serializer.attribute(null, "flags", Integer.toString(pkg.pkgFlags));
        serializer.attribute(null, "ft", Long.toHexString(pkg.timeStamp));
        serializer.attribute(null, "it", Long.toHexString(pkg.firstInstallTime));
        serializer.attribute(null, "ut", Long.toHexString(pkg.lastUpdateTime));
        serializer.attribute(null, "version", String.valueOf(pkg.versionCode));
        if (pkg.sharedUser == null) {
<<<<<<< HEAD
            serializer.attribute(null, "userId", Integer.toString(pkg.appId));
        } else {
            serializer.attribute(null, "sharedUserId", Integer.toString(pkg.appId));
=======
            serializer.attribute(null, "userId", Integer.toString(pkg.userId));
        } else {
            serializer.attribute(null, "sharedUserId", Integer.toString(pkg.userId));
>>>>>>> upstream/master
        }
        if (pkg.uidError) {
            serializer.attribute(null, "uidError", "true");
        }
<<<<<<< HEAD
=======
        if (pkg.enabled != COMPONENT_ENABLED_STATE_DEFAULT) {
            serializer.attribute(null, "enabled", Integer.toString(pkg.enabled));
        }
>>>>>>> upstream/master
        if (pkg.installStatus == PackageSettingBase.PKG_INSTALL_INCOMPLETE) {
            serializer.attribute(null, "installStatus", "false");
        }
        if (pkg.installerPackageName != null) {
            serializer.attribute(null, "installer", pkg.installerPackageName);
        }
        pkg.signatures.writeXml(serializer, "sigs", mPastSignatures);
        if ((pkg.pkgFlags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            serializer.startTag(null, "perms");
            if (pkg.sharedUser == null) {
                // If this is a shared user, the permissions will
                // be written there. We still need to write an
                // empty permissions list so permissionsFixed will
                // be set.
                for (final String name : pkg.grantedPermissions) {
<<<<<<< HEAD
                    serializer.startTag(null, TAG_ITEM);
                    serializer.attribute(null, ATTR_NAME, name);
                    serializer.endTag(null, TAG_ITEM);
=======
                    serializer.startTag(null, "item");
                    serializer.attribute(null, "name", name);
                    serializer.endTag(null, "item");
>>>>>>> upstream/master
                }
            }
            serializer.endTag(null, "perms");
        }
<<<<<<< HEAD
=======
        if (pkg.disabledComponents.size() > 0) {
            serializer.startTag(null, "disabled-components");
            for (final String name : pkg.disabledComponents) {
                serializer.startTag(null, "item");
                serializer.attribute(null, "name", name);
                serializer.endTag(null, "item");
            }
            serializer.endTag(null, "disabled-components");
        }
        if (pkg.enabledComponents.size() > 0) {
            serializer.startTag(null, "enabled-components");
            for (final String name : pkg.enabledComponents) {
                serializer.startTag(null, "item");
                serializer.attribute(null, "name", name);
                serializer.endTag(null, "item");
            }
            serializer.endTag(null, "enabled-components");
        }
>>>>>>> upstream/master

        serializer.endTag(null, "package");
    }

    void writePermissionLPr(XmlSerializer serializer, BasePermission bp)
            throws XmlPullParserException, java.io.IOException {
        if (bp.type != BasePermission.TYPE_BUILTIN && bp.sourcePackage != null) {
<<<<<<< HEAD
            serializer.startTag(null, TAG_ITEM);
            serializer.attribute(null, ATTR_NAME, bp.name);
=======
            serializer.startTag(null, "item");
            serializer.attribute(null, "name", bp.name);
>>>>>>> upstream/master
            serializer.attribute(null, "package", bp.sourcePackage);
            if (bp.protectionLevel != PermissionInfo.PROTECTION_NORMAL) {
                serializer.attribute(null, "protection", Integer.toString(bp.protectionLevel));
            }
            if (PackageManagerService.DEBUG_SETTINGS)
                Log.v(PackageManagerService.TAG, "Writing perm: name=" + bp.name + " type="
                        + bp.type);
            if (bp.type == BasePermission.TYPE_DYNAMIC) {
                final PermissionInfo pi = bp.perm != null ? bp.perm.info : bp.pendingInfo;
                if (pi != null) {
                    serializer.attribute(null, "type", "dynamic");
                    if (pi.icon != 0) {
                        serializer.attribute(null, "icon", Integer.toString(pi.icon));
                    }
                    if (pi.nonLocalizedLabel != null) {
                        serializer.attribute(null, "label", pi.nonLocalizedLabel.toString());
                    }
                }
            }
<<<<<<< HEAD
            serializer.endTag(null, TAG_ITEM);
=======
            serializer.endTag(null, "item");
>>>>>>> upstream/master
        }
    }

    ArrayList<PackageSetting> getListOfIncompleteInstallPackagesLPr() {
        final HashSet<String> kList = new HashSet<String>(mPackages.keySet());
        final Iterator<String> its = kList.iterator();
        final ArrayList<PackageSetting> ret = new ArrayList<PackageSetting>();
        while (its.hasNext()) {
            final String key = its.next();
            final PackageSetting ps = mPackages.get(key);
            if (ps.getInstallStatus() == PackageSettingBase.PKG_INSTALL_INCOMPLETE) {
                ret.add(ps);
            }
        }
        return ret;
    }

<<<<<<< HEAD
    boolean readLPw(List<UserInfo> users) {
=======
    boolean readLPw() {
>>>>>>> upstream/master
        FileInputStream str = null;
        if (mBackupSettingsFilename.exists()) {
            try {
                str = new FileInputStream(mBackupSettingsFilename);
                mReadMessages.append("Reading from backup settings file\n");
                PackageManagerService.reportSettingsProblem(Log.INFO,
                        "Need to read from backup settings file");
                if (mSettingsFilename.exists()) {
                    // If both the backup and settings file exist, we
                    // ignore the settings since it might have been
                    // corrupted.
                    Slog.w(PackageManagerService.TAG, "Cleaning up settings file "
                            + mSettingsFilename);
                    mSettingsFilename.delete();
                }
            } catch (java.io.IOException e) {
                // We'll try for the normal settings file.
            }
        }

        mPendingPackages.clear();
        mPastSignatures.clear();

        try {
            if (str == null) {
                if (!mSettingsFilename.exists()) {
                    mReadMessages.append("No settings file found\n");
                    PackageManagerService.reportSettingsProblem(Log.INFO,
                            "No settings file; creating initial state");
<<<<<<< HEAD
                    readDefaultPreferredAppsLPw();
=======
>>>>>>> upstream/master
                    return false;
                }
                str = new FileInputStream(mSettingsFilename);
            }
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(str, null);

            int type;
            while ((type = parser.next()) != XmlPullParser.START_TAG
                    && type != XmlPullParser.END_DOCUMENT) {
                ;
            }

            if (type != XmlPullParser.START_TAG) {
                mReadMessages.append("No start tag found in settings file\n");
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "No start tag found in package manager settings");
                Log.wtf(PackageManagerService.TAG,
                        "No start tag found in package manager settings");
                return false;
            }

            int outerDepth = parser.getDepth();
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                    && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
                if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                    continue;
                }

                String tagName = parser.getName();
                if (tagName.equals("package")) {
                    readPackageLPw(parser);
                } else if (tagName.equals("permissions")) {
                    readPermissionsLPw(mPermissions, parser);
                } else if (tagName.equals("permission-trees")) {
                    readPermissionsLPw(mPermissionTrees, parser);
                } else if (tagName.equals("shared-user")) {
                    readSharedUserLPw(parser);
                } else if (tagName.equals("preferred-packages")) {
                    // no longer used.
                } else if (tagName.equals("preferred-activities")) {
                    readPreferredActivitiesLPw(parser);
                } else if (tagName.equals("updated-package")) {
                    readDisabledSysPackageLPw(parser);
                } else if (tagName.equals("cleaning-package")) {
<<<<<<< HEAD
                    String name = parser.getAttributeValue(null, ATTR_NAME);
=======
                    String name = parser.getAttributeValue(null, "name");
>>>>>>> upstream/master
                    if (name != null) {
                        mPackagesToBeCleaned.add(name);
                    }
                } else if (tagName.equals("renamed-package")) {
                    String nname = parser.getAttributeValue(null, "new");
                    String oname = parser.getAttributeValue(null, "old");
                    if (nname != null && oname != null) {
                        mRenamedPackages.put(nname, oname);
                    }
                } else if (tagName.equals("last-platform-version")) {
                    mInternalSdkPlatform = mExternalSdkPlatform = 0;
                    try {
                        String internal = parser.getAttributeValue(null, "internal");
                        if (internal != null) {
                            mInternalSdkPlatform = Integer.parseInt(internal);
                        }
                        String external = parser.getAttributeValue(null, "external");
                        if (external != null) {
                            mExternalSdkPlatform = Integer.parseInt(external);
                        }
                    } catch (NumberFormatException e) {
                    }
                } else if (tagName.equals("verifier")) {
                    final String deviceIdentity = parser.getAttributeValue(null, "device");
                    try {
                        mVerifierDeviceIdentity = VerifierDeviceIdentity.parse(deviceIdentity);
                    } catch (IllegalArgumentException e) {
                        Slog.w(PackageManagerService.TAG, "Discard invalid verifier device id: "
                                + e.getMessage());
                    }
<<<<<<< HEAD
                } else if (TAG_READ_EXTERNAL_STORAGE.equals(tagName)) {
                    final String enforcement = parser.getAttributeValue(null, ATTR_ENFORCEMENT);
                    mReadExternalStorageEnforced = "1".equals(enforcement);
=======
>>>>>>> upstream/master
                } else {
                    Slog.w(PackageManagerService.TAG, "Unknown element under <packages>: "
                            + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }

            str.close();

        } catch (XmlPullParserException e) {
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR, "Error reading settings: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager settings", e);

        } catch (java.io.IOException e) {
            mReadMessages.append("Error reading: " + e.toString());
            PackageManagerService.reportSettingsProblem(Log.ERROR, "Error reading settings: " + e);
            Log.wtf(PackageManagerService.TAG, "Error reading package manager settings", e);

        }

        final int N = mPendingPackages.size();
        for (int i = 0; i < N; i++) {
            final PendingPackage pp = mPendingPackages.get(i);
            Object idObj = getUserIdLPr(pp.sharedId);
            if (idObj != null && idObj instanceof SharedUserSetting) {
                PackageSetting p = getPackageLPw(pp.name, null, pp.realName,
                        (SharedUserSetting) idObj, pp.codePath, pp.resourcePath,
                        pp.nativeLibraryPathString, pp.versionCode, pp.pkgFlags, true, true);
                if (p == null) {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Unable to create application package for " + pp.name);
                    continue;
                }
                p.copyFrom(pp);
            } else if (idObj != null) {
                String msg = "Bad package setting: package " + pp.name + " has shared uid "
                        + pp.sharedId + " that is not a shared uid\n";
                mReadMessages.append(msg);
                PackageManagerService.reportSettingsProblem(Log.ERROR, msg);
            } else {
                String msg = "Bad package setting: package " + pp.name + " has shared uid "
                        + pp.sharedId + " that is not defined\n";
                mReadMessages.append(msg);
                PackageManagerService.reportSettingsProblem(Log.ERROR, msg);
            }
        }
        mPendingPackages.clear();

        /*
         * Make sure all the updated system packages have their shared users
         * associated with them.
         */
        final Iterator<PackageSetting> disabledIt = mDisabledSysPackages.values().iterator();
        while (disabledIt.hasNext()) {
            final PackageSetting disabledPs = disabledIt.next();
<<<<<<< HEAD
            final Object id = getUserIdLPr(disabledPs.appId);
=======
            final Object id = getUserIdLPr(disabledPs.userId);
>>>>>>> upstream/master
            if (id != null && id instanceof SharedUserSetting) {
                disabledPs.sharedUser = (SharedUserSetting) id;
            }
        }

<<<<<<< HEAD
        if (mBackupStoppedPackagesFilename.exists()
                || mStoppedPackagesFilename.exists()) {
            // Read old file
            readStoppedLPw();
            mBackupStoppedPackagesFilename.delete();
            mStoppedPackagesFilename.delete();
            // Migrate to new file format
            writePackageRestrictionsLPr(0);
        } else {
            if (users == null) {
                readPackageRestrictionsLPr(0);
            } else {
                for (UserInfo user : users) {
                    readPackageRestrictionsLPr(user.id);
                }
            }
        }
=======
        readStoppedLPw();

>>>>>>> upstream/master
        mReadMessages.append("Read completed successfully: " + mPackages.size() + " packages, "
                + mSharedUsers.size() + " shared uids\n");

        return true;
    }

<<<<<<< HEAD
    private void readDefaultPreferredAppsLPw() {
        // Read preferred apps from .../etc/preferred-apps directory.
        File preferredDir = new File(Environment.getRootDirectory(), "etc/preferred-apps");
        if (!preferredDir.exists() || !preferredDir.isDirectory()) {
            return;
        }
        if (!preferredDir.canRead()) {
            Slog.w(TAG, "Directory " + preferredDir + " cannot be read");
            return;
        }

        // Iterate over the files in the directory and scan .xml files
        for (File f : preferredDir.listFiles()) {
            if (!f.getPath().endsWith(".xml")) {
                Slog.i(TAG, "Non-xml file " + f + " in " + preferredDir + " directory, ignoring");
                continue;
            }
            if (!f.canRead()) {
                Slog.w(TAG, "Preferred apps file " + f + " cannot be read");
                continue;
            }

            FileInputStream str = null;
            try {
                str = new FileInputStream(f);
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(str, null);

                int type;
                while ((type = parser.next()) != XmlPullParser.START_TAG
                        && type != XmlPullParser.END_DOCUMENT) {
                    ;
                }

                if (type != XmlPullParser.START_TAG) {
                    Slog.w(TAG, "Preferred apps file " + f + " does not have start tag");
                    continue;
                }
                if (!"preferred-activities".equals(parser.getName())) {
                    Slog.w(TAG, "Preferred apps file " + f
                            + " does not start with 'preferred-activities'");
                    continue;
                }
                readPreferredActivitiesLPw(parser);
            } catch (XmlPullParserException e) {
                Slog.w(TAG, "Error reading apps file " + f, e);
            } catch (IOException e) {
                Slog.w(TAG, "Error reading apps file " + f, e);
            } finally {
                if (str != null) {
                    try {
                        str.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

=======
>>>>>>> upstream/master
    private int readInt(XmlPullParser parser, String ns, String name, int defValue) {
        String v = parser.getAttributeValue(ns, name);
        try {
            if (v == null) {
                return defValue;
            }
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            PackageManagerService.reportSettingsProblem(Log.WARN,
                    "Error in package manager settings: attribute " + name
                            + " has bad integer value " + v + " at "
                            + parser.getPositionDescription());
        }
        return defValue;
    }

    private void readPermissionsLPw(HashMap<String, BasePermission> out, XmlPullParser parser)
            throws IOException, XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            final String tagName = parser.getName();
<<<<<<< HEAD
            if (tagName.equals(TAG_ITEM)) {
                final String name = parser.getAttributeValue(null, ATTR_NAME);
=======
            if (tagName.equals("item")) {
                final String name = parser.getAttributeValue(null, "name");
>>>>>>> upstream/master
                final String sourcePackage = parser.getAttributeValue(null, "package");
                final String ptype = parser.getAttributeValue(null, "type");
                if (name != null && sourcePackage != null) {
                    final boolean dynamic = "dynamic".equals(ptype);
                    final BasePermission bp = new BasePermission(name, sourcePackage,
                            dynamic ? BasePermission.TYPE_DYNAMIC : BasePermission.TYPE_NORMAL);
                    bp.protectionLevel = readInt(parser, null, "protection",
                            PermissionInfo.PROTECTION_NORMAL);
<<<<<<< HEAD
                    bp.protectionLevel = PermissionInfo.fixProtectionLevel(bp.protectionLevel);
=======
>>>>>>> upstream/master
                    if (dynamic) {
                        PermissionInfo pi = new PermissionInfo();
                        pi.packageName = sourcePackage.intern();
                        pi.name = name.intern();
                        pi.icon = readInt(parser, null, "icon", 0);
                        pi.nonLocalizedLabel = parser.getAttributeValue(null, "label");
                        pi.protectionLevel = bp.protectionLevel;
                        bp.pendingInfo = pi;
                    }
                    out.put(bp.name, bp);
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Error in package manager settings: permissions has" + " no name at "
                                    + parser.getPositionDescription());
                }
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Unknown element reading permissions: " + parser.getName() + " at "
                                + parser.getPositionDescription());
            }
            XmlUtils.skipCurrentTag(parser);
        }
    }

    private void readDisabledSysPackageLPw(XmlPullParser parser) throws XmlPullParserException,
            IOException {
<<<<<<< HEAD
        String name = parser.getAttributeValue(null, ATTR_NAME);
=======
        String name = parser.getAttributeValue(null, "name");
>>>>>>> upstream/master
        String realName = parser.getAttributeValue(null, "realName");
        String codePathStr = parser.getAttributeValue(null, "codePath");
        String resourcePathStr = parser.getAttributeValue(null, "resourcePath");
        String nativeLibraryPathStr = parser.getAttributeValue(null, "nativeLibraryPath");
        if (resourcePathStr == null) {
            resourcePathStr = codePathStr;
        }
        String version = parser.getAttributeValue(null, "version");
        int versionCode = 0;
        if (version != null) {
            try {
                versionCode = Integer.parseInt(version);
            } catch (NumberFormatException e) {
            }
        }

        int pkgFlags = 0;
        pkgFlags |= ApplicationInfo.FLAG_SYSTEM;
        PackageSetting ps = new PackageSetting(name, realName, new File(codePathStr),
                new File(resourcePathStr), nativeLibraryPathStr, versionCode, pkgFlags);
        String timeStampStr = parser.getAttributeValue(null, "ft");
        if (timeStampStr != null) {
            try {
                long timeStamp = Long.parseLong(timeStampStr, 16);
                ps.setTimeStamp(timeStamp);
            } catch (NumberFormatException e) {
            }
        } else {
            timeStampStr = parser.getAttributeValue(null, "ts");
            if (timeStampStr != null) {
                try {
                    long timeStamp = Long.parseLong(timeStampStr);
                    ps.setTimeStamp(timeStamp);
                } catch (NumberFormatException e) {
                }
            }
        }
        timeStampStr = parser.getAttributeValue(null, "it");
        if (timeStampStr != null) {
            try {
                ps.firstInstallTime = Long.parseLong(timeStampStr, 16);
            } catch (NumberFormatException e) {
            }
        }
        timeStampStr = parser.getAttributeValue(null, "ut");
        if (timeStampStr != null) {
            try {
                ps.lastUpdateTime = Long.parseLong(timeStampStr, 16);
            } catch (NumberFormatException e) {
            }
        }
        String idStr = parser.getAttributeValue(null, "userId");
<<<<<<< HEAD
        ps.appId = idStr != null ? Integer.parseInt(idStr) : 0;
        if (ps.appId <= 0) {
            String sharedIdStr = parser.getAttributeValue(null, "sharedUserId");
            ps.appId = sharedIdStr != null ? Integer.parseInt(sharedIdStr) : 0;
=======
        ps.userId = idStr != null ? Integer.parseInt(idStr) : 0;
        if (ps.userId <= 0) {
            String sharedIdStr = parser.getAttributeValue(null, "sharedUserId");
            ps.userId = sharedIdStr != null ? Integer.parseInt(sharedIdStr) : 0;
>>>>>>> upstream/master
        }
        int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals("perms")) {
                readGrantedPermissionsLPw(parser, ps.grantedPermissions);
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Unknown element under <updated-package>: " + parser.getName());
                XmlUtils.skipCurrentTag(parser);
            }
        }
        mDisabledSysPackages.put(name, ps);
    }

    private void readPackageLPw(XmlPullParser parser) throws XmlPullParserException, IOException {
        String name = null;
        String realName = null;
        String idStr = null;
        String sharedIdStr = null;
        String codePathStr = null;
        String resourcePathStr = null;
        String nativeLibraryPathStr = null;
        String systemStr = null;
        String installerPackageName = null;
        String uidError = null;
        int pkgFlags = 0;
        long timeStamp = 0;
        long firstInstallTime = 0;
        long lastUpdateTime = 0;
        PackageSettingBase packageSetting = null;
        String version = null;
        int versionCode = 0;
        try {
<<<<<<< HEAD
            name = parser.getAttributeValue(null, ATTR_NAME);
=======
            name = parser.getAttributeValue(null, "name");
>>>>>>> upstream/master
            realName = parser.getAttributeValue(null, "realName");
            idStr = parser.getAttributeValue(null, "userId");
            uidError = parser.getAttributeValue(null, "uidError");
            sharedIdStr = parser.getAttributeValue(null, "sharedUserId");
            codePathStr = parser.getAttributeValue(null, "codePath");
            resourcePathStr = parser.getAttributeValue(null, "resourcePath");
            nativeLibraryPathStr = parser.getAttributeValue(null, "nativeLibraryPath");
            version = parser.getAttributeValue(null, "version");
            if (version != null) {
                try {
                    versionCode = Integer.parseInt(version);
                } catch (NumberFormatException e) {
                }
            }
            installerPackageName = parser.getAttributeValue(null, "installer");

            systemStr = parser.getAttributeValue(null, "flags");
            if (systemStr != null) {
                try {
                    pkgFlags = Integer.parseInt(systemStr);
                } catch (NumberFormatException e) {
                }
            } else {
                // For backward compatibility
                systemStr = parser.getAttributeValue(null, "system");
                if (systemStr != null) {
                    pkgFlags |= ("true".equalsIgnoreCase(systemStr)) ? ApplicationInfo.FLAG_SYSTEM
                            : 0;
                } else {
                    // Old settings that don't specify system... just treat
                    // them as system, good enough.
                    pkgFlags |= ApplicationInfo.FLAG_SYSTEM;
                }
            }
            String timeStampStr = parser.getAttributeValue(null, "ft");
            if (timeStampStr != null) {
                try {
                    timeStamp = Long.parseLong(timeStampStr, 16);
                } catch (NumberFormatException e) {
                }
            } else {
                timeStampStr = parser.getAttributeValue(null, "ts");
                if (timeStampStr != null) {
                    try {
                        timeStamp = Long.parseLong(timeStampStr);
                    } catch (NumberFormatException e) {
                    }
                }
            }
            timeStampStr = parser.getAttributeValue(null, "it");
            if (timeStampStr != null) {
                try {
                    firstInstallTime = Long.parseLong(timeStampStr, 16);
                } catch (NumberFormatException e) {
                }
            }
            timeStampStr = parser.getAttributeValue(null, "ut");
            if (timeStampStr != null) {
                try {
                    lastUpdateTime = Long.parseLong(timeStampStr, 16);
                } catch (NumberFormatException e) {
                }
            }
            if (PackageManagerService.DEBUG_SETTINGS)
                Log.v(PackageManagerService.TAG, "Reading package: " + name + " userId=" + idStr
                        + " sharedUserId=" + sharedIdStr);
            int userId = idStr != null ? Integer.parseInt(idStr) : 0;
            if (resourcePathStr == null) {
                resourcePathStr = codePathStr;
            }
            if (realName != null) {
                realName = realName.intern();
            }
            if (name == null) {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Error in package manager settings: <package> has no name at "
                                + parser.getPositionDescription());
            } else if (codePathStr == null) {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Error in package manager settings: <package> has no codePath at "
                                + parser.getPositionDescription());
            } else if (userId > 0) {
                packageSetting = addPackageLPw(name.intern(), realName, new File(codePathStr),
                        new File(resourcePathStr), nativeLibraryPathStr, userId, versionCode,
                        pkgFlags);
                if (PackageManagerService.DEBUG_SETTINGS)
                    Log.i(PackageManagerService.TAG, "Reading package " + name + ": userId="
                            + userId + " pkg=" + packageSetting);
                if (packageSetting == null) {
                    PackageManagerService.reportSettingsProblem(Log.ERROR, "Failure adding uid "
                            + userId + " while parsing settings at "
                            + parser.getPositionDescription());
                } else {
                    packageSetting.setTimeStamp(timeStamp);
                    packageSetting.firstInstallTime = firstInstallTime;
                    packageSetting.lastUpdateTime = lastUpdateTime;
                }
            } else if (sharedIdStr != null) {
                userId = sharedIdStr != null ? Integer.parseInt(sharedIdStr) : 0;
                if (userId > 0) {
                    packageSetting = new PendingPackage(name.intern(), realName, new File(
                            codePathStr), new File(resourcePathStr), nativeLibraryPathStr, userId,
                            versionCode, pkgFlags);
                    packageSetting.setTimeStamp(timeStamp);
                    packageSetting.firstInstallTime = firstInstallTime;
                    packageSetting.lastUpdateTime = lastUpdateTime;
                    mPendingPackages.add((PendingPackage) packageSetting);
                    if (PackageManagerService.DEBUG_SETTINGS)
                        Log.i(PackageManagerService.TAG, "Reading package " + name
                                + ": sharedUserId=" + userId + " pkg=" + packageSetting);
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Error in package manager settings: package " + name
                                    + " has bad sharedId " + sharedIdStr + " at "
                                    + parser.getPositionDescription());
                }
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Error in package manager settings: package " + name + " has bad userId "
                                + idStr + " at " + parser.getPositionDescription());
            }
        } catch (NumberFormatException e) {
            PackageManagerService.reportSettingsProblem(Log.WARN,
                    "Error in package manager settings: package " + name + " has bad userId "
                            + idStr + " at " + parser.getPositionDescription());
        }
        if (packageSetting != null) {
            packageSetting.uidError = "true".equals(uidError);
            packageSetting.installerPackageName = installerPackageName;
            packageSetting.nativeLibraryPathString = nativeLibraryPathStr;
<<<<<<< HEAD
            // Handle legacy string here for single-user mode
            final String enabledStr = parser.getAttributeValue(null, ATTR_ENABLED);
            if (enabledStr != null) {
                try {
                    packageSetting.setEnabled(Integer.parseInt(enabledStr), 0 /* userId */);
                } catch (NumberFormatException e) {
                    if (enabledStr.equalsIgnoreCase("true")) {
                        packageSetting.setEnabled(COMPONENT_ENABLED_STATE_ENABLED, 0);
                    } else if (enabledStr.equalsIgnoreCase("false")) {
                        packageSetting.setEnabled(COMPONENT_ENABLED_STATE_DISABLED, 0);
                    } else if (enabledStr.equalsIgnoreCase("default")) {
                        packageSetting.setEnabled(COMPONENT_ENABLED_STATE_DEFAULT, 0);
=======
            final String enabledStr = parser.getAttributeValue(null, "enabled");
            if (enabledStr != null) {
                try {
                    packageSetting.enabled = Integer.parseInt(enabledStr);
                } catch (NumberFormatException e) {
                    if (enabledStr.equalsIgnoreCase("true")) {
                        packageSetting.enabled = COMPONENT_ENABLED_STATE_ENABLED;
                    } else if (enabledStr.equalsIgnoreCase("false")) {
                        packageSetting.enabled = COMPONENT_ENABLED_STATE_DISABLED;
                    } else if (enabledStr.equalsIgnoreCase("default")) {
                        packageSetting.enabled = COMPONENT_ENABLED_STATE_DEFAULT;
>>>>>>> upstream/master
                    } else {
                        PackageManagerService.reportSettingsProblem(Log.WARN,
                                "Error in package manager settings: package " + name
                                        + " has bad enabled value: " + idStr + " at "
                                        + parser.getPositionDescription());
                    }
                }
            } else {
<<<<<<< HEAD
                packageSetting.setEnabled(COMPONENT_ENABLED_STATE_DEFAULT, 0);
            }

=======
                packageSetting.enabled = COMPONENT_ENABLED_STATE_DEFAULT;
            }
>>>>>>> upstream/master
            final String installStatusStr = parser.getAttributeValue(null, "installStatus");
            if (installStatusStr != null) {
                if (installStatusStr.equalsIgnoreCase("false")) {
                    packageSetting.installStatus = PackageSettingBase.PKG_INSTALL_INCOMPLETE;
                } else {
                    packageSetting.installStatus = PackageSettingBase.PKG_INSTALL_COMPLETE;
                }
            }

            int outerDepth = parser.getDepth();
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                    && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
                if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                    continue;
                }

                String tagName = parser.getName();
<<<<<<< HEAD
                // Legacy 
                if (tagName.equals(TAG_DISABLED_COMPONENTS)) {
                    readDisabledComponentsLPw(packageSetting, parser, 0);
                } else if (tagName.equals(TAG_ENABLED_COMPONENTS)) {
                    readEnabledComponentsLPw(packageSetting, parser, 0);
=======
                if (tagName.equals("disabled-components")) {
                    readDisabledComponentsLPw(packageSetting, parser);
                } else if (tagName.equals("enabled-components")) {
                    readEnabledComponentsLPw(packageSetting, parser);
>>>>>>> upstream/master
                } else if (tagName.equals("sigs")) {
                    packageSetting.signatures.readXml(parser, mPastSignatures);
                } else if (tagName.equals("perms")) {
                    readGrantedPermissionsLPw(parser, packageSetting.grantedPermissions);
                    packageSetting.permissionsFixed = true;
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Unknown element under <package>: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        } else {
            XmlUtils.skipCurrentTag(parser);
        }
    }

<<<<<<< HEAD
    private void readDisabledComponentsLPw(PackageSettingBase packageSetting, XmlPullParser parser,
            int userId) throws IOException, XmlPullParserException {
=======
    private void readDisabledComponentsLPw(PackageSettingBase packageSetting, XmlPullParser parser)
            throws IOException, XmlPullParserException {
>>>>>>> upstream/master
        int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
<<<<<<< HEAD
            if (tagName.equals(TAG_ITEM)) {
                String name = parser.getAttributeValue(null, ATTR_NAME);
                if (name != null) {
                    packageSetting.addDisabledComponent(name.intern(), userId);
=======
            if (tagName.equals("item")) {
                String name = parser.getAttributeValue(null, "name");
                if (name != null) {
                    packageSetting.disabledComponents.add(name.intern());
>>>>>>> upstream/master
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Error in package manager settings: <disabled-components> has"
                                    + " no name at " + parser.getPositionDescription());
                }
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Unknown element under <disabled-components>: " + parser.getName());
            }
            XmlUtils.skipCurrentTag(parser);
        }
    }

<<<<<<< HEAD
    private void readEnabledComponentsLPw(PackageSettingBase packageSetting, XmlPullParser parser,
            int userId) throws IOException, XmlPullParserException {
=======
    private void readEnabledComponentsLPw(PackageSettingBase packageSetting, XmlPullParser parser)
            throws IOException, XmlPullParserException {
>>>>>>> upstream/master
        int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
<<<<<<< HEAD
            if (tagName.equals(TAG_ITEM)) {
                String name = parser.getAttributeValue(null, ATTR_NAME);
                if (name != null) {
                    packageSetting.addEnabledComponent(name.intern(), userId);
=======
            if (tagName.equals("item")) {
                String name = parser.getAttributeValue(null, "name");
                if (name != null) {
                    packageSetting.enabledComponents.add(name.intern());
>>>>>>> upstream/master
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Error in package manager settings: <enabled-components> has"
                                    + " no name at " + parser.getPositionDescription());
                }
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Unknown element under <enabled-components>: " + parser.getName());
            }
            XmlUtils.skipCurrentTag(parser);
        }
    }

<<<<<<< HEAD
    private void readSharedUserLPw(XmlPullParser parser) throws XmlPullParserException,IOException {
=======
    private void readSharedUserLPw(XmlPullParser parser) throws XmlPullParserException, IOException {
>>>>>>> upstream/master
        String name = null;
        String idStr = null;
        int pkgFlags = 0;
        SharedUserSetting su = null;
        try {
<<<<<<< HEAD
            name = parser.getAttributeValue(null, ATTR_NAME);
=======
            name = parser.getAttributeValue(null, "name");
>>>>>>> upstream/master
            idStr = parser.getAttributeValue(null, "userId");
            int userId = idStr != null ? Integer.parseInt(idStr) : 0;
            if ("true".equals(parser.getAttributeValue(null, "system"))) {
                pkgFlags |= ApplicationInfo.FLAG_SYSTEM;
            }
            if (name == null) {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Error in package manager settings: <shared-user> has no name at "
                                + parser.getPositionDescription());
            } else if (userId == 0) {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Error in package manager settings: shared-user " + name
                                + " has bad userId " + idStr + " at "
                                + parser.getPositionDescription());
            } else {
                if ((su = addSharedUserLPw(name.intern(), userId, pkgFlags)) == null) {
                    PackageManagerService
                            .reportSettingsProblem(Log.ERROR, "Occurred while parsing settings at "
                                    + parser.getPositionDescription());
                }
            }
        } catch (NumberFormatException e) {
            PackageManagerService.reportSettingsProblem(Log.WARN,
                    "Error in package manager settings: package " + name + " has bad userId "
                            + idStr + " at " + parser.getPositionDescription());
        }
        ;

        if (su != null) {
            int outerDepth = parser.getDepth();
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                    && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
                if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                    continue;
                }

                String tagName = parser.getName();
                if (tagName.equals("sigs")) {
                    su.signatures.readXml(parser, mPastSignatures);
                } else if (tagName.equals("perms")) {
                    readGrantedPermissionsLPw(parser, su.grantedPermissions);
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Unknown element under <shared-user>: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }

        } else {
            XmlUtils.skipCurrentTag(parser);
        }
    }

    private void readGrantedPermissionsLPw(XmlPullParser parser, HashSet<String> outPerms)
            throws IOException, XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
<<<<<<< HEAD
            if (tagName.equals(TAG_ITEM)) {
                String name = parser.getAttributeValue(null, ATTR_NAME);
=======
            if (tagName.equals("item")) {
                String name = parser.getAttributeValue(null, "name");
>>>>>>> upstream/master
                if (name != null) {
                    outPerms.add(name.intern());
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Error in package manager settings: <perms> has" + " no name at "
                                    + parser.getPositionDescription());
                }
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Unknown element under <perms>: " + parser.getName());
            }
            XmlUtils.skipCurrentTag(parser);
        }
    }

    private void readPreferredActivitiesLPw(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        int outerDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
<<<<<<< HEAD
            if (tagName.equals(TAG_ITEM)) {
=======
            if (tagName.equals("item")) {
>>>>>>> upstream/master
                PreferredActivity pa = new PreferredActivity(parser);
                if (pa.mPref.getParseError() == null) {
                    mPreferredActivities.addFilter(pa);
                } else {
                    PackageManagerService.reportSettingsProblem(Log.WARN,
                            "Error in package manager settings: <preferred-activity> "
                                    + pa.mPref.getParseError() + " at "
                                    + parser.getPositionDescription());
                }
            } else {
                PackageManagerService.reportSettingsProblem(Log.WARN,
                        "Unknown element under <preferred-activities>: " + parser.getName());
                XmlUtils.skipCurrentTag(parser);
            }
        }
    }

<<<<<<< HEAD
    void removeUserLPr(int userId) {
        File file = getUserPackagesStateFile(userId);
        file.delete();
        file = getUserPackagesStateBackupFile(userId);
        file.delete();
    }

=======
>>>>>>> upstream/master
    // Returns -1 if we could not find an available UserId to assign
    private int newUserIdLPw(Object obj) {
        // Let's be stupidly inefficient for now...
        final int N = mUserIds.size();
        for (int i = 0; i < N; i++) {
            if (mUserIds.get(i) == null) {
                mUserIds.set(i, obj);
<<<<<<< HEAD
                return Process.FIRST_APPLICATION_UID + i;
=======
                return PackageManagerService.FIRST_APPLICATION_UID + i;
>>>>>>> upstream/master
            }
        }

        // None left?
<<<<<<< HEAD
        if (N > (Process.LAST_APPLICATION_UID-Process.FIRST_APPLICATION_UID)) {
=======
        if (N >= PackageManagerService.MAX_APPLICATION_UIDS) {
>>>>>>> upstream/master
            return -1;
        }

        mUserIds.add(obj);
<<<<<<< HEAD
        return Process.FIRST_APPLICATION_UID + N;
=======
        return PackageManagerService.FIRST_APPLICATION_UID + N;
>>>>>>> upstream/master
    }

    public VerifierDeviceIdentity getVerifierDeviceIdentityLPw() {
        if (mVerifierDeviceIdentity == null) {
            mVerifierDeviceIdentity = VerifierDeviceIdentity.generate();

            writeLPr();
        }

        return mVerifierDeviceIdentity;
    }

    public PackageSetting getDisabledSystemPkgLPr(String name) {
        PackageSetting ps = mDisabledSysPackages.get(name);
        return ps;
    }

<<<<<<< HEAD
    boolean isEnabledLPr(ComponentInfo componentInfo, int flags, int userId) {
        if ((flags&PackageManager.GET_DISABLED_COMPONENTS) != 0) {
            return true;
        }
        final String pkgName = componentInfo.packageName;
        final PackageSetting packageSettings = mPackages.get(pkgName);
        if (PackageManagerService.DEBUG_SETTINGS) {
            Log.v(PackageManagerService.TAG, "isEnabledLock - packageName = "
                    + componentInfo.packageName + " componentName = " + componentInfo.name);
            Log.v(PackageManagerService.TAG, "enabledComponents: "
                    + Arrays.toString(packageSettings.getEnabledComponents(userId).toArray()));
            Log.v(PackageManagerService.TAG, "disabledComponents: "
                    + Arrays.toString(packageSettings.getDisabledComponents(userId).toArray()));
=======
    boolean isEnabledLPr(ComponentInfo componentInfo, int flags) {
        if ((flags&PackageManager.GET_DISABLED_COMPONENTS) != 0) {
            return true;
        }
        final PackageSetting packageSettings = mPackages.get(componentInfo.packageName);
        if (PackageManagerService.DEBUG_SETTINGS) {
            Log.v(PackageManagerService.TAG, "isEnabledLock - packageName = " + componentInfo.packageName
                       + " componentName = " + componentInfo.name);
            Log.v(PackageManagerService.TAG, "enabledComponents: "
                       + Arrays.toString(packageSettings.enabledComponents.toArray()));
            Log.v(PackageManagerService.TAG, "disabledComponents: "
                       + Arrays.toString(packageSettings.disabledComponents.toArray()));
>>>>>>> upstream/master
        }
        if (packageSettings == null) {
            return false;
        }
<<<<<<< HEAD
        final int enabled = packageSettings.getEnabled(userId);
        if (enabled == COMPONENT_ENABLED_STATE_DISABLED
                || enabled == COMPONENT_ENABLED_STATE_DISABLED_USER
                || (packageSettings.pkg != null && !packageSettings.pkg.applicationInfo.enabled
                    && enabled == COMPONENT_ENABLED_STATE_DEFAULT)) {
            return false;
        }
        if (packageSettings.getEnabledComponents(userId).contains(componentInfo.name)) {
            return true;
        }
        if (packageSettings.getDisabledComponents(userId).contains(componentInfo.name)) {
=======
        if (packageSettings.enabled == COMPONENT_ENABLED_STATE_DISABLED
                || packageSettings.enabled == COMPONENT_ENABLED_STATE_DISABLED_USER
                || (packageSettings.pkg != null && !packageSettings.pkg.applicationInfo.enabled
                        && packageSettings.enabled == COMPONENT_ENABLED_STATE_DEFAULT)) {
            return false;
        }
        if (packageSettings.enabledComponents.contains(componentInfo.name)) {
            return true;
        }
        if (packageSettings.disabledComponents.contains(componentInfo.name)) {
>>>>>>> upstream/master
            return false;
        }
        return componentInfo.enabled;
    }

    String getInstallerPackageNameLPr(String packageName) {
        final PackageSetting pkg = mPackages.get(packageName);
        if (pkg == null) {
            throw new IllegalArgumentException("Unknown package: " + packageName);
        }
        return pkg.installerPackageName;
    }

<<<<<<< HEAD
    int getApplicationEnabledSettingLPr(String packageName, int userId) {
=======
    int getApplicationEnabledSettingLPr(String packageName) {
>>>>>>> upstream/master
        final PackageSetting pkg = mPackages.get(packageName);
        if (pkg == null) {
            throw new IllegalArgumentException("Unknown package: " + packageName);
        }
<<<<<<< HEAD
        return pkg.getEnabled(userId);
    }

    int getComponentEnabledSettingLPr(ComponentName componentName, int userId) {
=======
        return pkg.enabled;
    }

    int getComponentEnabledSettingLPr(ComponentName componentName) {
>>>>>>> upstream/master
        final String packageName = componentName.getPackageName();
        final PackageSetting pkg = mPackages.get(packageName);
        if (pkg == null) {
            throw new IllegalArgumentException("Unknown component: " + componentName);
        }
        final String classNameStr = componentName.getClassName();
<<<<<<< HEAD
        return pkg.getCurrentEnabledStateLPr(classNameStr, userId);
    }

    boolean setPackageStoppedStateLPw(String packageName, boolean stopped,
            boolean allowedByPermission, int uid, int userId) {
        int appId = UserId.getAppId(uid);
=======
        return pkg.getCurrentEnabledStateLPr(classNameStr);
    }
    
    boolean setPackageStoppedStateLPw(String packageName, boolean stopped,
            boolean allowedByPermission, int uid) {
>>>>>>> upstream/master
        final PackageSetting pkgSetting = mPackages.get(packageName);
        if (pkgSetting == null) {
            throw new IllegalArgumentException("Unknown package: " + packageName);
        }
<<<<<<< HEAD
        if (!allowedByPermission && (appId != pkgSetting.appId)) {
            throw new SecurityException(
                    "Permission Denial: attempt to change stopped state from pid="
                    + Binder.getCallingPid()
                    + ", uid=" + uid + ", package uid=" + pkgSetting.appId);
=======
        if (!allowedByPermission && (uid != pkgSetting.userId)) {
            throw new SecurityException(
                    "Permission Denial: attempt to change stopped state from pid="
                    + Binder.getCallingPid()
                    + ", uid=" + uid + ", package uid=" + pkgSetting.userId);
>>>>>>> upstream/master
        }
        if (DEBUG_STOPPED) {
            if (stopped) {
                RuntimeException e = new RuntimeException("here");
                e.fillInStackTrace();
                Slog.i(TAG, "Stopping package " + packageName, e);
            }
        }
<<<<<<< HEAD
        if (pkgSetting.getStopped(userId) != stopped) {
            pkgSetting.setStopped(stopped, userId);
            // pkgSetting.pkg.mSetStopped = stopped;
            if (pkgSetting.getNotLaunched(userId)) {
                if (pkgSetting.installerPackageName != null) {
                    PackageManagerService.sendPackageBroadcast(Intent.ACTION_PACKAGE_FIRST_LAUNCH,
                            pkgSetting.name, null, null,
                            pkgSetting.installerPackageName, null, userId);
                }
                pkgSetting.setNotLaunched(false, userId);
=======
        if (pkgSetting.stopped != stopped) {
            pkgSetting.stopped = stopped;
            pkgSetting.pkg.mSetStopped = stopped;
            if (pkgSetting.notLaunched) {
                if (pkgSetting.installerPackageName != null) {
                    PackageManagerService.sendPackageBroadcast(Intent.ACTION_PACKAGE_FIRST_LAUNCH,
                            pkgSetting.name, null, null,
                            pkgSetting.installerPackageName, null);
                }
                pkgSetting.notLaunched = false;
>>>>>>> upstream/master
            }
            return true;
        }
        return false;
    }

<<<<<<< HEAD
    private List<UserInfo> getAllUsers() {
        long id = Binder.clearCallingIdentity();
        try {
            return AppGlobals.getPackageManager().getUsers();
        } catch (RemoteException re) {
            // Local to system process, shouldn't happen
        } catch (NullPointerException npe) {
            // packagemanager not yet initialized
        } finally {
            Binder.restoreCallingIdentity(id);
        }
        return null;
    }

    static final void printFlags(PrintWriter pw, int val, Object[] spec) {
        pw.print("[ ");
        for (int i=0; i<spec.length; i+=2) {
            int mask = (Integer)spec[i];
            if ((val & mask) != 0) {
                pw.print(spec[i+1]);
                pw.print(" ");
            }
        }
        pw.print("]");
    }

    static final Object[] FLAG_DUMP_SPEC = new Object[] {
        ApplicationInfo.FLAG_SYSTEM, "SYSTEM",
        ApplicationInfo.FLAG_DEBUGGABLE, "DEBUGGABLE",
        ApplicationInfo.FLAG_HAS_CODE, "HAS_CODE",
        ApplicationInfo.FLAG_PERSISTENT, "PERSISTENT",
        ApplicationInfo.FLAG_FACTORY_TEST, "FACTORY_TEST",
        ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING, "ALLOW_TASK_REPARENTING",
        ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA, "ALLOW_CLEAR_USER_DATA",
        ApplicationInfo.FLAG_UPDATED_SYSTEM_APP, "UPDATED_SYSTEM_APP",
        ApplicationInfo.FLAG_TEST_ONLY, "TEST_ONLY",
        ApplicationInfo.FLAG_VM_SAFE_MODE, "VM_SAFE_MODE",
        ApplicationInfo.FLAG_ALLOW_BACKUP, "ALLOW_BACKUP",
        ApplicationInfo.FLAG_KILL_AFTER_RESTORE, "KILL_AFTER_RESTORE",
        ApplicationInfo.FLAG_RESTORE_ANY_VERSION, "RESTORE_ANY_VERSION",
        ApplicationInfo.FLAG_EXTERNAL_STORAGE, "EXTERNAL_STORAGE",
        ApplicationInfo.FLAG_LARGE_HEAP, "LARGE_HEAP",
        ApplicationInfo.FLAG_STOPPED, "STOPPED",
        ApplicationInfo.FLAG_FORWARD_LOCK, "FORWARD_LOCK",
        ApplicationInfo.FLAG_CANT_SAVE_STATE, "CANT_SAVE_STATE",
    };

=======
>>>>>>> upstream/master
    void dumpPackagesLPr(PrintWriter pw, String packageName, DumpState dumpState) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date();
        boolean printedSomething = false;
<<<<<<< HEAD
        List<UserInfo> users = getAllUsers();
=======
>>>>>>> upstream/master
        for (final PackageSetting ps : mPackages.values()) {
            if (packageName != null && !packageName.equals(ps.realName)
                    && !packageName.equals(ps.name)) {
                continue;
            }

            if (packageName != null) {
                dumpState.setSharedUser(ps.sharedUser);
            }

            if (!printedSomething) {
                if (dumpState.onTitlePrinted())
                    pw.println(" ");
                pw.println("Packages:");
                printedSomething = true;
            }
            pw.print("  Package [");
                pw.print(ps.realName != null ? ps.realName : ps.name);
                pw.print("] (");
                pw.print(Integer.toHexString(System.identityHashCode(ps)));
                pw.println("):");

            if (ps.realName != null) {
                pw.print("    compat name=");
                pw.println(ps.name);
            }

<<<<<<< HEAD
            pw.print("    userId="); pw.print(ps.appId);
=======
            pw.print("    userId="); pw.print(ps.userId);
>>>>>>> upstream/master
            pw.print(" gids="); pw.println(PackageManagerService.arrayToString(ps.gids));
            pw.print("    sharedUser="); pw.println(ps.sharedUser);
            pw.print("    pkg="); pw.println(ps.pkg);
            pw.print("    codePath="); pw.println(ps.codePathString);
            pw.print("    resourcePath="); pw.println(ps.resourcePathString);
            pw.print("    nativeLibraryPath="); pw.println(ps.nativeLibraryPathString);
            pw.print("    versionCode="); pw.println(ps.versionCode);
            if (ps.pkg != null) {
<<<<<<< HEAD
                pw.print("    applicationInfo="); pw.println(ps.pkg.applicationInfo.toString());
                pw.print("    flags="); printFlags(pw, ps.pkg.applicationInfo.flags, FLAG_DUMP_SPEC); pw.println();
=======
>>>>>>> upstream/master
                pw.print("    versionName="); pw.println(ps.pkg.mVersionName);
                pw.print("    dataDir="); pw.println(ps.pkg.applicationInfo.dataDir);
                pw.print("    targetSdk="); pw.println(ps.pkg.applicationInfo.targetSdkVersion);
                if (ps.pkg.mOperationPending) {
                    pw.println("    mOperationPending=true");
                }
                pw.print("    supportsScreens=[");
                boolean first = true;
                if ((ps.pkg.applicationInfo.flags & ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS) != 0) {
                    if (!first)
                        pw.print(", ");
                    first = false;
                    pw.print("small");
                }
                if ((ps.pkg.applicationInfo.flags & ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS) != 0) {
                    if (!first)
                        pw.print(", ");
                    first = false;
                    pw.print("medium");
                }
                if ((ps.pkg.applicationInfo.flags & ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS) != 0) {
                    if (!first)
                        pw.print(", ");
                    first = false;
                    pw.print("large");
                }
                if ((ps.pkg.applicationInfo.flags & ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS) != 0) {
                    if (!first)
                        pw.print(", ");
                    first = false;
                    pw.print("xlarge");
                }
                if ((ps.pkg.applicationInfo.flags & ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS) != 0) {
                    if (!first)
                        pw.print(", ");
                    first = false;
                    pw.print("resizeable");
                }
                if ((ps.pkg.applicationInfo.flags & ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES) != 0) {
                    if (!first)
                        pw.print(", ");
                    first = false;
                    pw.print("anyDensity");
                }
            }
            pw.println("]");
            pw.print("    timeStamp=");
                date.setTime(ps.timeStamp);
                pw.println(sdf.format(date));
            pw.print("    firstInstallTime=");
                date.setTime(ps.firstInstallTime);
                pw.println(sdf.format(date));
            pw.print("    lastUpdateTime=");
                date.setTime(ps.lastUpdateTime);
                pw.println(sdf.format(date));
            if (ps.installerPackageName != null) {
                pw.print("    installerPackageName="); pw.println(ps.installerPackageName);
            }
            pw.print("    signatures="); pw.println(ps.signatures);
            pw.print("    permissionsFixed="); pw.print(ps.permissionsFixed);
            pw.print(" haveGids="); pw.println(ps.haveGids);
            pw.print("    pkgFlags=0x"); pw.print(Integer.toHexString(ps.pkgFlags));
            pw.print(" installStatus="); pw.print(ps.installStatus);
<<<<<<< HEAD
            for (UserInfo user : users) {
                pw.print(" User "); pw.print(user.id); pw.print(": ");
                pw.print(" stopped=");
                pw.print(ps.getStopped(user.id));
                pw.print(" enabled=");
                pw.println(ps.getEnabled(user.id));
                if (ps.getDisabledComponents(user.id).size() > 0) {
                    pw.println("    disabledComponents:");
                    for (String s : ps.getDisabledComponents(user.id)) {
                        pw.print("      "); pw.println(s);
                    }
                }
                if (ps.getEnabledComponents(user.id).size() > 0) {
                    pw.println("    enabledComponents:");
                    for (String s : ps.getEnabledComponents(user.id)) {
                        pw.print("      "); pw.println(s);
                    }
=======
            pw.print(" stopped="); pw.print(ps.stopped);
            pw.print(" enabled="); pw.println(ps.enabled);
            if (ps.disabledComponents.size() > 0) {
                pw.println("    disabledComponents:");
                for (String s : ps.disabledComponents) {
                    pw.print("      "); pw.println(s);
                }
            }
            if (ps.enabledComponents.size() > 0) {
                pw.println("    enabledComponents:");
                for (String s : ps.enabledComponents) {
                    pw.print("      "); pw.println(s);
>>>>>>> upstream/master
                }
            }
            if (ps.grantedPermissions.size() > 0) {
                pw.println("    grantedPermissions:");
                for (String s : ps.grantedPermissions) {
                    pw.print("      "); pw.println(s);
                }
            }
        }

        printedSomething = false;
        if (mRenamedPackages.size() > 0) {
<<<<<<< HEAD
            for (final Map.Entry<String, String> e : mRenamedPackages.entrySet()) {
=======
            for (final HashMap.Entry<String, String> e : mRenamedPackages.entrySet()) {
>>>>>>> upstream/master
                if (packageName != null && !packageName.equals(e.getKey())
                        && !packageName.equals(e.getValue())) {
                    continue;
                }
                if (!printedSomething) {
                    if (dumpState.onTitlePrinted())
                        pw.println(" ");
                    pw.println("Renamed packages:");
                    printedSomething = true;
                }
                pw.print("  ");
                pw.print(e.getKey());
                pw.print(" -> ");
                pw.println(e.getValue());
            }
        }

        printedSomething = false;
        if (mDisabledSysPackages.size() > 0) {
            for (final PackageSetting ps : mDisabledSysPackages.values()) {
                if (packageName != null && !packageName.equals(ps.realName)
                        && !packageName.equals(ps.name)) {
                    continue;
                }
                if (!printedSomething) {
                    if (dumpState.onTitlePrinted())
                        pw.println(" ");
                    pw.println("Hidden system packages:");
                    printedSomething = true;
                }
                pw.print("  Package [");
                pw.print(ps.realName != null ? ps.realName : ps.name);
                pw.print("] (");
                pw.print(Integer.toHexString(System.identityHashCode(ps)));
                pw.println("):");
                if (ps.realName != null) {
                    pw.print("    compat name=");
                    pw.println(ps.name);
                }
<<<<<<< HEAD
                if (ps.pkg != null && ps.pkg.applicationInfo != null) {
                    pw.print("    applicationInfo=");
                    pw.println(ps.pkg.applicationInfo.toString());
                }
                pw.print("    userId=");
                pw.println(ps.appId);
=======
                pw.print("    userId=");
                pw.println(ps.userId);
>>>>>>> upstream/master
                pw.print("    sharedUser=");
                pw.println(ps.sharedUser);
                pw.print("    codePath=");
                pw.println(ps.codePathString);
                pw.print("    resourcePath=");
                pw.println(ps.resourcePathString);
            }
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> upstream/master
    void dumpPermissionsLPr(PrintWriter pw, String packageName, DumpState dumpState) {
        boolean printedSomething = false;
        for (BasePermission p : mPermissions.values()) {
            if (packageName != null && !packageName.equals(p.sourcePackage)) {
                continue;
            }
            if (!printedSomething) {
                if (dumpState.onTitlePrinted())
                    pw.println(" ");
                pw.println("Permissions:");
                printedSomething = true;
            }
            pw.print("  Permission ["); pw.print(p.name); pw.print("] (");
                    pw.print(Integer.toHexString(System.identityHashCode(p)));
                    pw.println("):");
            pw.print("    sourcePackage="); pw.println(p.sourcePackage);
            pw.print("    uid="); pw.print(p.uid);
                    pw.print(" gids="); pw.print(PackageManagerService.arrayToString(p.gids));
                    pw.print(" type="); pw.print(p.type);
<<<<<<< HEAD
                    pw.print(" prot=");
                    pw.println(PermissionInfo.protectionToString(p.protectionLevel));
=======
                    pw.print(" prot="); pw.println(p.protectionLevel);
>>>>>>> upstream/master
            if (p.packageSetting != null) {
                pw.print("    packageSetting="); pw.println(p.packageSetting);
            }
            if (p.perm != null) {
                pw.print("    perm="); pw.println(p.perm);
            }
<<<<<<< HEAD
            if (READ_EXTERNAL_STORAGE.equals(p.name)) {
                pw.print("    enforced=");
                pw.println(mReadExternalStorageEnforced);
            }
        }
    }

=======
        }
    }
    
>>>>>>> upstream/master
    void dumpSharedUsersLPr(PrintWriter pw, String packageName, DumpState dumpState) {
        boolean printedSomething = false;
        for (SharedUserSetting su : mSharedUsers.values()) {
            if (packageName != null && su != dumpState.getSharedUser()) {
                continue;
            }
            if (!printedSomething) {
                if (dumpState.onTitlePrinted())
                    pw.println(" ");
                pw.println("Shared users:");
                printedSomething = true;
            }
            pw.print("  SharedUser [");
            pw.print(su.name);
            pw.print("] (");
            pw.print(Integer.toHexString(System.identityHashCode(su)));
                    pw.println("):");
            pw.print("    userId=");
            pw.print(su.userId);
            pw.print(" gids=");
            pw.println(PackageManagerService.arrayToString(su.gids));
            pw.println("    grantedPermissions:");
            for (String s : su.grantedPermissions) {
                pw.print("      ");
                pw.println(s);
            }
        }
    }

    void dumpReadMessagesLPr(PrintWriter pw, DumpState dumpState) {
        pw.println("Settings parse messages:");
        pw.print(mReadMessages.toString());
    }
}
