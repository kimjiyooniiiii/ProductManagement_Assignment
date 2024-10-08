package com.example.product_manage.infrastructure;

import com.example.product_manage.domain.Product;
import com.example.product_manage.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("prod")
@RequiredArgsConstructor
public class DatabaseProductRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product add(Product product){
        // Product에서 getter로 가져온 인자를 매핑
        SqlParameterSource namedParameter = new BeanPropertySqlParameterSource(product);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update("INSERT INTO products (name, price, amount) VALUES (:name, :price, :amount)",
                namedParameter, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        product.setId(generatedId);

        return product;
    }

    public Product findById(Long id){
        SqlParameterSource namedParameter = new MapSqlParameterSource("id", id);

        Product product = namedParameterJdbcTemplate.queryForObject(
                "SELECT id, name, price, amount FROM products WHERE id=:id",
                namedParameter,
                new BeanPropertyRowMapper<>(Product.class)
        );

        return product;
    }

    public List<Product> findAll(){
        List<Product> products = namedParameterJdbcTemplate.query(
                "SELECT * FROM products",
                new BeanPropertyRowMapper<>(Product.class)
        );

        return products;
    }

    public List<Product> findByNameContaining(String name){
        SqlParameterSource namedParameter = new MapSqlParameterSource("name", "%" + name + "%");

        List<Product> products = namedParameterJdbcTemplate.query(
                "SELECT * FROM products WHERE name LIKE :name",
                namedParameter,
                new BeanPropertyRowMapper<>(Product.class)
        );

        return products;
    }

    public Product update(Product product){
        SqlParameterSource namedParameter = new BeanPropertySqlParameterSource(product);

        namedParameterJdbcTemplate.update(
                "UPDATE products SET name=:name, price=:price, amount=:amount WHERE id=:id",
                namedParameter
        );
        return product;
    }

    public void delete(Long id){
        SqlParameterSource namedParameter = new MapSqlParameterSource("id", id);

        namedParameterJdbcTemplate.update(
                "DELETE FROM products WHERE id=:id",
                namedParameter
        );
    }

}
