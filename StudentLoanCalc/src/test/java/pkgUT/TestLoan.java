package pkgUT;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.*;
import org.junit.Test;

import app.bean.ColumnData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestLoan {

	@Test
	public void test() {
		double PMT;
		double r = 0.07 / 12;
		double n = 20 * 12;
		double p = 150000;
		double f = 0;
		boolean t = false;
		PMT = Math.abs(FinanceLib.pmt(r, n, p, f, t));
		
		double PMTExpected = 1162.95;
		
		assertEquals(PMTExpected, PMT, 0.01);
		
		
		
	}
	@Test
	public void testPay() throws Exception {
		DecimalFormat df = new DecimalFormat("0.00");
		List<ColumnData> observableArrayList = new ArrayList<ColumnData>();
		Map<String, Double> AdditionalMap = new HashMap<String, Double>();
		
		
		double amount = 150000;
		double rate = 0.07;
		int year = 20;
		double r = rate / 12;
		double numberOfMonth = year * 12;
		double PMT = Double.valueOf(df.format(Math.abs(FinanceLib.pmt(r, numberOfMonth, amount, 0, false))));
		double PMTExpected = 1162.95;
		
		assertEquals(PMTExpected, PMT, 0.01);
		LocalDate localDate = LocalDate.now();

		for (int i = 0; i < numberOfMonth && amount > 0 ; i++) {
			double interestOfMonth = amount * r;
			double principle = sub(PMT, Double.parseDouble(df.format(interestOfMonth)));
			double additionPay = 0;
			if (AdditionalMap.get(localDate.toString()) != null) {
				additionPay = Double.parseDouble(df.format(AdditionalMap.get(localDate.toString())));
			}
			if (i == numberOfMonth-1 || amount < PMT) {
				principle = amount;
			}
			amount = sub(amount, add(principle , additionPay));

			observableArrayList.add(new ColumnData(i + 1, localDate.toString(),
					df.format(add(principle , interestOfMonth)), additionPay == 0 ? null : df.format(additionPay),
					df.format(interestOfMonth), df.format(principle + additionPay), df.format(amount)));
			localDate = localDate.plusMonths(1);
		}

		double additional = 0, total = 0, interests = 0;
		for (ColumnData columnData : observableArrayList) {
			if (columnData.getId() != null) {
				if (columnData.getAddpayment() != null) {
					additional += df.parse(columnData.getAddpayment()).doubleValue();
				}
				total = add(total , add(df.parse(columnData.getInterest()).doubleValue()
						, df.parse(columnData.getPrinciple()).doubleValue()));
				interests = add(interests , df.parse(columnData.getInterest()).doubleValue());
			}
		}
		assertEquals(0, additional , 0);
		assertEquals(279106.89, total , 0.01);
		assertEquals(129106.89, interests , 0.01);

	}
	@Test
	public void testAdditionalPay() throws Exception {
		DecimalFormat df = new DecimalFormat("0.00");
		List<ColumnData> observableArrayList = new ArrayList<ColumnData>();
		Map<String, Double> AdditionalMap = new HashMap<String, Double>();
		
		
		double amount = 150000;
		double rate = 0.07;
		int year = 20;
		double r = rate / 12;
		double numberOfMonth = year * 12;
		double PMT = Double.valueOf(df.format(Math.abs(FinanceLib.pmt(r, numberOfMonth, amount, 0, false))));
		double PMTExpected = 1162.95;
		
		assertEquals(PMTExpected, PMT, 0.01);
		LocalDate localDate = LocalDate.parse("2010-10-10");
		AdditionalMap.put("2010-10-10", (double) 100000);
		for (int i = 0; i < numberOfMonth && amount > 0 ; i++) {
			double interestOfMonth = amount * r;
			double principle = sub(PMT, Double.parseDouble(df.format(interestOfMonth)));
			double additionPay = 0;
			if (AdditionalMap.get(localDate.toString()) != null) {
				additionPay = Double.parseDouble(df.format(AdditionalMap.get(localDate.toString())));
			}
			if (i == numberOfMonth-1 || amount < PMT) {
				principle = amount;
			}
			amount = sub(amount, add(principle , additionPay));
			
			observableArrayList.add(new ColumnData(i + 1, localDate.toString(),
					df.format(add(principle , interestOfMonth)), additionPay == 0 ? null : df.format(additionPay),
					df.format(interestOfMonth), df.format(principle + additionPay), df.format(amount)));
			localDate = localDate.plusMonths(1);
		}
		
		double additional = 0, total = 0, interests = 0;
		for (ColumnData columnData : observableArrayList) {
			if (columnData.getId() != null) {
				if (columnData.getAddpayment() != null) {
					additional += df.parse(columnData.getAddpayment()).doubleValue();
				}
				total = add(total , add(df.parse(columnData.getInterest()).doubleValue()
						, df.parse(columnData.getPrinciple()).doubleValue()));
				interests = add(interests , df.parse(columnData.getInterest()).doubleValue());
			}
		}
		assertEquals(100000, additional , 0);
		assertEquals(158511.93, total , 0.01);
		assertEquals(8511.93, interests , 0.01);
		
	}

	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.subtract(b2).doubleValue();
	}
	
	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.add(b2).doubleValue();
	}
}

 

