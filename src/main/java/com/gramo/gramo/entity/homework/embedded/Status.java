package com.gramo.gramo.entity.homework.embedded;

import com.gramo.gramo.entity.homework.Homework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Status {

    @Column(nullable = false)
    protected Boolean isSubmitted;

    @Column(nullable = false)
    protected Boolean isRejected;

    public void submitHomework() {
        this.isSubmitted = true;
    }

    public void rejectHomework() {
        this.isRejected = true;
        this.isSubmitted = false;
    }
}
