package com.example.webbackend.repository.entity.dtos;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

import java.util.HashMap;

public class Resolver extends SimpleObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object ob) {
        if (_items == null) {
            _items = new HashMap<>();
        }
        _items.put(id, ob);
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new Resolver();
    }
}