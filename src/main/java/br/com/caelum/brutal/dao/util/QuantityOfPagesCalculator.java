package br.com.caelum.brutal.dao.util;

public class QuantityOfPagesCalculator {
	
	public static long calculatePages(Long count, Integer pageSize) {
		long result = count/pageSize.longValue();
		if (count % pageSize.longValue() != 0) {
			result++;
		}
		return result;
	}

}
