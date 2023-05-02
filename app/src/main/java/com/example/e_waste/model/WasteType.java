package com.example.e_waste.model;

public class WasteType {

    private  int id;
    private String waste_type;
    private String cost;


    public WasteType() {
    }

    public WasteType(int id, String waste_type, String cost) {
        this.id = id;
        this.waste_type = waste_type;
        this.cost = cost;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaste_type() {
        return waste_type;
    }

    public void setWaste_type(String waste_type) {
        this.waste_type = waste_type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "WasteType{" +
                "id=" + id +
                ", waste_type='" + waste_type + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
