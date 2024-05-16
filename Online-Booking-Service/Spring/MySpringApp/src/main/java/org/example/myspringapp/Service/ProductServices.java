package org.example.myspringapp.Service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.example.myspringapp.DTO.ReviewDTO;
import org.example.myspringapp.Model.Comment;
import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.Reservation;
import org.example.myspringapp.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServices {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    JWTUtils jwtUtils;

    /*
        #Response Sheet
        200 => everything went as planned
        404 => resource cant be found
        500 => duplicated name
        501 => token not valid
        502 => product not found
    */

    /*
            #CRUD operations implemented
    */

    public Map<String,Object> addProductVol1(Product product ,String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        //check if the name is unique
        if(productRepository.findByName(product.getName()) != null){
            response.put("response",500);
            return response;

        }
        //assuming that all fields in Product are full
        productRepository.save(product);
        response.put("response",200);

        return response;
    }

    public Map<String, Object> addProduct(Product product, String token, MultipartFile imageFile) throws IOException {
        Map<String, Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response", 501);
            return response;
        }

        // Check if the name is unique
        if (productRepository.findByName(product.getName()) != null){
            response.put("response", 500);
            return response;
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            // Compress the image before saving
            byte[] compressedImageData = compressImage(imageFile);
            product.setImage(compressedImageData);
        }

        // Assuming that all fields in Product are filled
        productRepository.save(product);
        response.put("response", 200);

        return response;
    }
    public Map<String,Object> deleteProducts(Product product , String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }

        if (productRepository.findByName(product.getName()) == null){
            response.put("response",502);
            return response;
        }
        response.put("response",200);
        productRepository.delete(productRepository.findByName(product.getName()));

        return response;
    }

    public Map<String,Object> editProduct(Product product , String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        Product existingProduct = productRepository.findByName(product.getName());
        if (existingProduct == null){
            response.put("response",502);
            return response;
        }else {
            existingProduct.setAvailability(product.getAvailability());
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setLocation(product.getLocation());
            existingProduct.setPrice(product.getPrice());
            response.put("response",200);
            productRepository.save(existingProduct);

        }


        return response;

    }
    public Map<String, Object> editProduct(Product product, String token, MultipartFile imageFile) {
        Map<String, Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)) {
            response.put("response", 501);
            return response;
        }

        Product existingProduct = productRepository.findByName(product.getName());
        if (existingProduct == null) {
            response.put("response", 502);
            return response;
        } else {
            existingProduct.setAvailability(product.getAvailability());
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setLocation(product.getLocation());
            existingProduct.setPrice(product.getPrice());

            // Handle image upload and compression
            try {
                byte[] compressedImageData = compressImage(imageFile);
                existingProduct.setImage(compressedImageData); // Set compressed image data to Product entity
            } catch (IOException e) {
                e.printStackTrace();
                response.put("response", 503); // Error while compressing or uploading image
                return response;
            }

            productRepository.save(existingProduct);
            response.put("response", 200);
        }

        return response;
    }


    public Map<String,Object> getComments(String token,Long id){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)) {
            response.put("response", 501);
            return response;
        }
        List<Reservation> reservationList = reservationRepository.findByProductId(id);
        if(reservationList == null){
            response.put("response",502);
            response.put("data",reservationList);
            return response;
        }
        response.put("response",200);
        response.put("data",reservationList);

        return  response;
    }




    public Map<String,Object> filterByCategory(String category , String token,int page){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        Integer LastPage = ((productRepository.findByCategory(category).size())/3)+1;
        System.out.println("Last Page\t"+ LastPage);
        System.out.println("Current Page\t"+ page);
        System.out.println("Bool " + LastPage.equals(page));
        Pageable pageable = PageRequest.of(page-1,3);
        //response.put("response",productRepository.findAll(pageable).getContent());
        List<Product> productList = productRepository.findByCategory(category,pageable);
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);
        response.put("isLast",LastPage.equals(page));
        response.put("isFirst",page==1);

        return response;
    }

    public Map<String,Object> sortByPriceASC(String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        List<Product> productList = productRepository.findAllByOrderByPriceAsc();
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);

        return response;
    }

    public Map<String,Object> sortByPriceDESC(String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        List<Product> productList = productRepository.findAllByOrderByPriceDesc();
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);

        return response;
    }

    public Map<String,Object> FilterByPrice(String token,Integer price,int page){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        Pageable pageable = PageRequest.of(page-1,3);
        //response.put("response",productRepository.findAll(pageable).getContent());
        Integer LastPage = (productRepository.findByPriceLessThan(price).size())/3;
        List<Product> productList = productRepository.getAllByPriceLessThan(price,pageable);
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);
        response.put("isLast",page==LastPage);
        response.put("isFirst",page==1);
        return response;
    }

    public Map<String,Object> getPageableProducts(int page,String token){
        Map<String,Object> response = new HashMap<>();
        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        Integer LastPage = (productRepository.findAll().size())/3;
        Pageable pageable = PageRequest.of(page-1,3);
        response.put("response",productRepository.findAll(pageable).getContent());
        response.put("isLast",page==LastPage);
        response.put("isFirst",page==1);
        return response;
    }


    public Map<String ,Object> AddRating(String token , ReviewDTO reviewDTO){
        Map<String,Object> response= new HashMap<>();
        //1st step => check if token is valid
        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }

        //2nd step => Get the product Object
        Product product =  productRepository.findById(reviewDTO.getReservation().getProduct().getId());
        //3rd step => get the product if exist
        if(product == null){
            response.put("response" , 502);
            return response;
        }
        // calculer et save rating
        int count = productRepository.countBookingsForProduct(product.getId());
        int CalculatedRating = (int)((product.getRating()*count) + reviewDTO.getReservation().getProduct().getId())/(count+1);
        product.setRating(CalculatedRating);
        productRepository.save(product);

        // add comment

        Reservation reservation = reservationRepository.findById(reviewDTO.getReservation().getId()).get();
        Comment comment = Comment.builder()
                .Comment(reviewDTO.getCommentinput())
                .reservation(reviewDTO.getReservation())
                .build();
        commentRepository.save(comment);

        reservationRepository.save(reservation);

        response.put("response",200);
        response.put("rating",CalculatedRating);
        response.put("comment",reviewDTO.getCommentinput());
        return response;

    }

    public byte[] compressImage(MultipartFile imageFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the image using Thumbnails library and write to output stream
        Thumbnails.of(imageFile.getInputStream())
                .size(100, 100) // Specify the desired dimensions
                .outputQuality(0.6) // Adjust the quality as needed (0.0 to 1.0)
                .toOutputStream(outputStream);

        return outputStream.toByteArray(); // Convert output stream to byte array
    }

}

