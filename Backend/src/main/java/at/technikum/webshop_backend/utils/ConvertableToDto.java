package at.technikum.webshop_backend.utils;

/**
 * AN Interface to ensure a data Object can be converted to a DTO of itself.
 */
public interface ConvertableToDto <T extends DataTransferObject>{

    public T convertToDto();
}
