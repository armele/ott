package com.otterly76.ott.client.controls;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public interface IKeyEntry {
    KeyMapping getKey();
    Component getKeyDesc();
    Component categoryName();
}
