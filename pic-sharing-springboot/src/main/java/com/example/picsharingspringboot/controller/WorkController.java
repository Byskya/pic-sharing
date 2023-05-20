package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.*;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.service.WorkService;
import com.example.picsharingspringboot.util.FileDeleteUtil;
import com.example.picsharingspringboot.util.ImageUtils;
import com.example.picsharingspringboot.util.ResponseResult;
import com.example.picsharingspringboot.util.ThumbnailGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081","http://192.168.255.223:8081","http://192.168.255.223:8080"}, allowCredentials = "true")
@RestController
public class WorkController {
    @Autowired
    private WorkService workService;
    //        图片路径
    //    原图
    private final static String basePath = "D:\\Tools\\MyDatabase\\illustrations\\";
    //    缩略图
    private final static String thumbnailBasePath = "D:\\Tools\\MyDatabase\\illustrations\\Thumbnails\\";
//    下架某一个作品
    @PutMapping("/work/updateApproved/{workId}/{approved}")
    public ResponseResult<Void> takeoffWork(@PathVariable("workId") Integer workId,@PathVariable("approved")boolean approved){
        ResponseResult<Void> rr = new ResponseResult<>();
        Illustration illustration = new Illustration();
        illustration.setId(workId);
        illustration.setApproved(approved);
        boolean judge = workService.takeoffWork(illustration);
        if (judge){
            rr.setMessage("下架成功");
            rr.setState(200);
        }
        else {
            rr.setMessage("下架失败");
            rr.setState(500);
        }
        return rr;
    }
//    获取当前登录用户的所有关注者的作品
    @GetMapping("/following/works")
    public ResponseResult<List<Illustration>> getFollowingWorksByUserId(HttpSession session) throws IOException {
        ResponseResult<List<Illustration>> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        if(user!=null){
            Follow follow = new Follow();
            follow.setFollowerId(user.getId());
            List<Illustration> list = workService.getFollowingWorksByUserId(follow);
            for (Illustration illustration : list) {
                byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
                illustration.setImageResource(bytes);
            }
            if (!list.isEmpty()){
                rr.setState(200);
                rr.setMessage("获取关注用户的作品成功");
                rr.setData(list);
            }
            else {
                rr.setState(500);
                rr.setMessage("获取关注用户的作品失败");
                rr.setData(null);
            }
        }
        return rr;
    }
//    作品修改
    @PostMapping("/work/edit")
    @Transactional
    public ResponseResult<Void> illustrationEdit(MultipartHttpServletRequest request) throws IOException {
        ResponseResult<Void> rr = new ResponseResult<>();
//        获取前端发送过来的插画信息
        MultipartFile file = request.getFile("illustration");
        ObjectMapper objectMapper = new ObjectMapper();
        Illustration info = objectMapper.readValue(request.getParameter("workInfo"), Illustration.class);
        Integer[] tagId = objectMapper.readValue(request.getParameter("tags"),Integer[].class);
//        获取没修改前作品的信息
        Illustration returnWork = workService.getReturnWorkById(info.getId());
        String imageUrl = returnWork.getImageUrl();
        String thumbnailUrl = returnWork.getThumbnailUrl();
        boolean b = FileDeleteUtil.deleteFile(new File(imageUrl));
        boolean b2 = FileDeleteUtil.deleteFile(new File(thumbnailUrl));
        if (b && b2){
            assert file != null;
//            保存缩略图和原图到指定文件夹,并在数据库表单中存放图片的位置信息
            String filePath = basePath+System.currentTimeMillis()+"-"+file.getOriginalFilename();
            ImageUtils.saveImage(file.getBytes(),filePath);
            String thumbnailPath = thumbnailBasePath+System.currentTimeMillis()+"-thumbnail-"+file.getOriginalFilename();
            String format = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
            byte[] bytesThumbnail = ThumbnailGenerator.generateThumbnail(file.getBytes(), format);
            ImageUtils.saveImage(bytesThumbnail,thumbnailPath);
            Image image = new com.example.picsharingspringboot.entity.Image();
//            更新图片表单的数据
            image.setId(returnWork.getImageId());
            image.setUrl(filePath);
            image.setThumbnailPath(thumbnailPath);
            boolean judge = workService.editImage(image);
//            删除作品的旧标签映射
            boolean b3 = workService.deleteWorkTags(returnWork.getId());
            if (b3){
                List<MapIllustrationTag> listMap = new ArrayList<>();
                if (judge){
                    int imageId = image.getId();
                    info.setImageId(imageId);
                    info.setImageUrl(filePath);
                    boolean judge2 = workService.editIllustration(info);
                    if (judge2){
                        for (Integer integer : tagId) {
                            listMap.add(new MapIllustrationTag(null, info.getId(), integer));
                        }
                        System.out.println(listMap);
                        boolean judge3 = workService.addWorkTagMap(listMap);
                        if (judge3){
                            rr.setMessage("上传成功");
                            rr.setState(200);
                        }
                        else {
                            rr.setMessage("上传失败");
                            rr.setData(null);
                            rr.setState(500);
                        }
                    }
                    else {
                        rr.setMessage("上传失败");
                        rr.setData(null);
                        rr.setState(500);
                    }
                }
                else {
                    rr.setMessage("上传失败");
                    rr.setData(null);
                    rr.setState(500);
                }
            }
            else {
                rr.setMessage("上传失败");
                rr.setData(null);
                rr.setState(500);
            }
        }
        else {
            rr.setMessage("上传失败");
            rr.setData(null);
            rr.setState(500);
        }
        return rr;
    }
    //获取被退回的作品
    @GetMapping("/work/return/{workId}")
    public ResponseResult<Illustration> getReturnWorkById(@PathVariable("workId")Integer workId) throws IOException {
        ResponseResult<Illustration> rr = new ResponseResult<>();
        Illustration illustration = workService.getReturnWorkById(workId);
        List<IllustrationTag> tags = workService.getWorkTagsById(workId);
        if (illustration!=null){
            byte[] bytes = ImageUtils.readImage(illustration.getImageUrl());
            illustration.setImageResource(bytes);
            illustration.setTags(tags);
            rr.setState(200);
            rr.setMessage("获取退回作品成功");
            rr.setData(illustration);
        }
        else {
            rr.setState(500);
            rr.setMessage("获取退回作品失败");
            rr.setData(null);
        }
        return rr;
    }
//    获取修改作品的信息
    @GetMapping("/work/info/{workId}")
    public ResponseResult<Illustration> getWorkInfoById(@PathVariable("workId")Integer workId) throws IOException {
        ResponseResult<Illustration> rr = new ResponseResult<>();
        Illustration illustration = workService.getWorkInfoById(workId);
        List<IllustrationTag> tags = workService.getWorkTagsById(workId);
        if (illustration!=null){
            byte[] bytes = ImageUtils.readImage(illustration.getImageUrl());
            illustration.setImageResource(bytes);
            illustration.setTags(tags);
            rr.setState(200);
            rr.setMessage("获取要修改作品信息成功");
            rr.setData(illustration);
        }
        else {
            rr.setState(500);
            rr.setMessage("获取要修改的作品信息失败");
            rr.setData(null);
        }
        return rr;
    }
//    提交修改作品后的信息
    @PostMapping("/approvedWork/edit")
    @Transactional
    public ResponseResult<Void> approvedIllustrationEdit(MultipartHttpServletRequest request) throws IOException {
        System.out.println("测试===========================");
        System.out.println("测试===========================");
        System.out.println("测试===========================");
        ResponseResult<Void> rr = new ResponseResult<>();
    //        获取前端发送过来的插画信息
        ObjectMapper objectMapper = new ObjectMapper();
        MultipartFile file = request.getFile("illustration");
        Illustration info = objectMapper.readValue(request.getParameter("workInfo"), Illustration.class);
        Integer[] tagId = objectMapper.readValue(request.getParameter("tags"),Integer[].class);
        System.out.println(Arrays.toString(tagId) +"===========================");
    //        获取没修改前作品的信息
        Illustration returnWork = workService.getWorkInfoById(info.getId());
        String imageUrl = returnWork.getImageUrl();
        String thumbnailUrl = returnWork.getThumbnailUrl();
        boolean b = FileDeleteUtil.deleteFile(new File(imageUrl));
        boolean b2 = FileDeleteUtil.deleteFile(new File(thumbnailUrl));
        if (b && b2){
            assert file != null;
    //            保存缩略图和原图到指定文件夹,并在数据库表单中存放图片的位置信息
            String filePath = basePath+System.currentTimeMillis()+"-"+file.getOriginalFilename();
            ImageUtils.saveImage(file.getBytes(),filePath);
            String thumbnailPath = thumbnailBasePath+System.currentTimeMillis()+"-thumbnail-"+file.getOriginalFilename();
            String format = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
            byte[] bytesThumbnail = ThumbnailGenerator.generateThumbnail(file.getBytes(), format);
            ImageUtils.saveImage(bytesThumbnail,thumbnailPath);
            Image image = new com.example.picsharingspringboot.entity.Image();
    //            更新图片表单的数据
            image.setId(returnWork.getImageId());
            image.setUrl(filePath);
            image.setThumbnailPath(thumbnailPath);
            boolean judge = workService.editImage(image);
    //            删除作品的旧标签映射
            boolean b3 = workService.deleteWorkTags(returnWork.getId());
            if (b3){
                List<MapIllustrationTag> listMap = new ArrayList<>();
                if (judge){
                    int imageId = image.getId();
                    info.setImageId(imageId);
                    info.setImageUrl(filePath);
                    System.out.println(info+"=================================");
                    boolean judge2 = workService.editIllustration(info);
                    if (judge2){
                        for (Integer integer : tagId) {
                            listMap.add(new MapIllustrationTag(null, info.getId(), integer));
                        }
                        System.out.println(listMap);
                        boolean judge3 = workService.addWorkTagMap(listMap);
                        if (judge3){
                            rr.setMessage("作品内容修改成功");
                            rr.setState(200);
                        }
                        else {
                            rr.setMessage("作品内容修改失败");
                            rr.setData(null);
                            rr.setState(500);
                        }
                    }
                    else {
                        rr.setMessage("作品内容修改失败");
                        rr.setData(null);
                        rr.setState(500);
                    }
                }
                else {
                    rr.setMessage("作品内容修改失败");
                    rr.setData(null);
                    rr.setState(500);
                }
            }
            else {
                rr.setMessage("作品内容修改失败");
                rr.setData(null);
                rr.setState(500);
            }
        }
        else {
            rr.setMessage("作品内容修改失败");
            rr.setData(null);
            rr.setState(500);
        }
        return rr;
    }
    //根据关键词搜索作品
    @GetMapping("/search/work/{keyword}/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> searchWork(@PathVariable("keyword") String keyword,@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize") Integer pageSize) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.searchWorkByKeyword(keyword);
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setMessage("搜索成功");
            rr.setData(pageInfo);
            rr.setState(200);
        }
        else {
            rr.setMessage("搜索失败");
            rr.setData(null);
            rr.setState(500);
        }
        return rr;
    }
//    删除当前用户的指定的收藏作品
    @PostMapping("/collection/delete/{workId}")
    public ResponseResult<Void> deleteUserCollectionById(@PathVariable("workId")Integer workId,HttpSession session){
        ResponseResult<Void> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        Favorite favorite = new Favorite();
        favorite.setUserId(user.getId());
        favorite.setIllustrationId(workId);
        boolean judge= workService.deleteCollect(favorite);
        if (judge){
            rr.setState(200);
            rr.setMessage("收藏删除成功");
        }
        else {
            rr.setState(500);
            rr.setMessage("收藏删除失败");
        }
        return rr;
    }
//    获取当前用户的收藏作品
    @GetMapping("/work/collection/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> getUserCollection(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize") Integer pageSize,HttpSession session) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        User user = (User) session.getAttribute("user");
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getUserCollection(user);
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setData(pageInfo);
            rr.setMessage("获取用户收藏列表成功");
            rr.setState(200);
        }
        else {
            rr.setData(pageInfo);
            rr.setMessage("获取用户搜藏列表失败");
            rr.setState(500);
        }
        return rr;
    }
//    获取作品的审核进度
    @GetMapping("/work/timeline/{workId}")
    public ResponseResult<Map<String,Object>> getWorkTimeline(@PathVariable("workId") Integer workId){
        ResponseResult<Map<String,Object>> rr = new ResponseResult<>();
        System.out.println("test================================");
        Illustration illustration = new Illustration();
        illustration.setId(workId);
        illustration = workService.getIllustrationById(illustration);
        AuditInfo auditInfo = workService.getAuditInfoByWorkId(workId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("illustration",illustration);
        map.put("auditInfo",auditInfo);
        if (illustration!=null){
            rr.setData(map);
            rr.setMessage("审核信息加载成功");
            rr.setState(200);
        }
        else {
            rr.setData(null);
            rr.setMessage("审核信息加载失败");
            rr.setState(500);
        }
        return rr;
    }
//    审核作品通过
    @PostMapping("/review/pass/{workId}/{userId}")
    @Transactional
    ResponseResult<Illustration> reviewPass(@PathVariable("workId") Integer workId,@PathVariable("userId") Integer userId){
        ResponseResult<Illustration> rr = new ResponseResult<>();
        Illustration illustration = new Illustration();
        illustration.setId(workId);
        illustration.setUserId(userId);
        illustration.setApproved(true);

        boolean judge = workService.reviewPass(illustration);
        if (judge){

            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setUserId(userId);
            auditInfo.setIllustrationId(workId);
            auditInfo.setStatus("通过");
            boolean judge2 =  workService.updateAuditTable(auditInfo);

            if (judge2){

                rr.setState(200);
                rr.setMessage("审核通过");
                rr.setData(illustration);
            }
            else {

                rr.setState(500);
                rr.setMessage("审核未通过");
                rr.setData(null);
            }
        }
        else {
            rr.setState(500);
            rr.setMessage("审核未通过");
            rr.setData(null);
        }
        return rr;
    }
//    作品不通过
    @PostMapping("/review/notPass/{workId}/{userId}/{feedback}")
    public ResponseResult<AuditInfo> reviewNotPass(@PathVariable("workId")Integer workId,@PathVariable("userId") Integer userId,@PathVariable("feedback") String feedback){
        ResponseResult<AuditInfo> rr = new ResponseResult<>();
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setUserId(userId);
        auditInfo.setIllustrationId(workId);
        auditInfo.setStatus("不通过");
        auditInfo.setFeedback(feedback);
        System.out.println(auditInfo);
        boolean judge =  workService.updateAuditTable(auditInfo);
        if (judge){
            rr.setState(200);
            rr.setMessage("审核退回成功");
            rr.setData(auditInfo);
        }
        else {

            rr.setState(500);
            rr.setMessage("审核退回失败");
            rr.setData(null);
        }
        return rr;
    }
//    获取未审核作品
    @GetMapping("/work/notApproved/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> getNotApprovedWorks(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize") Integer pageSize) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getNotApprovedWorks();
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setData(pageInfo);
            rr.setMessage("加载未审核作品成功");
            rr.setState(200);
        }
        else {
            rr.setData(pageInfo);
            rr.setMessage("加载未审核作品失败");
            rr.setState(500);
        }
        return rr;
    }
//    判断作品是否收藏
    @GetMapping("/check/isCollect/{workId}")
    public ResponseResult<Favorite> checkCollect(@PathVariable("workId") Integer workId,HttpSession session){
        ResponseResult<Favorite> rr = new ResponseResult<>();
        Favorite favorite = new Favorite();
        favorite.setIllustrationId(workId);
        User user = (User)session.getAttribute("user");
        favorite.setUserId(user.getId());
        Favorite realHas = workService.checkCollect(favorite);
        if (realHas!=null){
            rr.setMessage("作品已收藏");
            rr.setState(200);
            rr.setData(favorite);
        }
        else {
            rr.setState(500);
            rr.setMessage("作品未收藏");
            rr.setData(null);
        }
        return rr;
    }
//    判断作品是否点赞
    @GetMapping("/check/isLike/{workId}")
    public ResponseResult<Like> checkLikes(@PathVariable("workId") Integer workId,HttpSession session){
        ResponseResult<Like> rr = new ResponseResult<>();
        Like like = new Like();
        like.setIllustrationId(workId);
        User user = (User)session.getAttribute("user");
        like.setUserId(user.getId());
        Favorite realHas = workService.checkLikes(like);
        if (realHas!=null){
            rr.setMessage("作品点赞");
            rr.setState(200);
            rr.setData(like);
        }
        else {
            rr.setState(500);
            rr.setMessage("作品未点赞");
            rr.setData(null);
        }
        return rr;
    }
    //收藏作品
    @PostMapping("/collect/{workId}")
    public ResponseResult<Favorite> collectWork(@PathVariable("workId") Integer workId,HttpSession session){
        ResponseResult<Favorite> rr = new ResponseResult<>();
        Favorite favorite = new Favorite();
        favorite.setIllustrationId(workId);
        User user = (User)session.getAttribute("user");
        favorite.setUserId(user.getId());
        boolean judge = workService.collectWork(favorite);
        if (judge){
            rr.setMessage("收藏成功");
            rr.setState(200);
            rr.setData(favorite);
        }
        else {
            rr.setData(favorite);
            rr.setState(500);
            rr.setMessage("收藏失败");
        }
        return rr;
    }
    //取消收藏
    @PostMapping("/delete/collect/{workId}")
    @Transactional
    public ResponseResult<Favorite> deleteCollect(@PathVariable("workId") Integer workId,HttpSession session){
        ResponseResult<Favorite> rr = new ResponseResult<>();
        Favorite favorite = new Favorite();
        favorite.setIllustrationId(workId);
        User user = (User)session.getAttribute("user");
        favorite.setUserId(user.getId());
        boolean judge = workService.deleteCollect(favorite);
        if (judge){
            rr.setMessage("取消收藏成功");
            rr.setState(200);
            rr.setData(favorite);
        }
        else {
            rr.setData(favorite);
            rr.setState(500);
            rr.setMessage("取消收藏失败");
        }
        return rr;
    }
    //取消点赞
    @PostMapping("/delete/like/{workId}")
    @Transactional
    public ResponseResult<Like> deleteLike(@PathVariable("workId") Integer workId,HttpSession session){
        ResponseResult<Like> rr = new ResponseResult<>();
        Like like = new Like();
        like.setIllustrationId(workId);
        User user = (User)session.getAttribute("user");
        like.setUserId(user.getId());
        boolean judge = workService.deleteLike(like);
        if (judge){
            Illustration illustration = new Illustration();
            illustration.setId(workId);
            illustration = workService.getIllustrationById(illustration);
            illustration.setLikes(illustration.getLikes()-1);
            boolean judge2 = workService.upWorkLikes(illustration);
            if (judge2){
                rr.setMessage("取消点赞成功");
                rr.setState(200);
                rr.setData(like);
                return rr;
            }
        }
        rr.setData(null);
        rr.setState(500);
        rr.setMessage("取消点赞失败");
        return rr;
    }
//    浏览数计算
    @PostMapping("/up/views/{workId}")
    public ResponseResult<Illustration> upWorkViews(@PathVariable("workId") Integer workId){
        ResponseResult<Illustration> rr = new ResponseResult<>();
        Illustration illustration = new Illustration();
        illustration.setId(workId);
        illustration = workService.getIllustrationById(illustration);
        illustration.setViews(illustration.getViews()+1);
        boolean judge = workService.upWorkViews(illustration);
        if (judge){
            rr.setData(illustration);
            rr.setMessage("浏览数添加成功");
            rr.setState(200);
        }
        else {
            rr.setState(500);
            rr.setData(null);
            rr.setMessage("浏览数添加失败");
        }
        return rr;
    }
    //    点赞数
    @PostMapping("/up/likes/{workId}")
    @Transactional
    public ResponseResult<Like> upWorkLikes(@PathVariable("workId") Integer workId,HttpSession session){
        ResponseResult<Like> rr = new ResponseResult<>();
        Like like = new Like();
        like.setIllustrationId(workId);
        User user = (User)session.getAttribute("user");
        like.setUserId(user.getId());
        boolean judge2 = workService.likeWork(like);
        if (judge2){
            Illustration illustration = new Illustration();
            illustration.setId(workId);
            illustration = workService.getIllustrationById(illustration);
            illustration.setLikes(illustration.getLikes()+1);
            boolean judge = workService.upWorkLikes(illustration);
            if (judge){
                rr.setData(like);
                rr.setMessage("点赞数添加成功");
                rr.setState(200);
                return rr;
            }
        }
        rr.setState(500);
        rr.setData(null);
        rr.setMessage("点赞数添加失败");
        return rr;
    }
//    根据作品id获取作品的信息（图片原图）
    @GetMapping("/work/{workId}")
    public ResponseResult<Illustration> getIllustrationById(@PathVariable("workId") Integer workId) throws IOException {
        ResponseResult<Illustration> rr = new ResponseResult<>();
        Illustration illustration = new Illustration();
        illustration.setId(workId);
        illustration = workService.getIllustrationById(illustration);
        System.out.println(illustration);
        if (illustration!=null){
            byte[] bytes = ImageUtils.readImage(illustration.getImageUrl());
            illustration.setImageResource(bytes);
            rr.setData(illustration);
            rr.setMessage("作品加载成功");
            rr.setState(200);
        }
        else {
            rr.setState(500);
            rr.setMessage("作品加载失败");
            rr.setData(null);
        }
        return rr;
    }
//    获取作品信息缩略图
    @GetMapping("/work/thumbnail/{workId}")
    public ResponseResult<Illustration> getIllustrationThumbnailById(@PathVariable("workId") Integer workId) throws IOException {
        ResponseResult<Illustration> rr = new ResponseResult<>();
        Illustration illustration = new Illustration();
        illustration.setId(workId);
        illustration = workService.getIllustrationThumbnailById(illustration);
        if (illustration!=null){
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
            rr.setData(illustration);
            rr.setMessage("作品索引信息加载成功");
            rr.setState(200);
        }
        else {
            rr.setState(500);
            rr.setMessage("作品索引信息加载失败");
            rr.setData(null);
        }
        return rr;
    }
//    获取作品的评论
    @GetMapping("/work/comments/{workId}")
    public ResponseResult<PageInfo<Comment>> getIllustrationComments(@PathVariable("workId") Integer workId){
        PageHelper.startPage(1,5);
        ResponseResult<PageInfo<Comment>> rr = new ResponseResult<>();
        List<Comment> list = workService.getIllustrationCommentById(workId);
        PageInfo<Comment> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setState(200);
            rr.setMessage("作品评论加载成功");
            rr.setData(pageInfo);
        }
        else {
            rr.setState(500);
            rr.setMessage("评论加载失败");
            rr.setData(null);
        }
        return rr;
    }
//    发送评论
    @PostMapping("work/comment/send/{workId}/{commentContent}")
    public ResponseResult<Comment> sendComment(@PathVariable("workId") Integer workId,@PathVariable("commentContent") String commentContent,HttpSession session){
        ResponseResult<Comment> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        Comment comment = new Comment();
        if (user!=null){
            System.out.println(user);
            comment.setContent(commentContent);
            comment.setUserId(user.getId());
            comment.setIllustrationId(workId);
            boolean judge = workService.addCommentByWorkId(comment);
            if (judge){
                rr.setState(200);
                rr.setMessage("消息发送成功");
                rr.setData(comment);
            }
            else {
                rr.setData(null);
                rr.setMessage("消息发送失败");
                rr.setState(500);
            }
        }
        else {
            System.out.println("用户未登录");
            rr.setData(null);
            rr.setMessage("消息发送失败");
            rr.setState(500);
        }
        return rr;
    }
//    e-upload空间中action 文件上传路径
    @PostMapping("/file/upload")
    public void fileUpload(@RequestParam("file") MultipartFile file){
        System.out.println(file);
    }
//    获取所有插画作品不包括未过审的
    @GetMapping("/work/all/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> getAllIllustration(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getAllIllustration();

        for (Illustration illustration : list) {
            byte[] bytes = new byte[0];
            try {
                bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            } catch (IOException e) {
                bytes = null;
                e.printStackTrace();
            }
            finally {
                illustration.setImageResource(bytes);
            }
        }
        PageInfo<Illustration> illustrationPageInfo = new PageInfo<Illustration>(list);
        if (!list.isEmpty()){
            rr.setData(illustrationPageInfo);
            rr.setState(200);
            rr.setMessage("加载成功");
        }
        else{
            rr.setData(null);
            rr.setMessage("加载失败");
            rr.setState(500);
        }
        return rr;
    }
    //获取所有作品包括未过审的
    @GetMapping("/work/all/includeUnapproved/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> getAllIllustrationIncludeUnapproved(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getAllIllustrationIncludeUnapproved();
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> pageInfo = new PageInfo<Illustration>(list);
        if (!list.isEmpty()){
            rr.setData(pageInfo);
            rr.setState(200);
            rr.setMessage("获取所有作品成功");
        }
        else{
            rr.setData(null);
            rr.setMessage("获取所有作品失败");
            rr.setState(500);
        }
        return rr;
    }

//    获取某张图片资源
    @GetMapping("/get/image/{imageId}")
    public ResponseResult<byte[]> getFile(@PathVariable("imageId") Integer imageId) throws IOException {
        ResponseResult<byte[]> rr = new ResponseResult<>();
        Image image = workService.getImageById(imageId);
        if (image != null){
            System.out.println(image.getUrl());
            byte[] bytes = ImageUtils.readImage(image.getUrl());
            rr.setMessage("获取成功");
            rr.setData(bytes);
            rr.setState(200);
        }
        else {
            rr.setData(null);
            rr.setMessage("获取失败");
            rr.setState(500);
        }
        return rr;
    }
//    获取当前页面作品的标签
    @GetMapping("/work/tags/{workId}")
    public ResponseResult<List<IllustrationTag>> getWorkTags(@PathVariable("workId")Integer workId){
        ResponseResult<List<IllustrationTag>> rr = new ResponseResult<>();
        List<IllustrationTag> tags = workService.getWorkTagsById(workId);
        if (!tags.isEmpty()){
            rr.setState(200);
            rr.setMessage("加载成功");
            rr.setData(tags);
        }
        else {
            rr.setState(500);
            rr.setMessage("加载失败");
            rr.setData(null);
        }
        return rr;
    }
//    获取当前登录用户的所有作品
    @GetMapping("user/allIllustration/{pageNum}/{pageSize}")
    @Transactional
    public ResponseResult<PageInfo<Illustration>> getUserAllIllustrations(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize, HttpSession session) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        User user = (User)session.getAttribute("user");
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getUserAllIllustration(user.getId());
        for (Illustration illustration : list) {
            byte[] bytes = new byte[0];
            try {
                bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            } catch (IOException e) {
                bytes=null;
                e.printStackTrace();
            } finally {
                illustration.setImageResource(bytes);
            }
        }
        PageInfo<Illustration> illustrationPageInfo = new PageInfo<Illustration>(list);
        if (!list.isEmpty()){
            rr.setState(200);
            rr.setMessage("加载登录用户的所有作品成功");
            rr.setData(illustrationPageInfo);
        }
        else {
            rr.setState(500);
            rr.setMessage("加载登录用户的所有作品失败");
            rr.setData(null);
        }
        return rr;
    }
//    根据用户id获取用户的所有作品,但不包括未过审的
    @GetMapping("/user/work/{userId}/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> getUserWorkById(@PathVariable("userId") Integer userId,@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getUserAllIllustrationExceptUnapproved(userId);
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setState(200);
            rr.setMessage("用户作品加载成功");
            rr.setData(pageInfo);
        }
        else {
            rr.setState(500);
            rr.setMessage("用户作品加载失败");
            rr.setData(null);
        }
        return rr;
    }
//    获取所有标点分类
    @GetMapping("/work/tags")
    public ResponseResult<List<IllustrationTag>> getAllTags(){
        ResponseResult<List<IllustrationTag>> rr = new ResponseResult<>();
        List<IllustrationTag> tags = workService.getAllTags();
        if (tags!=null){
            rr.setData(tags);
            rr.setMessage("获取成功");
            rr.setState(200);
        }
        else {
            rr.setData(null);
            rr.setMessage("获取失败");
            rr.setState(500);
        }
        return rr;
    }

//    作品上传
    @PostMapping("/work/upload")
    @Transactional
    public ResponseResult<Illustration> illustrationUpload(MultipartHttpServletRequest request) throws IOException {
        ResponseResult<Illustration> rr = new ResponseResult<>();
//        读取请求发送过来的数据
        MultipartFile file = request.getFile("illustration");
        ObjectMapper objectMapper = new ObjectMapper();
        Illustration info = objectMapper.readValue(request.getParameter("workInfo"), Illustration.class);
        Integer[] tagId = objectMapper.readValue(request.getParameter("tags"),Integer[].class);
        System.out.println(Arrays.toString(tagId));
//        把图片的原图，缩略图数据保存到指定位置
//        原图
        assert file != null;
        String filePath = basePath+System.currentTimeMillis()+"-"+file.getOriginalFilename();
        ImageUtils.saveImage(file.getBytes(),filePath);
//        缩略图
        String thumbnailPath = thumbnailBasePath+System.currentTimeMillis()+"-thumbnail-"+file.getOriginalFilename();
        String format = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        byte[] bytesThumbnail = ThumbnailGenerator.generateThumbnail(file.getBytes(), format);
        ImageUtils.saveImage(bytesThumbnail,thumbnailPath);
//        通过实体类image把图片资源的路径保存到数据库中
        Image image = new com.example.picsharingspringboot.entity.Image();
        image.setUrl(filePath);
        image.setThumbnailPath(thumbnailPath);
        boolean judge = workService.uploadImage(image);
        List<MapIllustrationTag> listMap = new ArrayList<>();
        if (judge){
            int imageId = image.getId();
            info.setImageId(imageId);
            info.setImageUrl(filePath);
            boolean judge2 = workService.uploadIllustration(info);
            if (judge2){
                for (Integer integer : tagId) {
                    listMap.add(new MapIllustrationTag(null, info.getId(), integer));
                }
                System.out.println(listMap);
                //多行数据插入
                boolean judge3 = workService.addWorkTagMap(listMap);
                if (judge3){
                    AuditInfo auditInfo = new AuditInfo();
                    auditInfo.setUserId(info.getUserId());
                    auditInfo.setIllustrationId(info.getId());
                    auditInfo.setStatus("待审核");
                    boolean judge4 = workService.addAuditInfo(auditInfo);
                    if (judge4){
                        rr.setMessage("上传成功");
                        rr.setData(info);
                        rr.setState(200);
                    }
                    else {
                        rr.setMessage("上传失败");
                        rr.setData(null);
                        rr.setState(500);
                    }
                }
                else {
                    rr.setMessage("上传失败");
                    rr.setData(null);
                    rr.setState(500);
                }
            }
            else {
                rr.setMessage("上传失败");
                rr.setData(null);
                rr.setState(500);
            }
        }
        else {
            rr.setMessage("上传失败");
            rr.setData(null);
            rr.setState(500);
        }
        return rr;
    }
    //根据标签获取作品
    @GetMapping("/illustration/bytag/{tagId}/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<Illustration>> getIllustrationByTag(@PathVariable("tagId")Integer tagId,@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<Illustration>> rr= new ResponseResult<>();
        IllustrationTag illustrationTag = new IllustrationTag();
        illustrationTag.setId(tagId);
        List<Illustration> list = workService.getIllustrationByTag(illustrationTag);
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setState(200);
            rr.setData(pageInfo);
            rr.setMessage("根据标签获取作品成功");
        }
        else {
            rr.setState(500);
            rr.setData(null);
            rr.setMessage("根据标签获取作品失败");
        }
        return rr;
    }
    //获取tio10的作品
    @GetMapping("/rank/works")
    public ResponseResult<List<Illustration>> getRankWorks() throws IOException {
        ResponseResult<List<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getRankWorks();
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getThumbnailUrl());
            illustration.setImageResource(bytes);
        }
        if (!list.isEmpty()){
            rr.setMessage("获取前10名作品成功");
            rr.setState(200);
            rr.setData(list);
        }
        else {
            rr.setMessage("获取前10名作品失败");
            rr.setState(500);
            rr.setData(null);
        }
        return rr;
    }
//    =============标签的增删改查====================
    @PostMapping("/add/workTag/{tagName}")
    public ResponseResult<Void> addWorkTag(@PathVariable("tagName")String tagName){
        ResponseResult<Void> rr = new ResponseResult<>();
        IllustrationTag illustrationTag = new IllustrationTag();
        illustrationTag.setTagName(tagName);
        boolean judge = workService.addWorkTag(illustrationTag);
        if (judge){
            rr.setState(200);
            rr.setMessage("标签添加成功");
        }
        else {
            rr.setState(500);
            rr.setMessage("标签添加失败");
        }
        return rr;
    }
    @PutMapping("/edit/tag/{tagId}/{tagName}")
    public ResponseResult<Void> editWorkTag(@PathVariable("tagId")Integer tagId,@PathVariable("tagName")String tagName){
        ResponseResult<Void> rr = new ResponseResult<>();
        IllustrationTag illustrationTag = new IllustrationTag();
        illustrationTag.setId(tagId);
        illustrationTag.setTagName(tagName);
        boolean judge = workService.editWorkTag(illustrationTag);
        if (judge){
            rr.setState(200);
            rr.setMessage("标签修改成功");
        }
        else {
            rr.setState(500);
            rr.setMessage("标签修改失败");
        }
        return rr;
    }
//    删除指定标签
    @DeleteMapping("/delete/tag/{tagId}")
    public ResponseResult<Void> deleteWorkTag(@PathVariable("tagId")Integer tagId){
        ResponseResult<Void> rr = new ResponseResult<>();
        IllustrationTag illustrationTag = new IllustrationTag();
        illustrationTag.setId(tagId);
        boolean judge = workService.deleteWorkTagById(illustrationTag);
        if (judge){
            rr.setState(200);
            rr.setMessage("标签删除成功");
        }
        else {
            rr.setState(500);
            rr.setMessage("标签删除失败");
        }
        return rr;
    }
//    删除用户的指定作品
    @DeleteMapping("/delete/work/{workId}")
    @Transactional
    public ResponseResult<Void> deleteUserIllustration(@PathVariable("workId")Integer workId,HttpSession session){
        ResponseResult<Void> rr= new ResponseResult<>();
        Illustration illustration = new Illustration();
        User user = (User) session.getAttribute("user");
        illustration.setId(workId);
        illustration.setUserId(user.getId());
        //        获取没修改前作品的信息
        //        删除本地硬盘的图片
        Illustration workInfo = workService.getWorkInfoById(illustration.getId());
        String imageUrl = workInfo.getImageUrl();
        String thumbnailUrl = workInfo.getThumbnailUrl();
        boolean b = FileDeleteUtil.deleteFile(new File(imageUrl));
        boolean b2 = FileDeleteUtil.deleteFile(new File(thumbnailUrl));
        if (b && b2){
            boolean judge = workService.deleteWorkById(illustration);
            if (judge){
                boolean judge2 = workService.deleteImageTableDataById(workId);
                if (judge2){
                    rr.setMessage("删除作品成功");
                    rr.setState(200);
                    return rr;
                }
            }
        }
        rr.setState(500);
        rr.setMessage("删除作品失败");
        return rr;
    }
//    记录当前用户的浏览记录
    @PostMapping("/user/watch/history/{workId}")
    public ResponseResult<Void> recordWatchHistory(@PathVariable("workId")Integer workId,HttpSession session){
        ResponseResult<Void> rr = new ResponseResult<>();
        UserHistory userHistory = new UserHistory();
        User user = (User) session.getAttribute("user");
        userHistory.setIllustrationId(workId);
        userHistory.setUserId(user.getId());
        UserHistory history = workService.findUserHistory(userHistory);
        if (history!=null){
            boolean judge3 = workService.updateUserHistory(history);
            if (judge3){
                rr.setMessage("浏览记录更新成功");
                rr.setState(200);
            }
            else {
                rr.setState(500);
                rr.setMessage("浏览记录更新失败");
            }
        }
        else {
            boolean judge2 = workService.recordWatchHistory(userHistory);
            if (judge2){
                rr.setMessage("浏览记录添加成功");
                rr.setState(200);
            }
            else {
                rr.setMessage("浏览记录添加失败");
                rr.setState(500);
            }
        }
        return rr;
    }
//    获取当前用户的浏览记录
    @GetMapping("/get/user/watchHistory")
    public ResponseResult<List<UserHistory>> getUserWatchHistory(HttpSession session){
        ResponseResult<List<UserHistory>> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        List<UserHistory> list= workService.getUserWatchHistory(user.getId());
        for (UserHistory userHistory : list) {
            Illustration workInfoById = workService.getWorkInfoById(userHistory.getIllustrationId());
            userHistory.setAuthorId(workInfoById.getUserId());
        }
        if (!list.isEmpty()){
            rr.setMessage("获取当前用户的历史记录成功");
            rr.setData(list);
            rr.setState(200);
        }
        else {
            rr.setState(500);
            rr.setMessage("获取当前用户的历史记录失败");
            rr.setData(null);
        }
        return rr;
    }
    //删除指定作品的浏览记录
    @DeleteMapping("/delete/work/history/{workId}")
    public ResponseResult<Void> deleteUserWatchWorkHistory(@PathVariable("workId")Integer workId,HttpSession session){
        ResponseResult<Void> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        UserHistory history = new UserHistory();
        history.setUserId(user.getId());
        history.setIllustrationId(workId);
        boolean judge = workService.deleteUserWatchWorkHistory(history);
        if (judge){
            rr.setState(200);
            rr.setMessage("删除指定作品的浏览记录成功");
        }
        else {
            rr.setState(500);
            rr.setMessage("删除指定作品的浏览记录失败");
        }
        return rr;
    }
    //清空用户的所有浏览记录
    @DeleteMapping("/delete/user/allHistory")
    public ResponseResult<Void> deleteUserAllHistory(HttpSession session){
        ResponseResult<Void> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        boolean judge = workService.deleteUserAllHistory(user.getId());
        if (judge){
            rr.setState(200);
            rr.setMessage("当前用户的所有浏览记录清空成功");
        }
        else {
            rr.setState(500);
            rr.setMessage("当前用户的所有浏览记录清空失败");
        }
        return rr;
    }
}
