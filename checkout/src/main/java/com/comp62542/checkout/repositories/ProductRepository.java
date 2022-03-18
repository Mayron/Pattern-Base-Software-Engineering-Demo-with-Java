package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

@Repository
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class ProductRepository extends BaseRepository<Product> {

}
