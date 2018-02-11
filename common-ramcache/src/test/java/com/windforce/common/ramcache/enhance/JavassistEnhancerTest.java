package com.windforce.common.ramcache.enhance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.windforce.common.ramcache.enhance.JavassistEntityEnhancer;

public class JavassistEnhancerTest {

	private static MockCacheService cacheService = new MockCacheService();
	private static JavassistEntityEnhancer enhancer;
	
	@BeforeClass
	public static void before() {
		enhancer = new JavassistEntityEnhancer();
		enhancer.initialize(cacheService);
	}
	
	@Test
	public void test() {
		Player player = new Player();
		Player enhanced = enhancer.transform(player);
		assertThat(enhanced, notNullValue());
		
		enhanced.setId(10);
		assertThat(enhanced.getId(), is(10));
		assertThat(player.getId(), is(10));
		
		enhanced.setName("Frank");
		assertThat(enhanced.getName(), is("Frank"));
		assertThat(player.getName(), is("Frank"));
		assertThat(cacheService.getId(), is(10));
		assertThat(cacheService.getEntity(), sameInstance(player));
		cacheService.clear();
	
		assertThat(enhanced.increaseGold(-10), is(false));
		assertThat(player.getGold(), is(0));
		assertThat(cacheService.getId(), nullValue());
		
		assertThat(enhanced.increaseGold(5), is(true));
		assertThat(enhanced.getGold(), is(5));
		assertThat(player.getGold(), is(5));
		assertThat(cacheService.getId(), is(10));
		assertThat(cacheService.getEntity(), sameInstance(player));
		cacheService.clear();

		try {
			enhanced.charge(-10);
			fail();
		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(player.getGold(), is(5));
			assertThat(cacheService.getId(), nullValue());
		}
		
		try {
			enhanced.charge(2);
		} catch (Exception e) {
			fail();
		}
		assertThat(enhanced.getGold(), is(3));
		assertThat(player.getGold(), is(3));
		assertThat(cacheService.getId(), is(10));
		assertThat(cacheService.getEntity(), sameInstance(player));
	}
	
	@Test
	public void test_hashCode() {
		Player player = new Player();
		Player enhanced = enhancer.transform(player);
		assertThat(enhanced.hashCode(), is(player.hashCode()));
	}
	
	@Test
	public void test_equals() {
		Player player = new Player();
		player.setId(1);
		Player enhanced = enhancer.transform(player);
		player = new Player();
		player.setId(1);
		assertThat(enhanced.equals(player), is(true));
		assertThat(player.equals(enhanced), is(true));
	}
	
}
