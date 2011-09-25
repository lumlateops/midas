package com.lumlate.midas.db;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
	private Email email;
	private Coupon coupon;

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

	public PersistData(String mysqlhost, String mysqlport, String mysqluser,
			String password, String database) {
		mysqlhost = mysqlhost;
		mysqlport = mysqlport;
		mysqluser = mysqluser;
		mysqlpassword = password;
		database = database;

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

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
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

	public void persist() throws Exception {
		emailorm.setCategory(emailcategories.get(this.email.getCategory()));
		emailorm.setContent(this.email.getContent());
		emailorm.setDateReceived(this.email.getRecieveddate());
		emailorm.setDomainKey(this.email.getDomainkey_status());
		emailorm.setFromEmail(this.email.getFromemail());
		emailorm.setFromName(this.email.getFromname());
		emailorm.setParsedContent(this.email.getHtml().getRawtext());
		emailorm.setSenderIP(this.email.getSenderip());
		emailorm.setSentDate(this.email.getSentdate());
		emailorm.setSpfResult(this.email.getSpf_result());
		emailorm.setSubject(this.email.getSubject());
		emailorm.setToName(this.email.getToname());
		emailorm = emaildao.insertGetId(emailorm);

		if (accountorm.getEmail() == null)
			accountorm.setEmail(this.email.getToemail());
		if (accountorm.getId() <= 0)
			accountorm = accountdao.getIDfromEmail(accountorm);

		RetailersORM retailer = retailersdao.getRetailer(this.coupon.getRetailer().getDomain());

		department.setCreatedAt(emailorm.getDateReceived());
		department.setEmail(emailorm.getFromEmail());
		department.setLogo(null);
		department.setUpdatedAt(emailorm.getDateReceived());
		department.setRetailerId(retailer.getId());
		department = departmentdao.insertGetId(department);

		subscription.setAccountId(accountorm.getId());// change it
		subscription.setCreatedAt(this.email.getRecieveddate());
		subscription.setDepartment_id(department.getId());
		subscription.setActive(true);
		subscription = subscriptiondao.insertGetId(subscription);

		deal.setCreatedAt(this.email.getRecieveddate());
		deal.setDealValue(this.coupon.getDealvalue());
		deal.setDiscountPercentage(this.coupon.getSalepercentage());
		// deal.setOriginalValue(this.coupon.get);
		deal.setPostDate(this.email.getRecieveddate());
		deal.setSubscription_id(subscription.getId());
		deal.setTitle(this.email.getHtml().getTitle());
		deal.setUpdatedAt(this.email.getRecieveddate());
		deal.setUserInfoid(accountorm.getUserid());
		deal.setExpiryDate(this.coupon.getExpiration());
		deal.setFreeShipping(this.coupon.isIs_free_shipping());
		deal.setValidTo(this.coupon.getValidupto());
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
