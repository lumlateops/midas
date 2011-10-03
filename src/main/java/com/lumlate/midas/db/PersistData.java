package com.lumlate.midas.db;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.db.dao.AccountDAO;
import com.lumlate.midas.db.dao.DealCategoryDAO;
import com.lumlate.midas.db.dao.DealDAO;
import com.lumlate.midas.db.dao.DealDealCategoryDAO;
import com.lumlate.midas.db.dao.DealEmailDAO;
import com.lumlate.midas.db.dao.DealProductDAO;
import com.lumlate.midas.db.dao.DepartmentDAO;
import com.lumlate.midas.db.dao.ProductDAO;
import com.lumlate.midas.db.dao.RetailersDAO;
import com.lumlate.midas.db.dao.SubscriptionDAO;
import com.lumlate.midas.db.orm.AccountORM;
import com.lumlate.midas.db.orm.DealCategoryORM;
import com.lumlate.midas.db.orm.DealDealCategoryORM;
import com.lumlate.midas.db.orm.DealEmailORM;
import com.lumlate.midas.db.orm.DealORM;
import com.lumlate.midas.db.orm.DealProductORM;
import com.lumlate.midas.db.orm.DepartmentORM;
import com.lumlate.midas.db.orm.ProductORM;
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
	private DealProductDAO dealproductdao;
	private ProductDAO productdao;
	private DealDealCategoryDAO dealdealcategorydao;
	private Gson gson;
	
	public PersistData(String mysqlhost, String mysqlport, String mysqluser,
			String password, String database) {
		this.mysqlhost = mysqlhost;
		this.mysqlport = mysqlport;
		this.mysqluser = mysqluser;
		mysqlpassword = password;
		this.database = database;
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
		
		dealproductdao=new DealProductDAO();
		dealproductdao.setAccess(myaccess);
		
		dealdealcategorydao = new DealDealCategoryDAO();
		dealdealcategorydao.setAccess(myaccess);
		productdao = new ProductDAO();
		productdao.setAccess(myaccess);
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
		deal.setTitle(email.getSubject());
		deal.setUpdatedAt(email.getRecieveddate());
		deal.setUserInfoid(accountorm.getUserid());
		deal.setExpiryDate(coupon.getExpiration());
		deal.setFreeShipping(coupon.isIs_free_shipping());
		deal.setValidTo(coupon.getValidupto());
		deal.setDealEmailId(emailorm.getId());
		if(coupon.getProducts()!=null && coupon.getProducts().size()>0){
			String tags="";
			for(ProductORM p:coupon.getProducts()){
				tags+=p.getItem()+",";
			}
			deal.setTags(tags.substring(0, tags.length()-2));
		}
		deal = dealdao.insertGetId(deal);
		List<DealDealCategoryORM> dealdealcategorylist = new ArrayList<DealDealCategoryORM>();
		List<DealProductORM> dealproductlist = new ArrayList<DealProductORM>();
		if(coupon.getProducts()!=null && coupon.getProducts().size()>0){
			for(ProductORM p:coupon.getProducts()){
				DealDealCategoryORM d=new DealDealCategoryORM();
				DealProductORM dp=new DealProductORM();
				p=productdao.getIDbyItem(p);
				d.setDealId(deal.getId());
				d.setCategoryId(p.getCategory_id());
				dealdealcategorylist.add(d);
				dp.setDealId(deal.getId());
				dp.setProductId(p.getId());
				dealproductlist.add(dp);
			}
			dealdealcategorydao.multiInsertGetIds(dealdealcategorylist);
			dealproductdao.multiInsertGetIds(dealproductlist);
		}
		
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
