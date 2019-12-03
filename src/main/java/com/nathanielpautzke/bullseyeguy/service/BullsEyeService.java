package com.nathanielpautzke.bullseyeguy.service;

import com.nathanielpautzke.bullseyeguy.domain.Product;
import com.nathanielpautzke.bullseyeguy.storage.Price;
import com.nathanielpautzke.bullseyeguy.storage.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BullsEyeService {
    private ProductRepository productRepository;
    private RestTemplate restTemplate;
    private static final String NAME = "name";
    private static final String PRICE = "price";

    public BullsEyeService(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Looks for product in the data store. If entry exists, it will return the data store values. If it does not exist
     * a new entry is created in the data store and the product info is returned.
     * @param id
     * @return Product object
     */
    public Product pullProductInformation(String id) {
        Optional<com.nathanielpautzke.bullseyeguy.storage.Product> getProduct = productRepository.findById(id);

        if(getProduct.isPresent()){
            log.info("Data store entry exists. Returning document.");
            return documentToDTO(getProduct.get());
        }else {
            Map<String, String> attributes = getProductAttributes(id);

            Price price = Price.builder().value(Double.parseDouble(attributes.get(PRICE))).build();

            com.nathanielpautzke.bullseyeguy.storage.Product docmentProduct =
                    com.nathanielpautzke.bullseyeguy.storage.Product.builder()
                            .id(id)
                            .name(attributes.get(NAME))
                            .price(price)
                            .build();

            log.info("Saving product document to data store.");
            productRepository.save(docmentProduct);

            return documentToDTO(docmentProduct);
        }
    }

    /**
     * Takes in a price object and updates in the data store in entry is found.
     *
     * @param price
     * @param id
     * @throws ResponseStatusException
     */
    public void updatePrice(com.nathanielpautzke.bullseyeguy.domain.Price price, String id) {
        Optional<com.nathanielpautzke.bullseyeguy.storage.Product> getProduct = productRepository.findById(id);

        if(getProduct.isPresent()) {
            com.nathanielpautzke.bullseyeguy.storage.Product document = getProduct.get();
            document.getPrice().setValue(price.getValue());
            productRepository.save(document);
        }else{
            log.debug("Product ID does not exist in external API.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + id + " was not found in the data store. Unable to update");
        }
    }

    /**
     * Gets JSON response form hardcoded api.
     *
     * @param id
     * @return Map<String,String>
     * @throws HttpServerErrorException
     */
    protected Map<String, String> getProductAttributes(String id) {

        String response = restTemplate.getForObject("https://redsky.target.com/v2/pdp/tcin/" + id, String.class);
        if(!StringUtils.isEmpty(response)){
            return parseJSON(response);
        }else {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "External API not responding.");
        }
    }

    /**
     * Parses response for hardcoded values.
     *
     * @param json
     * @return Map<String, String>
     */
    protected Map<String, String> parseJSON(String json){
        JSONObject jsonObject = new JSONObject(json);
        Map<String, String> productAttributes = new HashMap<>();

        log.info("Parsing API response data...");
        productAttributes.put(NAME, jsonObject.getJSONObject("product").getJSONObject("item").getJSONObject("product_description").getString("title"));
        productAttributes.put(PRICE, String.valueOf(jsonObject.getJSONObject("product").getJSONObject(PRICE).getJSONObject("listPrice").getDouble(PRICE)));

        return productAttributes;
    }

    /**
     * Maps the JPA storage object to the data transfer object.
     *
     * @param  product
     * @return product
     */
    protected Product documentToDTO(com.nathanielpautzke.bullseyeguy.storage.Product product){

        log.info("Converting database document to data transfer object...");
        com.nathanielpautzke.bullseyeguy.domain.Price price = com.nathanielpautzke.bullseyeguy.domain.Price.builder()
                .value(product.getPrice().getValue())
                .build();

        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(price)
                .build();
    }
}
