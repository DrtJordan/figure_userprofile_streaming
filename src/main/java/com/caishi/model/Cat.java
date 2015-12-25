package com.caishi.model;

/**
 * Created by root on 15-11-19.
 */
public class Cat {
	public int catId;
	public Double weight;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "cat{" +
				"catId=" + catId +
				", weight=" + weight +
				'}';
	}
}
