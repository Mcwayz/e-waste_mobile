package com.example.e_waste.model.collections;

import java.util.List;

public class CollectResponse {

    private List<Collection> collections;

    public CollectResponse(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
}
