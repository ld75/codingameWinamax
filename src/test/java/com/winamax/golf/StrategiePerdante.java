package com.winamax.golf;

import java.util.List;

public class StrategiePerdante extends Throwable {
    private List<String> invalidPaths;

    public List<String> getInvalidPaths() {
        return this.invalidPaths;
    }
    StrategiePerdante(List<String> invalidPaths)
    {
        this.invalidPaths=invalidPaths;
    }
}
