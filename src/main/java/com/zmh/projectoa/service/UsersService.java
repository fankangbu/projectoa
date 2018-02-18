package com.zmh.projectoa.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmh.projectoa.mapper.UserinfoMapper;
import com.zmh.projectoa.mapper.UsersMapper;
import com.zmh.projectoa.model.Userinfo;
import com.zmh.projectoa.model.Users;
import com.zmh.projectoa.model.UsersExample;
import com.zmh.projectoa.util.MD5Util;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by ChengShanyunduo
 * 2018/2/14
 */
@Service
public class UsersService {

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    UserinfoMapper userinfoMapper;

    /**
     * 创建职工
     * @param users
     * @return
     */
    public int createUser(Users users){
        String password = MD5Util.string2MD5("123456");
        users.setPassword(password);

        int count = usersMapper.insertSelective(users);

        if (count != 1){
            return 0;
        }

        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andUsernameEqualTo(users.getUsername());
        Users user = queryUserByUsername(users);

        Userinfo userinfo = new Userinfo();
        userinfo.setUserId(user.getId());

        count = userinfoMapper.insertSelective(userinfo);

        return count;
    }

    /**
     * 根据用户名查找用户
     * @param users
     * @return
     */
    public Users queryUserByUsername(Users users){
        return usersMapper.queryUserByUsername(users.getUsername());
    }

    /**
     * 分页查询
     * @param userDto
     * @return
     */
    public PageInfo userList(Users userDto){
        userDto.setIsDel("0");

        PageHelper.startPage(1, 10);
        List<Users> list = usersMapper.queryBySelective(userDto);
        PageInfo page = new PageInfo(list);

        return page;

    }


}