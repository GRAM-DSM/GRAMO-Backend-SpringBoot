package com.gramo.gramo.entity.homework.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Builder
@AllArgsConstructor
@Embeddable
public class Status {

    @Column(nullable = false)
    private Boolean isSubmitted;

    @Column(nullable = false)
    private Boolean isRejected;

    public Status() {
        this.isSubmitted = false;
        this.isRejected = false;
    }

    public void submitHomework() {
        this.isSubmitted = true;
        this.isRejected = false;
    }

    public void rejectHomework() {
        this.isRejected = true;
        this.isSubmitted = false;
    }

}
