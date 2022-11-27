package com.guflimc.brick.texticons.api;

import org.jetbrains.annotations.ApiStatus;

public class TextIconsAPI {

    private static TextIconsManager manager;

    @ApiStatus.Internal
    public static void setManager(TextIconsManager _manager) {
        manager = _manager;
    }

    //

    public static TextIconsManager get() {
        return manager;
    }

}
