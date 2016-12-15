package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dake Wang on 2016/2/4.
 *
 *
 * {
 *
 *     date:[...],
 *     data:{pv:[...], uv[...]}
 *
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageViewAnalysisReport {

    @JsonProperty("date")
    private String[] date;

    @JsonProperty("data")
    private PVUV data;

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public PVUV getData() {
        return data;
    }

    public void setData(PVUV data) {
        this.data = data;
    }

    public final static class PVUV
    {
        @JsonProperty("pv")
        private int[] pv;

        @JsonProperty("uv")
        private int[] uv;

        public int[] getPv() {
            return pv;
        }

        public void setPv(int[] pv) {
            this.pv = pv;
        }

        public int[] getUv() {
            return uv;
        }

        public void setUv(int[] uv) {
            this.uv = uv;
        }
    }
}





