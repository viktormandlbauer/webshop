package at.fhtw.webshop.config;

import at.fhtw.webshop.model.Category;
import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.CategoryRepository;
import at.fhtw.webshop.repository.ProductRepository;
import at.fhtw.webshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCatalog();
    }

    private void seedAdmin() {
        if (userRepository.findByUsername("admin") != null) {
            return;
        }
        User admin = new User();
        admin.setSalutation("Herr");
        admin.setFirstName("Webshop");
        admin.setLastName("Admin");
        admin.setAddress("Campusweg 1");
        admin.setZip("1100");
        admin.setCity("Wien");
        admin.setEmail("admin@webshop.local");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin1234"));
        admin.setPaymentInfo("ADMIN");
        admin.setRole("Admin");
        userRepository.save(admin);
    }

    private void seedCatalog() {
        if (categoryRepository.count() == 0) {
            List.of("Elektronik", "Gaming", "Buecher", "Gadgets").forEach(name -> {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
            });
        }
        if (productRepository.count() > 0) {
            return;
        }
        Category electronics = categoryRepository.findByNameIgnoreCase("Elektronik").orElseThrow();
        Category gaming = categoryRepository.findByNameIgnoreCase("Gaming").orElseThrow();
        Category books = categoryRepository.findByNameIgnoreCase("Buecher").orElseThrow();
        createProduct("Aurora Headphones", "Kabellose Over-Ear-Kopfhoerer mit Noise Cancelling.", electronics, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=900", "149.90", "4.60", 25);
        createProduct("Pixel Tablet 11", "Leichtes Tablet fuer Studium, Streaming und Notizen.", electronics, "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=900", "429.00", "4.40", 14);
        createProduct("Neon Controller", "Praeziser Controller mit USB-C und RGB-Profilen.", gaming, "https://images.unsplash.com/photo-1600080972464-8e5f35f63d08?w=900", "69.90", "4.80", 42);
        createProduct("Java Web Guide", "Praxisbuch fuer REST, Sessions, Datenbanken und Frontends.", books, "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=900", "34.50", "4.70", 31);
    }

    private void createProduct(String name, String description, Category category, String imageUrl,
                               String price, String rating, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setImageURL(imageUrl);
        product.setPrice(new BigDecimal(price));
        product.setAvgRating(new BigDecimal(rating));
        product.setStock(stock);
        productRepository.save(product);
    }
}
