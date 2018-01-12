package com.buswe.module.cms.dao;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.module.cms.entity.Article;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ArticleDao  extends BaseRepository<Article, String>{
    @Modifying
    @Query("update  Article entity set entity.category.id=:category where entity.id in(:ids) and " +
            " entity.websiteid=:currentSite")
    void updateArticlesType(@Param("ids") String[] ids,
                            @Param("category")String category,
                            @Param("currentSite")String currentSite);


    @Modifying
    @Query("update  Article entity set entity.top=:top where entity.id in(:ids) and " +
            " entity.websiteid=:currentSite")
    void fixedTop(  @Param("top") Boolean top,@Param("ids")String[] ids,
                    @Param("currentSite") String currentSite);

    @Modifying
    @Query("update  Article entity set entity.headpic=:resKey where entity.id in(:ids) and " +
            " entity.websiteid=:currentSite")
    void setCover(@Param("ids")String[] ids, @Param("currentSite")String currentSite, @Param("resKey")String resKey);



    List<Article> findByWebsiteidOrderByHitsDesc(String websiteid, PageRequest pageRequest);
}
