package org.mule.modules;

/**
 * Wrapper exception for isolating client library's exceptions from Mule.
 * @author Juan Alberto López Cavallotti
 */
public class SolrModuleException extends Exception {

    /**
     * Build the exception with just a message.
     * @param message the message of the exception.
     */
    public SolrModuleException(String message) {
        super(message);
    }

    /**
     * Build the exeption with a message and a cause.
     * @param message the message of the exception.
     * @param cause  the exception that caused this exception.
     */
    public SolrModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
