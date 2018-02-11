package com.windforce.common.ramcache.sample.region.basic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.IndexValue;
import com.windforce.common.ramcache.service.RegionCacheService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LoadQueueTest {

	@Inject
	private RegionCacheService<Integer, BasicItem> itemService;
	
	private IndexValue idx = IndexValue.valueOf("owner", 2);

	@Test
	public void test_load() throws InterruptedException {
		Collection<BasicItem> result = itemService.load(idx);
		assertThat(result.size(), is(1));
	}
}
