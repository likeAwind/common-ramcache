package com.windforce.common.ramcache.enhance;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.ChkUnique;
import com.windforce.common.ramcache.anno.Unique;
import com.windforce.common.ramcache.enhance.Enhance;

@Cached(size = "default")
public class Player implements IEntity<Integer> {

	private Integer id;
	@Unique(query = "Player.name")
	private String name;
	private int gold;
	
	@Enhance("true")
	public boolean increaseGold(int value) {
		if (value <= 0) {
			return false;
		}
		gold += value;
		return true;
	}
	
	@Enhance(ignore = IllegalStateException.class)
	public void charge(int value) throws IllegalStateException {
		if (value <= 0) {
			throw new IllegalArgumentException();
		}
		gold -= value;
		if (gold < 0) {
			throw new IllegalStateException();
		}
	}
	
	@Enhance
	void setName(@ChkUnique("name") String name) {
		this.name = name;
	}

	// Getter And Setter

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getGold() {
		return gold;
	}

	protected void setGold(int gold) {
		this.gold = gold;
	}

	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public boolean serialize() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
