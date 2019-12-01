package com.nathanielpautzke.bullseyeguy.controller;

import com.nathanielpautzke.bullseyeguy.domain.Price;
import com.nathanielpautzke.bullseyeguy.domain.Product;
import com.nathanielpautzke.bullseyeguy.service.BullsEyeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class Web {
    private BullsEyeService bullsEyeService;

    public Web(BullsEyeService bullsEyeService) {
        this.bullsEyeService = bullsEyeService;
    }

    @GetMapping(path = "/products/{id}", produces = "application/json")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {

        return ResponseEntity.ok(bullsEyeService.pullProductInformation(id));
    }

    @PutMapping(path = "/products/{id}", consumes = "application/json")
    public ResponseEntity updateProduct(@PathVariable String id, @RequestBody Price price) {

        bullsEyeService.updatePrice(price, id);

        return ResponseEntity.ok().build();
    }
}
