package edu.sjsu.cmpe.bigdata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shankey on 4/26/14.
 */
public class Sentiment {

    @JsonProperty
    private double[] values;


    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}
