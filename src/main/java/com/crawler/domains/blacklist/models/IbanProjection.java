package com.crawler.domains.blacklist.models;

/**
 * With this class the <code>blacklisted_iban</code> table can be queried without fetching all of its content.
 * The only relevant content for the <code>BlacklistedIbanValidator</code> is the iban string itself and not other eventual metadata
 * present in the table.
 */
public interface IbanProjection {
    String getIban();
}
