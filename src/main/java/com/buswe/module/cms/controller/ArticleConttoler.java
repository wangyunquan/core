package com.buswe.module.cms.controller;

import com.buswe.core.web.Filterable;
import com.buswe.core.web.WebConstants;
import com.buswe.module.cms.entity.Article;
import com.buswe.module.cms.service.ArticleService;
import com.buswe.module.cms.service.CategoryService;
import com.buswe.module.cms.utils.CmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;


@Controller
@RequestMapping("admin/cms/article")
public class ArticleConttoler {

    @Resource
    ArticleService service;
    @Resource
    CategoryService categoryService;

    @RequestMapping("list")
    public String list(Pageable page, Filterable filter, Model model) {
        model.addAttribute("page", service.findArticlePage(page, filter));
        model.addAttribute("catAll", categoryService.findAll());

        return "/cms/article_list";
    }

    @RequestMapping({"/save"})
    public String save(@Valid Article entity, String articleLableStr, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            try {
                if (StringUtils.isBlank(entity.getId())) {
                    entity.setId(null);
                }
                if (StringUtils.isBlank(articleLableStr)) {
                    entity.setLables(null);
                }
                entity.setWebsiteid(CmsUtil.getCurrentSite());
                service.saveArticle(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:list";
    }

    @RequestMapping({"/input"})
    public String input(String id, Model model) {
        Article entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = service.getArticle(id);
        } else {
            entity = new Article();
        }

        model.addAttribute("catAll", categoryService.findAll());
        model.addAttribute("article", entity);
        return "/cms/article_input";
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public String delete(String[] id) {
        for (String ids : id) {
            this.service.delete(ids);
        }
        return String.format("成功删除%s条记录!", id.length);
    }


    /**
     * @param ids
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping("/update_articletype.do")
    public String updateArticlesType(@RequestParam("ids[]") String[] ids, String category) {
        String msg = WebConstants.RESPONSE_SUCCESS;
        try {

            service.updateArticlesType(ids, category);
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 构建索引
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/build_index.do")
    public String buildIndex() {
        String msg = WebConstants.RESPONSE_SUCCESS;
        try {
            service.buildIndex();

        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 文章置顶
     *
     * @param ids 文章id
     * @return
     */
    @ResponseBody
    @RequestMapping("/fixedTop.do")
    public String fixedTop(@RequestParam("ids[]") String[] ids) {
        String msg = WebConstants.RESPONSE_SUCCESS;
        try {
            service.fixedTop(ids);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 取消置顶
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/cancleFixedTop.do")
    public String cancleFixedTop(@RequestParam("ids[]") String[] ids) {
        String msg = WebConstants.RESPONSE_SUCCESS;
        try {
            service.cancleFixed(ids);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 设置封面
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/setCover.do")
    public String setCover(@RequestParam("ids[]") String[] ids, String resKey) {
        String msg = WebConstants.RESPONSE_SUCCESS;
        try {
            if (null == resKey) {
                throw new Exception("资源key为空，操作失败！");
            }
            service.setCover(ids, resKey);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 外键图片资源转换
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/outsideImgResTransformation.do")
    public String outsideImgResTransformation(String id) {
        String msg = WebConstants.RESPONSE_SUCCESS;
        try {
            service.downloadArticleImg(id);
        } catch (Exception ex) {

            ex.printStackTrace();
            msg = "error";
        }
        return msg;
    }

}
