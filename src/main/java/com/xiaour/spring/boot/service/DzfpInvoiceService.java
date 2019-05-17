package com.xiaour.spring.boot.service;

import com.xiaour.spring.boot.core.Result;
import com.xiaour.spring.boot.core.ResultGenerator;
import com.xiaour.spring.boot.entity.DzfpInvoice;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public interface DzfpInvoiceService {

    Result<Map<String, Object>> findfp(DzfpInvoice dzfpInvoice, String kprq, String kjje, String type) ;

}
