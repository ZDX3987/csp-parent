package com.zdx.csp.article.feign;

import com.zdx.csp.api.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * User 服务调用客户端
 */
@Component
@FeignClient("user")
public interface UserServiceFeign extends UserService {
}
