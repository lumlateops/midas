package com.lumlate.midas.db;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.db.dao.AccountDAO;
import com.lumlate.midas.db.dao.DealDAO;
import com.lumlate.midas.db.dao.DealEmailDAO;
import com.lumlate.midas.db.dao.DepartmentDAO;
import com.lumlate.midas.db.dao.RetailersDAO;
import com.lumlate.midas.db.dao.SubscriptionDAO;
import com.lumlate.midas.db.orm.AccountORM;
import com.lumlate.midas.db.orm.DealEmailORM;
import com.lumlate.midas.db.orm.DealORM;
import com.lumlate.midas.db.orm.DepartmentORM;
import com.lumlate.midas.db.orm.RetailersORM;
import com.lumlate.midas.db.orm.SubscriptionORM;
import com.lumlate.midas.email.Email;
import com.mysql.jdbc.PreparedStatement;

public class PersistData {
	private static String mysqlhost;
	private static String mysqlport;
	private static String mysqluser;
	private static String mysqlpassword;
	private static String database;
	private static Map<String, Integer> emailcategories;
	private MySQLAccess myaccess;
	private PreparedStatement stmt;
	private DealEmailORM emailorm;
	private DealEmailDAO emaildao;
	private RetailersDAO retailersdao;
	private DepartmentORM department;
	private DepartmentDAO departmentdao;
	private SubscriptionORM subscription;
	private SubscriptionDAO subscriptiondao;
	private DealORM deal;
	private DealDAO dealdao;
	private AccountORM accountorm;
	private AccountDAO accountdao;
	private Gson gson;
	public PersistData(String mysqlhost, String mysqlport, String mysqluser,
			String password, String database) {
		mysqlhost = mysqlhost;
		mysqlport = mysqlport;
		mysqluser = mysqluser;
		mysqlpassword = password;
		database = database;
		gson = new Gson();
		myaccess = new MySQLAccess(mysqlhost, mysqlport, mysqluser,
				mysqlpassword, database);
		
		emailorm = new DealEmailORM();
		emaildao = new DealEmailDAO();
		emaildao.setAccess(myaccess);
		
		accountorm = new AccountORM();
		accountdao = new AccountDAO();
		accountdao.setAccess(myaccess);

		retailersdao = new RetailersDAO();
		retailersdao.setAccess(myaccess);

		department = new DepartmentORM();
		departmentdao = new DepartmentDAO();
		departmentdao.setAccess(myaccess);

		subscription = new SubscriptionORM();
		subscriptiondao = new SubscriptionDAO();
		subscriptiondao.setAccess(myaccess);

		deal = new DealORM();
		dealdao = new DealDAO();
		dealdao.setAccess(myaccess);

		emailcategories = new HashMap<String, Integer>() {
			{
				put("deal", 1);
				put("subscription", 2);
				put("spam", 3);
				put("confirmation", 4);
				put("other", 5);
			}
		};
	}

	public AccountORM getAccount() {
		return accountorm;
	}

	public void setAccount(AccountORM account) {
		this.accountorm = account;
	}

	public static Map<String, Integer> getEmailcategories() {
		return emailcategories;
	}

	public void persist(Email email, Coupon coupon) throws Exception {
		emailorm.setCategory(emailcategories.get(email.getCategory()));
		emailorm.setContent(email.getContent());
		emailorm.setDateReceived(email.getRecieveddate());
		emailorm.setDomainKey(email.getDomainkey_status());
		emailorm.setFromEmail(email.getFromemail());
		emailorm.setFromName(email.getFromname());
		emailorm.setParsedContent(email.getHtml().getRawtext());
		emailorm.setSenderIP(email.getSenderip());
		emailorm.setSentDate(email.getSentdate());
		emailorm.setSpfResult(email.getSpf_result());
		emailorm.setSubject(email.getSubject());
		emailorm.setToName(email.getToname());
		emailorm = emaildao.insertGetId(emailorm);

		if (accountorm.getEmail() == null)
			accountorm.setEmail(email.getToemail());
		if (accountorm.getId() <= 0)
			accountorm = accountdao.getIDfromEmail(accountorm);

		department.setCreatedAt(emailorm.getDateReceived());
		department.setEmail(emailorm.getFromEmail());
		department.setLogo(null);
		department.setUpdatedAt(emailorm.getDateReceived());
		department.setRetailerId(retailersdao.getRetailer(coupon.getRetailer().getDomain(),coupon.getRetailer()).getId());
		try{
			department = departmentdao.insertGetId(department);
		}catch(Exception err){
			err.printStackTrace();
		}
		subscription.setAccountId(accountorm.getId());// change it
		subscription.setCreatedAt(email.getRecieveddate());
		subscription.setDepartment_id(department.getId());
		subscription.setActive(true);
		subscription = subscriptiondao.insertGetId(subscription);

		deal.setCreatedAt(email.getRecieveddate());
		deal.setDealValue(coupon.getDealvalue());
		deal.setDiscountPercentage(coupon.getSalepercentage());
		// deal.setOriginalValue(this.coupon.get);
		deal.setPostDate(email.getRecieveddate());
		deal.setSubscription_id(subscription.getId());
		deal.setTitle(email.getHtml().getTitle());
		deal.setUpdatedAt(email.getRecieveddate());
		deal.setUserInfoid(accountorm.getUserid());
		deal.setExpiryDate(coupon.getExpiration());
		deal.setFreeShipping(coupon.isIs_free_shipping());
		deal.setValidTo(coupon.getValidupto());
		deal.setDealEmailId(emailorm.getId());
		deal = dealdao.insertGetId(deal);
		clear();
	}

	private void clear() {
		emailorm.clear();
		accountorm.clear();
		department.clear();
		subscription.clear();
		deal.clear();
	}

}
