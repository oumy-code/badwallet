package com.ism.badwallet.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/external/factures")
@RequiredArgsConstructor
public class ExternalProxyController {

    private final RestTemplate restTemplate;
   
    private final String PAYMENT_SERVICE_URL = "http://localhost:8081/api/external/factures";

    
    @GetMapping("/{walletCode}/current")
    public ResponseEntity<String> getCurrentFactures(
            @PathVariable String walletCode,
            @RequestParam(required = false) String unite) {
        
        String url = PAYMENT_SERVICE_URL + "/" + walletCode + "/current";
        if (unite != null) {
            url += "?unite=" + unite;
        }
        
       
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{walletCode}/periode")
    public ResponseEntity<String> getFacturesByPeriode(
            @PathVariable String walletCode,
            @RequestParam String debut,
            @RequestParam String fin) {
        
        String url = PAYMENT_SERVICE_URL + "/" + walletCode + "/periode?debut=" + debut + "&fin=" + fin;
        
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
}