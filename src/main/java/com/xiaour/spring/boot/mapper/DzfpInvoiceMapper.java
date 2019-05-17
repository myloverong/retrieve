package com.xiaour.spring.boot.mapper;

import com.xiaour.spring.boot.entity.DzfpInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DzfpInvoiceMapper {
    int deleteByPrimaryKey(String id);

    int insert(DzfpInvoice record);

    int insertSelective(DzfpInvoice record);

    DzfpInvoice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DzfpInvoice record);

    int updateByPrimaryKey(DzfpInvoice record);

    DzfpInvoice selectEntityByFpdmAndFphm(DzfpInvoice dzfpInvoice);


}