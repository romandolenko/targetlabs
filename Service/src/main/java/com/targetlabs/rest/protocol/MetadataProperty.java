package com.targetlabs.rest.protocol;

import java.io.Serializable;

/**
 * @author Dolenko Roman <roman.dolenko@olfatrade.com> on 15.08.2017.
 */
public class MetadataProperty implements Serializable {

    private String name;

    private String value;

    public MetadataProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
