package com.buswe.module.cms.service;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.core.security.SecurityHelper;
import com.buswe.core.service.BaseServiceImpl;
import com.buswe.module.cms.dao.CmsResourceDao;
import com.buswe.module.cms.entity.CmsResource;
import com.buswe.module.cms.utils.CmsUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional("jpaTransaction")
public class ResouceServiceImpl  extends BaseServiceImpl<CmsResource> implements ResouceService {

    @Resource
    CmsResourceDao cmsResourceDao;
    @Resource
    QiNiuManagement qiNiuManagement;

    @Override
    public BaseRepository<CmsResource, String> repository() {
        return cmsResourceDao;
    }

    @Override
    public String uploadFile(String filepath, String filename, String filetype) throws Exception {

        File f = new File(filepath);

        JSONObject json=qiNiuManagement.upload(filepath,null);
        CmsResource cmsResource=new CmsResource();
        cmsResource.setResName(filename);
        cmsResource.setResType(filetype);
        cmsResource.setResKey(json.getString("key"));
        cmsResource.setResHash(json.getString("hash"));
        cmsResource.setResWebsiteid(CmsUtil.getCurrentSite());
        String username=SecurityHelper.getCurrentUserDetails().getUsername();
        cmsResource.setResCreator(username);
        cmsResource.setResDate(new Date());
        cmsResource.setResSize(f.length());
        cmsResourceDao.save(cmsResource);
        f.delete();//删除文件
        return json.getString("key");

    }

    @Override
    public String getResList(int actionCode, int pageindex) throws Exception {

        Pageable inPage=new PageRequest(pageindex,20);
        Specification<CmsResource> spec=  null;
String resouceType="";
        switch (actionCode) {
            case 7://image
                resouceType="image";
                break;
            case 6://file
                resouceType="file";
                break;
        }

        Page<CmsResource>  page=  cmsResourceDao.findCmsResourceByResTypeAndResWebsiteid(resouceType,CmsUtil.getCurrentSite(),inPage);
        List<CmsResource> lis =   page.getContent();

        JSONArray jsons = new JSONArray();
        for (CmsResource res : lis) {
            JSONObject json = new JSONObject();
            json.put("url", res.getResKey());
            json.put("state", "SUCCESS");

            jsons.put(json);
        }
        JSONObject json= new JSONObject();
        json.put("state", "SUCCESS");
        json.put("total", page.getTotalElements());
        json.put("start", pageindex);
        json.put("list", jsons);
        return json.toString();

    }

    @Override
    public Page<CmsResource> getResList(int pageindex, int limit, String resname) throws Exception {

        if(resname==null)
        {
            return cmsResourceDao.findAll(new Specification<CmsResource>() {
                @Override
                public Predicate toPredicate(Root<CmsResource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return  cb.equal( root.get("resWebsiteid"),CmsUtil.getCurrentSite());
                }
            },new PageRequest(pageindex,limit));
        }
 else
     {
         return cmsResourceDao.findAll(new Specification<CmsResource>() {
             @Override
             public Predicate toPredicate(Root<CmsResource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

//TODO
                 return  cb.equal( root.get("resWebsiteid"),CmsUtil.getCurrentSite());
             }
         },new PageRequest(pageindex,limit));

        }


    }

    @Override
    public int delRes(String[] keys) throws Exception {
        String website=CmsUtil.getCurrentSite();
        List<String> lis= cmsResourceDao.findResKeyByResWebsiteidAndResKeyIn(website,keys);
        List<String> res=new ArrayList<String>();
        for (String key : lis) {
            try {
                qiNiuManagement.delRes(key);
                res.add(key);
            } catch (QiniuException e) {
                Response re=e.response;
                if (re.error.equals("no such file or directory")){
                    res.add(key);
                }
                log.error(re.bodyString());
            }
            cmsResourceDao.deleteCmsResourceByResWebsiteidAndResKey(website,key);
        }



        return 0;
    }

    @Override
    public void rename(String id, String name) throws Exception {
        String website=CmsUtil.getCurrentSite();
        CmsResource cmsResource=   cmsResourceDao.findOne(id);
        cmsResource.setResName(name);
        cmsResourceDao.save(cmsResource);

    }
}
