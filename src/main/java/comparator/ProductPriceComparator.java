package comparator;

import com.how2java.tmall.pojo.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {

    @Override
    public int compare(Product product1, Product product2) {
        return (int) (product2.getPromotePrice()-product1.getPromotePrice());
    }
}
