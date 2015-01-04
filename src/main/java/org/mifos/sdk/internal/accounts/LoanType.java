/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal.accounts;

/**
 * Holds loan type information.
 */
public class LoanType {

    private Long id;
    private String code;
    private String value;

    /**
     * Returns the loan type ID.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Returns the loan type code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the loan type value.
     */
    public String getValue() {
        return this.value;
    }

}
