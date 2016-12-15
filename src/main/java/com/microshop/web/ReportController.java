package com.microshop.web;

import com.microshop.domain.PageViewAnalysisReport;
import com.microshop.report.ReportService;
import com.microshop.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Created by yqy09_000 on 2016/11/30.
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/pageview_daily_trend", method = RequestMethod.GET)
    public PageViewAnalysisReport getPageViewDailyReport(@RequestParam(value = "host_url") String hostUrl,
    @RequestParam(value = "date_from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate dateFrom,
    @RequestParam(value = "date_end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate dateEnd,
                                                 HttpServletRequest request) throws UnsupportedEncodingException {
        String openId = AuthUtils.getOpenId(request);
        //TODO permission check 是否有权限访问
        return reportService.getPageViewAnalysisReport(hostUrl,dateFrom, dateEnd);
    }

    @RequestMapping(value = "/pageview_total", method = RequestMethod.GET)
    public int[] getPageViewTotal(@RequestParam(value = "host_url") String hostUrl,
                                                   HttpServletRequest request  ) throws UnsupportedEncodingException {
        String openId = AuthUtils.getOpenId(request);
        //TODO permission check 是否有权限访问
        return reportService.getPageViewTotal(hostUrl);
    }
}
