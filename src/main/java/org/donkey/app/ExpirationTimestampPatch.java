package org.donkey.app;

import javax.json.bind.annotation.JsonbProperty;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ExpirationTimestampPatch {
    @JsonbProperty("expiration_timestamp")
    private String expirationTimestamp;

    public ExpirationTimestampPatch(ZonedDateTime date) {
        this.expirationTimestamp = date.format(DateTimeFormatter.ISO_INSTANT);
    }

    public String getExpirationTimestamp() {
        return expirationTimestamp;
    }
}
