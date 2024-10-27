package com.carpool.car_pool.services;

public enum EmailTemplate {
    DEFAULT("emailTemplate"),
    RIDE_REQUEST("emailTemplateRideRequest");

    private final String html;

    EmailTemplate(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }
}
