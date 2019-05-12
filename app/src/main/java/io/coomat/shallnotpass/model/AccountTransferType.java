package io.coomat.shallnotpass.model;

public enum AccountTransferType {
    IMPORT("Import"),
    EXPORT("Export");

    private String label;

    AccountTransferType(String label) {}
}
