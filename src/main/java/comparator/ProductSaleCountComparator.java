package comparator;

import com.how2java.tmall.pojo.Product;

import java.util.Comparator;

public class ProductSaleCountComparator implements Comparator<Product> {

    @Override
    public int compare(Product product1, Product product2) {
        return product2.getSaleCount()-product1.getSaleCount();
    }
}
