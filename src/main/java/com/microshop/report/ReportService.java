package com.microshop.report;

import com.microshop.domain.PageViewAnalysisReport;
import com.microshop.domain.PageViewDaily;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.GET;

/**
 * Created by yqy09_000 on 2016/11/29.
 */
@Service
public class ReportService {
    @Value("${yunsoo.client.di_api}")
    private String diAPIUrl;

    private RestTemplate diAPITemplate;

    public ReportService() {
        diAPITemplate = new RestTemplate();
    }

    public PageViewAnalysisReport getPageViewAnalysisReport(String hostUrl, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) throws UnsupportedEncodingException {
        String query = new StringBuilder(diAPIUrl + "/track/daily_report" + "?")
                .append(String.format("host_url=%s", URLEncoder.encode(hostUrl, "UTF-8"))).append("&")
                .append(String.format("date_from=%s", startTime.toString("yyyy-MM-dd"))).append("&").append(String.format("date_end=%s", endTime.toString("yyyy-MM-dd"))).toString();
        List<PageViewDaily> dailyEntries = diAPITemplate.exchange(query, GET, null, new ParameterizedTypeReference<List<PageViewDaily>>() {
        }).getBody();

        Map<String, PageViewDaily> mapData = dailyEntries.stream().collect(Collectors.toMap(PageViewDaily::getDate, p -> p, (k, v) -> {
            throw new IllegalStateException();
        }, LinkedHashMap::new));

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0)).plusMinutes(10);

        List<Integer> pvList = new ArrayList<>();
        List<Integer> uvList = new ArrayList<>();
        List<String> dateList = new ArrayList<String>();

        do {
            int pv = 0;
            int uv = 0;
            int firstScan = 0;
            String dateString = startDateTime.toString("yyyy-MM-dd");
            if (mapData.containsKey(dateString)) {
                PageViewDaily object = mapData.get(dateString);
                pv = object.getPv();
                uv = object.getUv();
            }
            pvList.add(pv);
            uvList.add(uv);
            dateList.add(dateString);
            startDateTime = startDateTime.plusDays(1);

        } while (startDateTime.isBefore(endDateTime));


        PageViewAnalysisReport report = new PageViewAnalysisReport();
        PageViewAnalysisReport.PVUV pvuv = new PageViewAnalysisReport.PVUV();

        pvuv.setPv(pvList.stream().mapToInt(Integer::intValue).toArray());
        pvuv.setUv(uvList.stream().mapToInt(Integer::intValue).toArray());
        report.setData(pvuv);
        report.setDate(dateList.toArray(new String[0]));
        return report;

    }

    public int[] getPageViewTotal(String hostUrl) throws UnsupportedEncodingException {
        String query = new StringBuilder(diAPIUrl + "/track/page_view_total" + "?")
                .append(String.format("host_url=%s", URLEncoder.encode(hostUrl, "UTF-8"))).toString();
        int[] data = diAPITemplate.getForObject(query, int[].class);
        return data;
    }

}
