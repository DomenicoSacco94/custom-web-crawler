package com.crawler.domains.regexps.models;

/**
 * With this class the <code>regexps</code> table can be queried without fetching all of its content.
 * The only relevant content for the <code>RegexpValidator</code> is the regexp string itself and not other eventual metadata
 * present in the table.
 */
public interface RegexpProjection {
    String getPattern();
}
