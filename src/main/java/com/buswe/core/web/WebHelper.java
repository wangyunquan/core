package com.buswe.core.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

abstract class WebHelper
{
    private static final Pattern MOBILE_USER_AGENT_PATTERN = Pattern.compile(
            "android.+mobile|avantgo|bada|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge|maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)|plucker|pocket|psp|symbian|treo|up.(browser|link)|ucweb|vodafone|wap|webos|windows (ce|phone)|xda|xiino|htc|MQQBrowser",



            2);

    public static boolean mobileRequest(HttpServletRequest request)
    {
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(userAgent)) {
            return false;
        }
        return MOBILE_USER_AGENT_PATTERN.matcher(userAgent).find();
    }

    public static List<PropertyFilter> filterRequest(HttpServletRequest request)
    {
        List<PropertyFilter> filterList = new ArrayList();

        Map<String, Object> filterParamMap = WebUtils.getParametersStartingWith(request, "filter_");
        for (Map.Entry<String, Object> entry : filterParamMap.entrySet())
        {
            String filterName = (String)entry.getKey();
            String value = (String)entry.getValue();
            if (StringUtils.isNotBlank(value))
            {
                String matchTypeStr = StringUtils.substringBefore(filterName, "_");
                String realFilterName = StringUtils.substringAfter(filterName, "_");
                PropertyFilter filter = new PropertyFilter(realFilterName, (MatchType)Enum.valueOf(MatchType.class, matchTypeStr), value);
                request.setAttribute(filterName.replace(".", "_"), value);
                filterList.add(filter);
            }
        }
        return filterList;
    }

    public static HttpServletRequest request()
    {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    public static String getRootUrl()
    {
        HttpServletRequest request = request();
        String protocol = request.getProtocol();
        String ss = request.getServerName();
        Integer ss1 = Integer.valueOf(request.getServerPort());
        String path = request.getContextPath();
        return "http://" + ss + ":" + ss1 + path;
    }
}
