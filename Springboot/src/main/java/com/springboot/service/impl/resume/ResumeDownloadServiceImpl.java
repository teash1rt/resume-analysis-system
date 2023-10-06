package com.springboot.service.impl.resume;

import com.springboot.common.R;
import com.springboot.mapper.resume.ResumeMapper;
import com.springboot.service.resume.ResumeDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeDownloadServiceImpl implements ResumeDownloadService {
    private final ResumeMapper resumeMapper;

    @Override
    public R resume_download(Integer rid) {
        String route = resumeMapper.selectById(rid).getRoute();
        String type = route.substring(route.lastIndexOf("."));
        try (FileInputStream inputStream = new FileInputStream(route);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] byteArr = outputStream.toByteArray();
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("data", byteArr);
            return R.success("文件下载准备就绪", map);
        } catch (IOException e) {
            return R.error("文件下载失败");
        }
    }
}
