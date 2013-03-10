/*
 *    Copyright 2013 Juan Alberto López Cavallotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
