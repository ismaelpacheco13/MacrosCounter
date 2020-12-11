package com.ismael.macroscounter.model;

public class Setting {
    private int height;
    private int weight;
    private String gender;
    private int age;
    private String physicalActivity;
    private String goal;

    private int kcal;
    private int bmr;
    private int protein;
    private int carbs;
    private int fats;

    public Setting() {
    }

    @Override
    public String toString() {
        return "Setting{" +
                "height=" + height +
                ", weight=" + weight +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", physicalActivity='" + physicalActivity + '\'' +
                ", goal='" + goal + '\'' +
                ", kcal=" + kcal +
                ", bmr=" + bmr +
                ", protein=" + protein +
                ", carbs=" + carbs +
                ", fats=" + fats +
                '}';
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(String physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
