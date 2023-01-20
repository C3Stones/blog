package com.c3stones.es.repository;

import com.c3stones.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户信息 Repository
 *
 * @author CL
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Long> {
}
