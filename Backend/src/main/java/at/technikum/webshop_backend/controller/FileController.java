package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.FileUploadResponse;
import at.technikum.webshop_backend.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The {@code FileController} class handles HTTP requests related to file uploads.
 * It exposes endpoints for uploading files.
 *
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    private static final String authorityAdmin = "ROLE_ADMIN";



    /**
     * Handles an HTTP POST request to upload a file.
     *
     * @param file The uploaded MultipartFile.
     * @return A ResponseEntity with a FileUploadResponse indicating the success of the upload
     *         and an appropriate HTTP status code.
     */
    @PostMapping
    public ResponseEntity<FileUploadResponse>  upload(
            @RequestParam("file") MultipartFile file
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            String reference = fileService.upload(file);
            return ResponseEntity.ok(new FileUploadResponse(true, reference));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Handles an HTTP GET request to retrieve a file by its reference.
     *
     * @param reference The reference identifier for the file.
     * @return A ResponseEntity with the requested file as a Resource and appropriate HTTP headers
     *         indicating the file type and disposition, or a ResponseEntity with a "not found" status.
     */
    @GetMapping("/{reference}")
    public ResponseEntity<Resource> getFile(@PathVariable String reference) {
        Resource fileResource = fileService.get(reference);

        if (fileResource != null) {
            String filename = fileResource.getFilename();
            String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);

            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (fileExtension.equalsIgnoreCase("png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles an HTTP DELETE request to delete a file by its reference.
     *
     * @param reference The reference identifier for the file to be deleted.
     * @return A ResponseEntity with an appropriate HTTP status code, either indicating
     *         the successful deletion (204 No Content) or a "not found" status.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{reference}")
    public ResponseEntity<Void> deleteFile(@PathVariable String reference) {
        boolean deleted = fileService.delete(reference);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
