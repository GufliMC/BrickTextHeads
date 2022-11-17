package com.guflimc.brick.textheads.api;

import org.jetbrains.annotations.ApiStatus;

public class TextHeadsAPI {

    private static TextHeadsManager manager;

    @ApiStatus.Internal
    public static void setManager(TextHeadsManager _manager) {
        manager = _manager;
    }

    //

    public static TextHeadsManager get() {
        return manager;
    }

}
