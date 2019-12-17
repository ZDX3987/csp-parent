package com.zdx.csp.user.mapper;

import com.zdx.csp.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 获取关注和粉丝
     * @param userId
     * @param flag
     * @return
     */
    List<Integer> getFriends(Integer userId, Integer flag);

    /**
     * 检查是否已关注
     * @param from
     * @param to
     * @return
     */
    Integer attentioned(Integer from, Integer to);

    /**
     * 取消关注
     * @param from
     * @param to
     * @return
     */
    Integer unAttention(Integer from, Integer to);

    /**
     * 关注
     * @param from
     * @param to
     * @return
     */
    Integer attention(Integer from, Integer to);

    User findByEmail(String email);

    User findById(Integer id);
    /**
     * 注册
     * @param user
     * @return
     */
    Integer insert(User user);

    /**
     * 登录
     * @return
     */
    User login(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Integer update(User user);

    /**
     * 上传图片
     * @param url
     * @param id
     * @return
     */
    Integer updateUrl(String url, Integer id);

    /**
     * 修改密码
     * @param password
     * @param id
     * @return
     */
    Integer changePassword(String password, String email);
}
