package edu.sjsu.cmpe.bigdata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shankey on 4/26/14.
 */
public class Sentiment {

    @JsonProperty
    private int[] values;


    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }
}
