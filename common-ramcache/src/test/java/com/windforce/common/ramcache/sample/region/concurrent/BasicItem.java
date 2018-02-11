package com.windforce.common.ramcache.sample.region.concurrent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.CacheUnit;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.ChangeIndex;
import com.windforce.common.ramcache.anno.Index;
import com.windforce.common.ramcache.enhance.Enhance;

/**
 * 测试道具实体
 * 
 * @author frank
 */
@Entity
@NamedQueries({ @NamedQuery(name = "BasicItem.owner", query = "from BasicItem t where t.owner = ?") })
@Cached(size = "default", unit = CacheUnit.REGION)
public class BasicItem implements IEntity<Integer> {

	public static BasicItem valueOf(int id, int owner, int amount) {
		BasicItem result = new BasicItem();
		result.id = id;
		result.owner = owner;
		result.amount = amount;
		return result;
	}

	@Id
	private Integer id;
	@Index(query = "BasicItem.owner")
	private int owner;
	private int amount;

	@Enhance
	public void setOwner(@ChangeIndex("owner") int owner) {
		this.owner = owner;
	}

	/**
	 * 添加数量
	 * 
	 * @param value
	 *            增量值
	 * @return true:添加成功;false:添加失败
	 */
	@Enhance("true")
	public boolean increaseAmount(int value) {
		if (value <= 0) {
			return false;
		}
		amount += value;
		return true;
	}

	// Getter and Setter ...

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public int getOwner() {
		return owner;
	}

	public int getAmount() {
		return amount;
	}

	protected void setAmount(int amount) {
		this.amount = amount;
	}

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
		if (!(obj instanceof BasicItem))
			return false;
		BasicItem other = (BasicItem) obj;
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
