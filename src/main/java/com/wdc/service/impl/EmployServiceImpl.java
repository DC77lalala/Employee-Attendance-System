package com.wdc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.mapper.EmploymentMapper;
import com.wdc.model.po.EmploymentBean;
import com.wdc.service.IEmployService;
import org.springframework.stereotype.Service;

/**
 * employ 的实现类
 */
@Service
public class EmployServiceImpl extends ServiceImpl<EmploymentMapper, EmploymentBean> implements IEmployService {

}
