package com.example.demo.controller;

import com.example.demo.model.Resume;
import com.example.demo.repository.ResumeRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import java.io.File;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeRepository resumeRepository;

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir="uploads/";
            Tika tika = new Tika();
            String content = tika.parseToString(file.getInputStream());
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath =
                    uploadDir + file.getOriginalFilename();

            file.transferTo(new File(filePath));

            int score=calculateScore(content);

            Resume resume = new Resume();
            resume.setFileName(file.getOriginalFilename());
            resume.setContent(content);
            resume.setScore(score);
            resume.setFilePath(filePath);

            resumeRepository.save(resume);

            return "Saved with score: " + score;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @GetMapping("/all")
    @ResponseBody
    public List<Resume> getAllResumes()
    {
    return resumeRepository.findAllByOrderByScoreDesc();
    }
    @GetMapping("/top")
    @ResponseBody
    public Resume getTopResume()
    {
        return resumeRepository.findTopByOrderByScoreDesc();
    }

private int calculateScore(String content) {
        int score = 0;

        content = content.toLowerCase();

        if (content.contains("java")) score += 20;
        if (content.contains("spring")) score += 20;
        if (content.contains("mysql")) score += 20;
        if (content.contains("html")) score += 10;
        if (content.contains("css")) score += 10;
        if (content.contains("javascript")) score += 20;

        return score;
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Resume> resumes =
                resumeRepository.findAllByOrderByScoreDesc();

        model.addAttribute("resumes", resumes);

        return "dashboard";
    }
    @GetMapping("/search")
    public String searchResume(@RequestParam("keyword") String keyword,
                               Model model) {

        List<Resume> resumes =
                resumeRepository.findByFileNameContainingIgnoreCase(keyword);

        model.addAttribute("resumes", resumes);

        return "dashboard";
    }
    @GetMapping("/delete/{id}")
    public String deleteResume(@PathVariable Long id) {

        resumeRepository.deleteById(id);

        return "redirect:/resume/dashboard";
    }
    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadResume(
            @PathVariable Long id) throws Exception {

        Resume resume =
                resumeRepository.findById(id).orElse(null);

        Path path = Paths.get(resume.getFilePath());

        Resource resource =
                new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        if(username.equals("admin")
                && password.equals("admin123")) {

            return "redirect:/resume/dashboard";
        }

        return "login";
    }
    @GetMapping("/")
    public String homePage()
    {
        return "home";
    }
}
