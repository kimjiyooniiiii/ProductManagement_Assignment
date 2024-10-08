package com.example.product_manage.infrastructure;

import com.example.product_manage.domain.EntityNotFoundException;
import com.example.product_manage.domain.Product;
import com.example.product_manage.domain.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("test")
public class ListProductRepository implements ProductRepository {

    private List<Product> products = new ArrayList<>();
    private AtomicLong sequence = new AtomicLong(1L);

    public Product add(Product product){
        product.setId(sequence.getAndAdd(1L));

        products.add(product);
        return product;
    }

    public Product findById(Long id){
        return products.stream()
                .filter(p -> p.sameId(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product를 찾지 못했습니다."));
    }

    public List<Product> findAll(){
        return products;
    }

    public List<Product> findByNameContaining(String name){
        return products.stream()
                .filter(product -> product.containsName(name))
                .toList();
    }

    public Product update(Product product){
        Integer indexToModify = products.indexOf(product);  // 매개변수와 동일한 인스턴스 반환. equals 재정의 필요
        products.set(indexToModify, product);
        return product;
    }

    public void delete(Long id){
        Product product = this.findById(id);
        products.remove(product);
    }
}
