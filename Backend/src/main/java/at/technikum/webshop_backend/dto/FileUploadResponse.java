package at.technikum.webshop_backend.dto;

public class FileUploadResponse {
    public boolean success;

    public String reference;

    public FileUploadResponse(boolean success, String reference) {
        this.success = success;
        this.reference = reference;
    }
}
