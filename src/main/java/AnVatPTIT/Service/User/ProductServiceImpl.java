package AnVatPTIT.Service.User;//productServiceIml

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AnVatPTIT.Dao.ProductsDao;
import AnVatPTIT.Dto.ProductsDto;
@Service
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	ProductsDao productDao = new ProductsDao();

	public ProductsDto GetProductByID(long id) {
		List<ProductsDto> listProducts = productDao.GetProductByID(id);
		return listProducts.get(0);
	}

	public List<ProductsDto> GetProductByIDCategory(int id) {
		return productDao.GetAllProductsByID(id);
	}

	public List<ProductsDto> GetProductByIDProduct(String id_products) {
		return productDao.GetAllBillsDetailByIdProduct(id_products);
	}
}