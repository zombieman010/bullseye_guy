package com.nathanielpautzke.bullseyeguy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathanielpautzke.bullseyeguy.domain.Price;
import com.nathanielpautzke.bullseyeguy.domain.Product;
import com.nathanielpautzke.bullseyeguy.service.BullsEyeService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "Get request using product TCIN")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {

        return ResponseEntity.ok(bullsEyeService.pullProductInformation(id));
    }

    @PutMapping(path = "/products/{id}", consumes = "application/json")
    @ApiOperation(value = "Put endpoint accepts a key value with double")
    public ResponseEntity updateProduct(@PathVariable String id, @RequestBody Price price) throws JsonProcessingException {

        if(price.getValue() != 0 && price.getValue() > 0.00)  {
            bullsEyeService.updatePrice(price, id);
            return ResponseEntity.ok().build();
        }else{
            ResultCode response = new ResultCode("Bad Request", "Input is 0. Please check the data type is a double");
            ObjectMapper objectMapper = new ObjectMapper();

            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(response));
        }
    }
}
