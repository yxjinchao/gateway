package com.landongnet.gateway.enhance.mapper;


import com.landongnet.gateway.enhance.entity.RouteLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * @author jc
 */
@Repository
public interface RouteLogMapper extends ReactiveMongoRepository<RouteLog, String> {

  /**
   * 删除路由日志
   *
   * @param ids 路由日志id
   * @return 被删除的路由日志
   */
  Flux<RouteLog> deleteByIdIn(Collection<String> ids);
}
