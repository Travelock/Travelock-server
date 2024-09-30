package com.travelock.server.controller;

import com.travelock.server.service.CSVImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CSVController {

    private final CSVImportService csvImportService;

    @GetMapping("/import/csv")
    public String importCSV(@RequestParam String filePath) {
        try {
            csvImportService.importCSV(filePath);
            return "CSV 파일이 성공적으로 DB에 삽입되었습니다!";
        } catch (Exception e) {
            return "CSV 파일 삽입 중 오류 발생: " + e.getMessage();
        }
    }
}