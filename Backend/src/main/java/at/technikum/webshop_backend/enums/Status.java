package at.technikum.webshop_backend.enums;
public enum Status {
    IN_PROGRESS("In Progress"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private final String displayValue;

    Status (String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}