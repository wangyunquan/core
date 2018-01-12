package com.buswe.module.cms.service;


import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.core.dao.jpa.QueryHelper;
import com.buswe.core.service.BaseServiceImpl;
import com.buswe.core.web.Filterable;
import com.buswe.core.web.MatchType;
import com.buswe.core.web.PropertyFilter;
import com.buswe.module.cms.dao.ArticleDao;
import com.buswe.module.cms.dao.ArticleTextDao;
import com.buswe.module.cms.entity.Article;
import com.buswe.module.cms.entity.ArticleText;
import com.buswe.module.cms.entity.Lable;
import com.buswe.module.cms.utils.CmsUtil;
import com.buswe.module.cms.utils.DownLoadImgUtil;
import javafx.scene.control.Pagination;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.search.FullTextSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;


@Service
@Transactional("jpaTransaction")
public class ArticleServiceImpl  extends BaseServiceImpl<Article> implements ArticleService{

	@Resource
	ArticleDao articleDao;
	@Resource
	ArticleTextDao articleTextDao;
	@Resource
	ResouceService resouceService;
	@Override
	public Article saveArticle(Article entity) {

		return articleDao.save(entity);
	}

	@Override
	public Article getArticle(String id) {
		return articleDao.getOne(id);
	}

	@Override
	public Page<Article> findArticlePage(Pageable page, Filterable filter) {
		Specification<Article> spec= QueryHelper.filterable(filter);
		return articleDao.findAll(spec, page);
	}

	@Override
	public void updateArticlesType(String[] ids, String category) {
		articleDao.updateArticlesType(ids, category, CmsUtil.getCurrentSite());

	}

	@Override
	public void buildIndex() {
		HibernateEntityManager hEntityManager = (HibernateEntityManager)articleDao.getEntityManager() ;
		Session session = hEntityManager.getSession();
		FullTextSession ftSession = org.hibernate.search.Search.getFullTextSession(session);
		List<Article> articles =articleDao.findAll();
		for (Article ar : articles) {
			ftSession.index(ar);
		}
	}

	@Override
	public void fixedTop(String[] ids) {

		  articleDao.fixedTop(true,ids,  CmsUtil.getCurrentSite());
	}

	@Override
	public void cancleFixed(String[] ids) {
		articleDao.fixedTop(false,ids,  CmsUtil.getCurrentSite());

	}

	@Override
	public void setCover(String[] ids, String resKey) {

		articleDao.setCover(ids,  CmsUtil.getCurrentSite(), resKey);
	}

	@Override
	public void downloadArticleImg(String id) {
		Article article=	articleDao.findOne(id);
		ArticleText text=article.getArticleText();
		Document doc = Jsoup.parse(text.getArticleContent());

		Elements es = doc.getElementsByTag("img");
		for (Element img : es) {
			// img.attr("src","res.51so.info");
			String url = img.attr("src").toString();
			if (!url.startsWith(CmsUtil.getCdnhost())) {
				DownLoadImgUtil file = null;
				try {
					file = new DownLoadImgUtil().download(url,  System.getProperty("java.io.tmpdir"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (null != file) {
					//img.attr("src",)
					try {
						String key = resouceService.uploadFile(file.getFilePath(), file.getFileName(), file.getFileType() );
						img.attr("src", String.format("%s/%s", CmsUtil.getCdnhost(), key));
					} catch (Exception e) {
						log.error("downloadArticleImg:", e);
					}
				}
			}
		}
		text.setArticleContent(doc.toString());
		articleTextDao.save(text);
	}

	@Override
	public Page<Article> getArticleList(Pageable pageable, Map<String, Object> args, Sort sort) {
  	List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		if (args.containsKey("category")) {
			filters.add(new PropertyFilter("category.categoryName", MatchType.LIKE,args.get("category")));
		}
		if (args.containsKey("title")) {
			filters.add(new PropertyFilter("title", MatchType.LIKE,args.get("title")));
		}
		if (args.containsKey("categoryId")) {
			filters.add(new PropertyFilter("category.categoryId", MatchType.EQ,args.get("categoryId")));
		}
	return 	articleDao.findAll( QueryHelper.bySearchFilter(filters),
			new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort));

	}


    public Page<Article> getArticleByLable(String lableName, String website, int page, int limit)
    {

        Article article=new Article();
        Lable lable=new Lable();
        lable.setLableName(lableName);
        //TODO 这种查法有待提高效率
        Set<Lable> list=   Collections.emptySet();
        list.add(lable);
        article.setLables(list);
        article.setWebsiteid(website);
        Example<Article> example=Example.of(article);
        return  articleDao.findAll(example,new PageRequest(page,limit));
    }

    public List<Article> getHotArticle(String websiteid,int limit)
    {
        return  articleDao.findByWebsiteidOrderByHitsDesc(websiteid,new PageRequest(0,limit));
    }


    public Pagination getCommentsByArticle(int page, int limit, String articleId, String userid, String model) throws Exception {
//
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("articleid", articleId);
//        params.put("userid", userid);
//        params.put("root", "");
//        String sqlcmd = SqlUtil.getSql("article", "article_comments");
//        StringBuffer sql = new StringBuffer(sqlcmd);
//        if (model.equals("default")) {
//            sql.append(" order by a.c_datetime desc");
//        } else if (model.equals("hot")) {
//            sql.append(" order by a.c_support desc,a.c_datetime desc ");
//        } else if (model.equals("aboutme")) {
//            sql.append(" and (a.c_user_id=:userid or a.c_tosomeone=:userid) ")
//                    .append(" order by a.c_datetime desc");
//        }
//        Pagination p = article_dao.PaginationsBySQL(sql.toString(), page, limit, false, params);
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> lis = (List<Map<String, Object>>) p.getData();
//        List<ArticleCommentVo> rows = new ArrayList<ArticleCommentVo>();
//        for (Map<String, Object> map : lis) {
//            ArticleCommentVo ac = new ArticleCommentVo();
//            ac.setCDatetime((Timestamp) map.get("c_datetime"));
//            ac.setId(map.get("id").toString());
//            String content = map.get("c_text").toString();//评论的内容
//            String html = BlogUtil.markDownToHtml(content);
//            html = BlogUtil.atUserFormat(html);//处理@用户
//            ac.setCText(html.replaceAll("\\<(\\/)?p\\>", ""));//将markdown转成html
//            ac.setCSupport(Integer.valueOf(map.get("c_support").toString()));
//            ac.setCReplyTotal(Integer.valueOf(map.get("reply_total").toString()));
//            //谁评论的
//            UserVo fromU = new UserVo();
//            fromU.setId(map.get("from_user_id").toString());
//            fromU.setUserProfileImg(map.get("user_profile_img").toString());
//            fromU.setUserScreenName(map.get("user_screen_name").toString());
//            fromU.setUserName(map.get("user_name").toString());
//            fromU.setExtendUserUrl(map.get("from_user_url").toString());
//            ac.setCUser(fromU);
//            //@谁
//            if (null != map.get("to_uid")) {
//                UserVo toU = new UserVo();
//                toU.setId(map.get("to_uid").toString());
//                toU.setUserProfileImg(map.get("to_img_s").toString());
//                toU.setUserScreenName(map.get("to_screen_name").toString());
//                toU.setExtendUserUrl(map.get("to_user_url").toString());
//                ac.setCTosomeone(toU);
//            }
//            ac.setExistsSupport(null != map.get("support"));//当前登录用户是否已经点赞过
//            rows.add(ac);
//        }
//        p.setData(rows);
//        return p;

        return null;
    }

	@Override
	public BaseRepository<Article, String> repository() {
		return articleDao;
	}
}
