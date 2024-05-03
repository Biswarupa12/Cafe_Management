package com.cafe.cafe_management.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Constants.CafeConstants;
import com.cafe.cafe_management.Filter.JwtFilter;
import com.cafe.cafe_management.Model.Category;
import com.cafe.cafe_management.Model.Product;
import com.cafe.cafe_management.Repo.ProductRepo;
import com.cafe.cafe_management.Service.ProductService;
import com.cafe.cafe_management.Wrapper.ProductWrapper;
import com.cafe.cafe_management.utils.CafeUtils;

@Service
public class ProductServiceImpl   implements ProductService{
    @Autowired
    ProductRepo  productRepo;

    @Autowired
    JwtFilter jwtFilter;




    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap , false)){
                    productRepo.save(getProductfromMap(requestMap , false));
                    return CafeUtils.getResponseEntity("Product added Successfully", HttpStatus.OK);
                }

                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            else 
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }
            else if(!validateId){
                return true;
            }
        }
        return false;
       
    }

    private Product getProductfromMap(Map<String, String> requestMap, boolean isAdd) {
      Category category = new Category();
      category.setCategoryId(Integer.parseInt(requestMap.get("categoryId")));

      Product product = new Product();
      if(isAdd){
        product.setId(Integer.parseInt(requestMap.get("id")));

      }else{
        product.setStatus("true");
      }

      product.setCategory(category);
      product.setName(requestMap.get("name"));
      product.setDescription(requestMap.get("description"));
      product.setPrice(Integer.parseInt(requestMap.get("price")));
      return product;
    }



    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
            try {
                return new ResponseEntity<>(productRepo.getAllProduct(), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
            }


        return new ResponseEntity<>(new ArrayList<>() , HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, true)){
                   Optional< Product>  optional = productRepo.findById(Integer.parseInt(requestMap.get("id")));
                   if(!optional.isEmpty()){
                    Product product = getProductfromMap(requestMap, true);
                    product.setStatus(optional.get().getStatus());
                    productRepo.save(product);
                    return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);
                   }else{
                    return CafeUtils.getResponseEntity("Product id is not exist", HttpStatus.OK);
                   }
                }else{
                    return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
          e.printStackTrace();  
        }



        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
            try {
                if(jwtFilter.isAdmin()){
                   Optional optional =  productRepo.findById(id);
                   if(optional.isEmpty()){
                    productRepo.deleteById(id);
                    return CafeUtils.getResponseEntity("Product deleted Sucessfully", HttpStatus.OK);
                   }
                   return CafeUtils.getResponseEntity("Product id doesnot exist", HttpStatus.OK);

                }else{
                    return CafeUtils.getResponseEntity(CafeConstants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
        e.printStackTrace();                
            }


        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        
        try {
            if(jwtFilter.isAdmin()){
              Optional optional =   productRepo.findById(Integer.parseInt(requestMap.get("id")));

              if(!optional.isEmpty()){
                productRepo.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));

                return CafeUtils.getResponseEntity("Product status Updated Successfully "  , HttpStatus.OK);
              }
              return CafeUtils.getResponseEntity("Product Id doesn't exist", HttpStatus.OK);
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productRepo.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
            try {
                return new  ResponseEntity<>(productRepo.getProductById(id) , HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();    
            }



        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
