package com.caishi.model;

import java.util.List;

/**
 * Created by root on 15-11-19.
 */
public class CatModel {
	private String _id;
	private List<Cat> catLikes;
	private Long createTime;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public List<Cat> getCatLikes() {
		return catLikes;
	}

	public void setCatLikes(List<Cat> catLikes) {
		this.catLikes = catLikes;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "CatModel{" +
				"_id='" + _id + '\'' +
				", catLikes=" + catLikes +
				", createTime=" + createTime +
				'}';
	}
}

