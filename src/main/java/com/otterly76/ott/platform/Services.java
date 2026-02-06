package com.otterly76.ott.platform;

import com.otterly76.ott.platform.services.IConfigHelper;
import com.otterly76.ott.platform.services.IPlatform;

public class Services {
    public static final IConfigHelper CONFIG = new com.otterly76.ott.platform.neoforge.NeoForgeConfigHelper();
    public static final IPlatform PLATFORM = new com.otterly76.ott.platform.neoforge.NeoForgePlatform();
}
