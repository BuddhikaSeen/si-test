package com.surroundinsurance.user.service.controller.dto;

public enum Provider {

    FACEBOOK,
    GOOGLE;

    public static boolean contains(String providerName) {
        for (Provider provider : Provider.values()) {
            if (provider.name().equals(providerName)) {
                return true;
            }
        }

        return false;
    }

}
