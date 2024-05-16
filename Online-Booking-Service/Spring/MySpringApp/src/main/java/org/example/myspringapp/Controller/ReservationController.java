package org.example.myspringapp.Controller;


import org.example.myspringapp.DTO.PDFContentDTO;
import org.example.myspringapp.DTO.ReservationRequestDTO;
import org.example.myspringapp.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReservationController {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    ProductServices productServices;
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;
    @Autowired
    PDFService pdfService;


    /*@PostMapping("/reservation/PDF")
    public Map<String, Object> generatePDF(@RequestBody PDFContentDTO requestDTO) throws IOException {
        //DTO stands for Data Transfer Object
        System.out.println("for testing\n" + requestDTO);
        Map<String, Object> response = new HashMap<>();
        pdfService.generatePdf(requestDTO.getServiceName(),requestDTO.getServicePrice());
        System.out.println(response);
        return response;
    }*/
    @PostMapping("/reservation")
    public Map<String, Object> makeReservation(@RequestBody ReservationRequestDTO requestDTO) {
        //DTO stands for Data Transfer Object
        System.out.println("for testing" + requestDTO);
        Map<String, Object> response = reservationService.makeReservation(requestDTO.getProductName(), requestDTO.getToken());
        System.out.println(response);
        return response;
    }

    @PostMapping("/reservation/PDF")
    public ResponseEntity<byte[]> MakeReservationPdf(@RequestBody ReservationRequestDTO requestDTO) {
        System.out.println("i am here");
        reservationService.makeReservation(requestDTO.getProductName(), requestDTO.getToken());
        try {
            byte[] pdfBytes = pdfService.generatePdf(requestDTO.getProductName(), requestDTO.getPrice());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "generated_pdf.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    public ResponseEntity<byte[]> generatePDF(@RequestBody PDFContentDTO requestDTO) {
        try {
            byte[] pdfBytes = pdfService.generatePdf(requestDTO.getServiceName(), requestDTO.getServicePrice());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "generated_pdf.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}


