/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.mod_Invasion;
import invmod.common.util.VersionChecker;

public class ThreadGetData
extends Thread {
    public ThreadGetData() {
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        mod_Invasion.latestVersionNumber = VersionChecker.getLatestVersion();
        mod_Invasion.recentNews = VersionChecker.getRecentNews();
    }
}

