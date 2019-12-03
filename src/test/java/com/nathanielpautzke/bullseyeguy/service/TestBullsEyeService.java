package com.nathanielpautzke.bullseyeguy.service;

import com.nathanielpautzke.bullseyeguy.domain.Price;
import com.nathanielpautzke.bullseyeguy.domain.Product;
import com.nathanielpautzke.bullseyeguy.service.BullsEyeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestBullsEyeService {

    @MockBean
    private BullsEyeService bullsEyeService;

    @Before
    public void setup() {
        Price price = Price.builder().value(10.00).build();
        Product product = Product.builder().id("1234").name("Test").price(price).build();
        Mockito.when(bullsEyeService.pullProductInformation("1234"))
                .thenReturn(product);
    }

    @Test
    public void getProductTest() {
        Assert.assertEquals("1234", bullsEyeService.pullProductInformation("1234").getId());
        Assert.assertEquals("Test", bullsEyeService.pullProductInformation("1234").getName());
    }


}
