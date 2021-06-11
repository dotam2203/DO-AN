package AnVatPTIT.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MapperBillDetail implements RowMapper<BillDetail> {

	public BillDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		BillDetail billDetail = new BillDetail();
//		billDetail.setId(rs.getLong("id"));
		billDetail.setId_product(rs.getLong("id_product"));
		billDetail.setId_bills(rs.getLong("id_bills"));
		billDetail.setQuanty(rs.getInt("quanty"));
		billDetail.setTotal(rs.getDouble("total"));

		return billDetail;
	}

}
