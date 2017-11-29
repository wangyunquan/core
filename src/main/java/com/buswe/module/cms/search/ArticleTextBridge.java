package com.buswe.module.cms.search;

import com.buswe.module.cms.entity.ArticleText;
import org.hibernate.search.bridge.StringBridge;

/**
 * Created by wangy on 2017/6/26.
 */
public class ArticleTextBridge implements StringBridge {

    @Override
    public String objectToString(Object o) {
        ArticleText at = (ArticleText)o;
        return at.getArticleContent();
    }
}