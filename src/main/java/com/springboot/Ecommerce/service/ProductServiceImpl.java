package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.exceptions.APIException;
import com.springboot.Ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.Ecommerce.model.Category;
import com.springboot.Ecommerce.model.Product;
import com.springboot.Ecommerce.payload.ProductDTO;
import com.springboot.Ecommerce.payload.ProductResponse;
import com.springboot.Ecommerce.repository.CategoryRepository;
import com.springboot.Ecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Product> alreadyPresentProducts = category.getProducts();
        for(Product product : alreadyPresentProducts){
            if(productDTO.getProductName().equals(product.getProductName())){
                throw new APIException("Product with this name exists in this category");
            }
        }
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setCategory(category);
        product.setImage("https://via.placeholder.com/150");
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        double specialPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100));
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> productsPage = productRepository.findAll(pageDetails);
        List<Product> products = productsPage.getContent();
        List<ProductDTO> productDTO = products.stream().map(p -> {
            return modelMapper.map(p,ProductDTO.class);
        }).toList();
        ProductResponse productsResponse = new ProductResponse();
        productsResponse.setContent(productDTO);
        productsResponse.setPageNumber(productsPage.getNumber());
        productsResponse.setTotalPages(productsPage.getTotalPages());
        productsResponse.setTotalElements(productsPage.getTotalElements());
        productsResponse.setPageSize(productsPage.getSize());
        productsResponse.setLastPage(productsPage.isLast());
        return productsResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> products = productRepository.findByCategory(category,pageDetails);
        List<ProductDTO> productDTOs = products.stream().map(p -> {
            return modelMapper.map(p,ProductDTO.class);
        }).toList();
        ProductResponse productsResponse = new ProductResponse();
        productsResponse.setContent(productDTOs);
        productsResponse.setPageNumber(products.getNumber());
        productsResponse.setTotalPages(products.getTotalPages());
        productsResponse.setTotalElements(products.getTotalElements());
        productsResponse.setPageSize(products.getSize());
        productsResponse.setLastPage(products.isLast());
        return productsResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword,pageDetails);
        List<ProductDTO> productDTO = products.stream().map(p -> {
            return modelMapper.map(p,ProductDTO.class);
        }).toList();
        ProductResponse productsResponse = new ProductResponse();
        productsResponse.setContent(productDTO);
        productsResponse.setPageNumber(products.getNumber());
        productsResponse.setTotalPages(products.getTotalPages());
        productsResponse.setTotalElements(products.getTotalElements());
        productsResponse.setPageSize(products.getSize());
        productsResponse.setLastPage(products.isLast());
        return productsResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException());
//        Category category = categoryRepository
//                .findById(product.getCategory())
//                .orElseThrow(() -> new ResourceNotFoundException());

        double specialPrice = productDTO.getPrice() - (productDTO.getPrice() * (productDTO.getDiscount() / 100));
        product.setProductName(productDTO.getProductName());
//        foundProduct.setCategory(category);
        product.setImage(productDTO.getImage());
        product.setDescription(productDTO.getDescription());
        product.setDiscount(productDTO.getDiscount());
        product.setQuantity(productDTO.getQuantity());
        product.setSpecialPrice(specialPrice);
        product.setPrice(productDTO.getPrice());
        productRepository.save(product);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException());
        productRepository.deleteById(productId);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","ProductID",productId));
        String imagePath = fileService.uploadImage(path,image);
        productFromDB.setImage(imagePath);
        productRepository.save(productFromDB);
        return modelMapper.map(productFromDB,ProductDTO.class);
    }

}
