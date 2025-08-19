package com.batching.license;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LicenseValidator {

    private static final String LICENSE_PATH = "D:/license.dat";

    public static boolean isLicenseValid() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(new FileReader(LICENSE_PATH));

            String expiry = (String) obj.get("validUntil");
            LocalDate expiryDate = LocalDate.parse(expiry, DateTimeFormatter.ISO_DATE);
            return !LocalDate.now().isAfter(expiryDate); // true = valid
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
