package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.*;
import com.example.picsharingspringboot.service.WorkService;
import com.example.picsharingspringboot.util.ImageUtils;
import com.example.picsharingspringboot.util.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"}, allowCredentials = "true")
@RestController
public class WorkController {
//    上传作品
    @Autowired
    private WorkService workService;
//    e-upload空间中action 文件上传路径
    @PostMapping("/file/upload")
    public void fileUpload(@RequestParam("file") MultipartFile file){
        System.out.println(file);
    }
//    获取某张图片资源
    @GetMapping("/get/image/{imageId}")
    public ResponseResult<byte[]> getFile(@PathVariable("imageId") Integer imageId) throws IOException {
        ResponseResult<byte[]> rr = new ResponseResult<>();
        System.out.println(imageId+"=====================");
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
            rr.setState(400);
        }
        return rr;
    }
//    获取当前用户的所有作品
    @GetMapping("user/allIllustration/{pageNum}/{pageSize}")
    @Transactional
    public ResponseResult<PageInfo<Illustration>> getUserAllIllustrations(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize, HttpSession session) throws IOException {
        PageHelper.startPage(pageNum,pageSize);
        User user = (User)session.getAttribute("user");
        ResponseResult<PageInfo<Illustration>> rr = new ResponseResult<>();
        List<Illustration> list = workService.getUserAllIllustration(user.getId());
        for (Illustration illustration : list) {
            byte[] bytes = ImageUtils.readImage(illustration.getImageUrl());
            illustration.setImageResource(bytes);
        }
        PageInfo<Illustration> illustrationPageInfo = new PageInfo<Illustration>(list);
        if (list!=null){
            rr.setState(200);
            rr.setMessage("加载成功");
            rr.setData(illustrationPageInfo);
        }
        else {
            rr.setState(400);
            rr.setMessage("加载失败");
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
            rr.setState(400);
        }
        return rr;
    }

//    作品上传
    @PostMapping("/work/upload")
    @Transactional
    public ResponseResult<Illustration> illustrationUpload(MultipartHttpServletRequest request) throws IOException {
        ResponseResult<Illustration> rr = new ResponseResult<>();
//        图片保存位置
        String basePath = "D:\\Tools\\MyDatabase\\illustrations\\";
//        读取请求发送过来的数据
        MultipartFile file = request.getFile("illustration");
        ObjectMapper objectMapper = new ObjectMapper();
        Illustration info = objectMapper.readValue(request.getParameter("workInfo"), Illustration.class);
        Integer[] tagId = objectMapper.readValue(request.getParameter("tags"),Integer[].class);
        System.out.println(Arrays.toString(tagId));
//        把文件数据保存到指定位置
        assert file != null;
        String filePath = basePath+System.currentTimeMillis()+"-"+file.getOriginalFilename();
        ImageUtils.saveImage(file.getBytes(),filePath);
        Image image = new com.example.picsharingspringboot.entity.Image();
        image.setUrl(filePath);
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
                    rr.setMessage("上传成功");
                    rr.setData(info);
                    rr.setState(200);
                }
                else {
                    rr.setMessage("上传失败");
                    rr.setData(null);
                    rr.setState(400);
                }
            }
            else {
                rr.setMessage("上传失败");
                rr.setData(null);
                rr.setState(400);
            }
        }
        else {
            rr.setMessage("上传失败");
            rr.setData(null);
            rr.setState(400);
        }
        return rr;
    }
}
