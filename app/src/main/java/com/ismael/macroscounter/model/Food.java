package com.ismael.macroscounter.model;

public class Food {
    private String id;
    private String nombre;
    private String timeOfEating;
    private int grMl;
    private int kcal;
    private int protein;
    private int carbs;
    private int fats;

    public Food() {
    }

    @Override
    public String toString() {
        return nombre;
    }

    public int getGrMl() {
        return grMl;
    }

    public void setGrMl(int grMl) {
        this.grMl = grMl;
    }

    public String getTimeOfEating() {
        return timeOfEating;
    }

    public void setTimeOfEating(String timeOfEating) {
        this.timeOfEating = timeOfEating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }
}
