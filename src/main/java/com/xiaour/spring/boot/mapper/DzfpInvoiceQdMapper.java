package com.xiaour.spring.boot.mapper;


import com.xiaour.spring.boot.entity.DzfpInvoice;
import com.xiaour.spring.boot.entity.DzfpInvoiceQd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.HashMap;
import java.util.List;
@Mapper
public interface DzfpInvoiceQdMapper {
    int deleteByPrimaryKey(String id);

    int insert(DzfpInvoiceQd record);

    int insertSelective(DzfpInvoiceQd record);

    DzfpInvoiceQd selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DzfpInvoiceQd record);

    int updateByPrimaryKey(DzfpInvoiceQd record);

    List<DzfpInvoiceQd> selectEntityByFpdmAndFphm(@Param("fpdm") String fpdm, @Param("fphm") String fphm);
}