package app.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import app.StudentCalc;
import app.bean.ColumnData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class LoanCalcViewController implements Initializable {

	private StudentCalc SC = null;

	@FXML private TextField LoanAmount;
	@FXML private TextField InterestRate;
	@FXML private TextField NbrOfYears;

	@FXML private Label lblTotalAdditionalPayemnts;
	@FXML private Label lblTotalPayemnts;
	@FXML private Label lblTotalInterests;

	@FXML private DatePicker PaymentStartDate;

	@FXML
	TableView<ColumnData> tableView;
	@FXML
	TableColumn<ColumnData, Integer> tc_id;
	@FXML
	TableColumn<ColumnData, String> tc_date;
	@FXML
	TableColumn<ColumnData, String> tc_payment;
	@FXML
	TableColumn<ColumnData, String> tc_addpayment;
	@FXML
	TableColumn<ColumnData, String> tc_interest;
	@FXML
	TableColumn<ColumnData, String> tc_principle;
	@FXML
	TableColumn<ColumnData, String> tc_balance;
	
	ObservableList<ColumnData> observableArrayList = FXCollections.observableArrayList();
	Map<String, Double> AdditionalMap = new HashMap<String, Double>();
	DecimalFormat df = new DecimalFormat("0.00");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tc_id.setCellValueFactory(new PropertyValueFactory<ColumnData, Integer>("id"));
		tc_date.setCellValueFactory(new PropertyValueFactory<ColumnData, String>("date"));
		tc_payment.setCellValueFactory(new PropertyValueFactory<ColumnData, String>("payment"));
		tc_addpayment.setCellValueFactory(new PropertyValueFactory<ColumnData, String>("addpayment"));
		tc_interest.setCellValueFactory(new PropertyValueFactory<ColumnData, String>("interest"));
		tc_principle.setCellValueFactory(new PropertyValueFactory<ColumnData, String>("principle"));
		tc_balance.setCellValueFactory(new PropertyValueFactory<ColumnData, String>("balance"));
		tableView.setItems(observableArrayList);

		tc_addpayment.setCellFactory(TextFieldTableCell.forTableColumn());
		tc_addpayment.setOnEditCommit(event -> {
			try {
				String date = event.getRowValue().getDate();
				if (event.getNewValue().length() > 0) {
					double additionalPay = df.parse(event.getNewValue()).doubleValue();
					AdditionalMap.put(date, additionalPay);
					btnCalcLoan(null);
				} else {
					AdditionalMap.remove(date);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		tableView.setEditable(true);
	}

	public void setMainApp(StudentCalc sc) {
		this.SC = sc;
	}

	/**
	 * btnCalcLoan - Fire this event when the button clicks
	 * 
	 * @version 1.0
	 * @param event
	 */
	@FXML
	private void btnCalcLoan(ActionEvent event) {

		try {
			observableArrayList.clear();
			double amount = Double.valueOf(LoanAmount.getText());
			double rate = Double.valueOf(InterestRate.getText());
			int year = Integer.valueOf(NbrOfYears.getText());
			double r = rate / 12;
			double numberOfMonth = year * 12;
			double PMT = Double.valueOf(df.format(Math.abs(FinanceLib.pmt(r, numberOfMonth, amount, 0, false))));
			observableArrayList.add(new ColumnData(df.format(amount)));

			LocalDate localDate = PaymentStartDate.getValue();
			int dayOfmonth = localDate.getDayOfMonth();
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
				try {
					localDate=localDate.withDayOfMonth(dayOfmonth);
				} catch (Exception e) {
					localDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
				}
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
			lblTotalAdditionalPayemnts.setText(df.format(additional));
			lblTotalPayemnts.setText(df.format(total));
			lblTotalInterests.setText(df.format(interests));
		} catch (Exception e) {
			e.printStackTrace();
		}

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
