package com.niyamr.pdfchecker.service.impl;

import com.niyamr.pdfchecker.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.niyamr.pdfchecker.exception.PdfProcessingException;
import com.niyamr.pdfchecker.service.PdfService;
import com.niyamr.pdfchecker.service.ValidationService;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of PDF service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final ValidationService validationService;

    @Override
    public String extractText(MultipartFile file) {
        log.info("Extracting text from PDF: {}", file.getOriginalFilename());

        validatePdfFile(file);

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {

            if (document.getNumberOfPages() > AppConstants.MAX_PAGES) {
                throw new PdfProcessingException(
                        String.format("PDF has too many pages: %d (max: %d)",
                                document.getNumberOfPages(), AppConstants.MAX_PAGES)
                );
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            log.info("Successfully extracted {} characters from {} pages",
                    text.length(), document.getNumberOfPages());

            return text;

        } catch (IOException e) {
            log.error("Failed to extract text from PDF", e);
            throw new PdfProcessingException("Failed to extract text from PDF", e);
        }
    }

    @Override
    public int getPageCount(MultipartFile file) {
        log.info("Getting page count for PDF: {}", file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            log.error("Failed to get page count", e);
            throw new PdfProcessingException("Failed to read PDF file", e);
        }
    }

    @Override
    public void validatePdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException(AppConstants.FILE_REQUIRED_MSG);
        }

        String fileName = file.getOriginalFilename();
        if (!validationService.isValidPdfExtension(fileName)) {
            throw new PdfProcessingException(AppConstants.INVALID_FILE_TYPE_MSG);
        }

        if (!validationService.isValidFileSize(file.getSize(), AppConstants.MAX_FILE_SIZE_MB)) {
            throw new PdfProcessingException(AppConstants.FILE_SIZE_EXCEEDED_MSG);
        }

        log.info("PDF file validated successfully: {}", fileName);
    }
}