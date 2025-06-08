package com.MMD.code.DTO;

import java.time.LocalDateTime;

public class MetricAverageResponse {
    private LocalDateTime timestamp;
    private String label;
    private double value;

    public MetricAverageResponse(LocalDateTime timestamp, String label, double value) {
        this.timestamp = timestamp;
        this.label = label;
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
