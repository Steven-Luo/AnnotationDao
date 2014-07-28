package com.cn.naive.sample.model;

import java.text.MessageFormat;

import com.cn.naive.library.dao.annotation.Column;
import com.cn.naive.library.dao.annotation.DataType;
import com.cn.naive.library.dao.annotation.Id;
import com.cn.naive.library.dao.annotation.Table;

@Table(name = "dog")
public class Dog {
	private boolean alive;
	private int id;
	private String name;

	public Dog() {

	}

	public Dog(boolean alive, String name) {
		this.alive = alive;
		this.name = name;
	}

	@Id(name = "id")
	@Column(name = "id", type = DataType.Integer)
	public int getId() {
		return id;
	}

	@Column(name = "name", type = DataType.Varchar, length = 20)
	public String getName() {
		return name;
	}

	@Column(name = "alive", type = DataType.Boolean)
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String format = "id: {0}, Name: {1}, alive: {2}";
		return MessageFormat.format(format, id, name, alive);
	}
}
