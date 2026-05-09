package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    @JsonIgnore
    @Column(length = 5000)
    private String content;
    private String filePath;

    private double score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getFileName() {

        return fileName;
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public double getScore() {

        return score;
    }

    public void setScore(double score) {

        this.score = score;
    }
    public String getFilePath()
    {
        return filePath;
    }
    public void setFilePath(String filePath)
    {
        this.filePath=filePath;
    }
}