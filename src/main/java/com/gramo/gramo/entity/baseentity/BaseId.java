package com.gramo.gramo.entity.baseentity;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public abstract class BaseId {
    @Id
    @GeneratedValue
    private Long id;
}
