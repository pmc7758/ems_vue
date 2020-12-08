package com.pang.service;

import com.pang.entity.Emp;
import com.pang.mapper.EmpDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpDAO empDAO;

    @Override
    public void update(Emp emp) {
        empDAO.update(emp);
    }


    @Override
    public void delete(String id) {
        empDAO.delete(id);
    }

    @Override
    public Emp findOne(String id) {
        return empDAO.findOne(id);

    }


    @Override
    public void save(Emp emp) {
        empDAO.save(emp);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //支持事务，没必要强加事务
    public List<Emp> findAll() {
        return empDAO.findAll();
    }

}
