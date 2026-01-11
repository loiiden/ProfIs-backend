package com.example.profisbackend.service;

import com.example.profisbackend.dto.reports.SwsReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final SpringTemplateEngine templateEngine;

    public byte[] generatePdfSwsReport(SwsReportDTO swsReportDTO) throws IOException {
        Context context = new Context();
        context.setVariable("reportDTO", swsReportDTO);
        String htmlContent = templateEngine.process("reports/SwsReport/SwsReport", context);
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }
}
