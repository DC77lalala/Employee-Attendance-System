package com.wdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.common.ErrorCode;
import com.wdc.exception.BusinessException;
import com.wdc.mapper.EmploymentMapper;
import com.wdc.model.dao.EmploymentRequestDTO;
import com.wdc.model.dao.PostSignInRequestDTO;
import com.wdc.model.dao.UserRegisterDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.service.IEmployService;
import com.wdc.util.DateUtil;
import com.wdc.util.RestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
        queryWrapper.eq("ename", userAccount);
        queryWrapper.eq("password", userPassword);
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

    @Override
    public RestResponse<SignIn> postSignIn(PostSignInRequestDTO postSignInRequestDTO) {
        //获取当前时间
        String today = DateUtil.today();
//        Date dateTime = DateUtil.parse(today, "yyyy-MM-dd");


        return null;
    }

    @Override
    public EmploymentBean update(EmploymentRequestDTO employmentRequestDTO, Long employId) {
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("eid", employId);
        EmploymentBean employmentBean  = employmentMapper.selectOne(queryWrapper);
        if (employmentBean  == null) return null;

        BeanUtils.copyProperties(employmentRequestDTO, employmentBean);

        int updateResult = employmentMapper.updateById(employmentBean);
        if (updateResult > 0) {
            return employmentBean;
        } else {
            return null;
        }


    }

    @Override
    public RestResponse<EmploymentBean> del(Long employId) {
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("eid", employId);
        EmploymentBean employmentBean  = employmentMapper.selectOne(queryWrapper);
        if (employmentBean  == null) return RestResponse.fail("员工不存在");
        int i = employmentMapper.deleteById(employId);
        if (i > 0) {
            return RestResponse.ok(employmentBean);
        } else {
            return RestResponse.fail("删除失败");
        }

    }
}
