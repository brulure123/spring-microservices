package com.ecomerce.microservice.web.controller;

import java.net.URI;

import javax.validation.Valid;

import com.ecomerce.microservice.dao.ProductDAO;
import com.ecomerce.microservice.model.Product;
import com.ecomerce.microservice.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ProductController {

    @Autowired
    private ProductDAO productDAO;
    
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits(){
        Iterable<Product> produits = productDAO.findAll();

        SimpleBeanPropertyFilter monFilter = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFilter);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);

        return produitsFiltres;
    }

   @RequestMapping(value = "/Produits/{id}", method = RequestMethod.GET)
    public Product afficherUnProduit(@PathVariable int id){
        Product produit = productDAO.findById(id);
        
        if(produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        return produit;
    }

    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product){
        Product productAdded = productDAO.save(product);

        if(productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(productAdded.getId())
        .toUri();

        return ResponseEntity.created(location).build();
    }
    
    @DeleteMapping(value="/Produits/{id}")
    public void supprimerProduit(@PathVariable int id){
        productDAO.deleteById(id);
    }

    @PutMapping(value = "/Produits")
    public void updateProduit(@RequestBody Product product){
        productDAO.save(product);
    }
}