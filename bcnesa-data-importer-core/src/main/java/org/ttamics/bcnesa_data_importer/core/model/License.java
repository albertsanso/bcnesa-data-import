package org.ttamics.bcnesa_data_importer.core.model;

import org.albertsanso.commons.model.ValueObject;

public class License extends ValueObject {
    private final LicenseType licenseType;
    private final String licenseId;

    private License(LicenseType licenseType, String licenseId) {
        this.licenseType = licenseType;
        this.licenseId = licenseId;
    }

    public static License createCatalanaLicenseOf(String id) {
        return new License(LicenseType.CATALANA, id);
    }

    public static License createEspanyolaLicenseOf(String id) {
        return new License(LicenseType.ESPANYOLA, id);
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public String getLicenseId() {
        return licenseId;
    }
}
