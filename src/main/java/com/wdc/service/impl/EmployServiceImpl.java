package com.wdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.common.ErrorCode;
import com.wdc.exception.BusinessException;
import com.wdc.mapper.EmploymentMapper;
import com.wdc.model.DTO.UserRegisterDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.UserBean;
import com.wdc.service.IEmployService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * employ 的实现类
 */
@Service
public class EmployServiceImpl extends ServiceImpl<EmploymentMapper, EmploymentBean> implements IEmployService {

    private static final String SALT = "wdc";
    @Resource
    private EmploymentMapper employmentMapper;


    @Override
    public EmploymentBean userRegister(UserRegisterDTO userRegisterRequest, HttpServletRequest request) {

        //1.判断用户是否存在
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        String userAccount = userRegisterRequest.getUloginname();
        String userPassword = userRegisterRequest.getUpassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return null;
        }
        //对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        queryWrapper.eq("ename",userAccount);
        queryWrapper.eq("password",userPassword);
        long count = employmentMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 3. 插入数据
        EmploymentBean employmentBean = new EmploymentBean();
        employmentBean.setEaddr(userAccount);
        employmentBean.setPassword(encryptPassword);
        boolean saveResult = this.save(employmentBean);
        if (!saveResult) {
            return null;
        }
        return employmentBean;
    }
}
