package at.technikum.webshop_backend.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service interface for managing files. This interface defines methods for uploading, retrieving, updating,
 * and deleting files by their references.
 */
@Service
public interface FileService {

    /**
     * Uploads a file and returns a reference to the uploaded file.
     *
     * @param file The MultipartFile to be uploaded.
     * @return A reference to the uploaded file.
     */
    String upload(MultipartFile file);

    /**
     * Retrieves a file by its reference.
     *
     * @param reference The reference to the file.
     * @return A Resource representing the retrieved file.
     */
    Resource get(String reference);

    /**
     * Deletes a file by its reference.
     *
     * @param reference The reference to the file.
     * @return True if the file was successfully deleted, false otherwise.
     */
    boolean delete(String reference);

    /**
     * Updates a file by its reference with a new MultipartFile.
     *
     * @param reference The reference to the file.
     * @param file      The new MultipartFile to update the file with.
     * @return True if the file was successfully updated, false otherwise.
     */
    boolean update(String reference, MultipartFile file);


}
