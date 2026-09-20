package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     */
    @Override
    public void save(AddressBook addressBook) {
        // 获取当前登录用户id
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        // 默认新增地址不是默认地址
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询当前用户地址列表
     */
    @Override
    public List<AddressBook> list() {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = AddressBook.builder()
                .userId(userId)
                .build();
        return addressBookMapper.list(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 修改地址
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 删除地址
     */
    @Override
    public void delete(Long id) {
        addressBookMapper.deleteById(id);
    }

    /**
     * 设置默认地址
     * 事务：先把该用户所有地址改为非默认，再把选中这条设为默认
     */
    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        //1.把当前用户全部地址 is_default置为0
        addressBookMapper.cancelAllDefault(userId);
        //2.把选中地址设为默认1
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * 查询默认地址
     */
    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.getDefault(userId);
    }
}
