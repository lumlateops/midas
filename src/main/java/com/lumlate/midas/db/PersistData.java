package com.lumlate.midas.db;

import java.util.HashMap;
import java.util.Map;

import com.lumlate.midas.coupon.Coupon;
import com.lumlate.midas.db.dao.DealDAO;
import com.lumlate.midas.db.dao.DealEmailDAO;
import com.lumlate.midas.db.orm.AccountORM;
import com.lumlate.midas.db.orm.DealEmailORM;
import com.lumlate.midas.db.orm.DealORM;
import com.lumlate.midas.db.orm.SubscriptionORM;
import com.lumlate.midas.email.Email;

public class PersistData {
	private Email email;
	private Coupon coupon;
	
	private static final Map<String, Integer> emailcategories = new HashMap<String, Integer>() {
	    { 
	        put("deal", 1);
	        put("subscription", 2);
	        put("spam", 3);
	        put("confirmation", 4);
	        put("other", 5);
	    }
	};
	
	public Email getEmail() {
		return email;
	}

	public Coupon getCoupon() {
		return coupon;
	}	
	
	public void persist(){
		DealEmailORM email=new DealEmailORM();
		DealEmailDAO emaildao=new DealEmailDAO();
		
		email.setCategory(emailcategories.get(this.email.getCategory()));
		email.setContent(this.email.getContent());
		email.setDateReceived(this.email.getRecieveddate());
		email.setDomainKey(this.email.getDomainkey_status());
		email.setFromEmail(this.email.getFromemail());
		email.setFromName(this.email.getFromname());
		email.setParsedContent(this.email.getHtml().getRawtext());
		email.setSenderIP(this.email.getSenderip());
		email.setSentDate(this.email.getSentdate());
		email.setSpfResult(this.email.getSpf_result());
		email.setSubject(this.email.getSubject());
		email.setToName(this.email.getTo());
		long emailid=emaildao.insert(email);
		
		DealORM deal = new DealORM();
		DealDAO dealdao=new DealDAO();
		AccountORM account = new AccountORM();
		long accountId=account.getId(this.email.getTo());
		long userId=account.getUserid();
		SubscriptionORM subscription=new SubscriptionORM();
		long subscriptionId=subscription.getId(account.getId(),this.coupon.getRetailer().getId());
		deal.setCreatedAt(this.email.getRecieveddate());
		deal.setDealValue(this.coupon.getDealvalue());
		deal.setDiscountPercentage(this.coupon.getSalepercentage());
		//deal.setOriginalValue();
		deal.setPostDate(this.email.getRecieveddate());
		deal.setSubscription_id(subscriptionId);
		deal.setTitle(this.email.getHtml().getTitle());
		deal.setUpdatedAt(this.email.getRecieveddate());
		deal.setUserInfoid(userId);
		//deal.setExpiryDate();
		deal.setFreeShipping(this.coupon.isIs_free_shipping());
		deal.setValidTo(this.coupon.getExpiration());
		deal.setDealEmailId(emailid);
		long dealid=dealdao.insert(deal);
	}
	
}
