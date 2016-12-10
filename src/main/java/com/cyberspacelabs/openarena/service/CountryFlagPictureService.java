package com.cyberspacelabs.openarena.service;

import java.io.InputStream;

public interface CountryFlagPictureService {
    InputStream getPNG(String countryCode) throws Exception;
}
