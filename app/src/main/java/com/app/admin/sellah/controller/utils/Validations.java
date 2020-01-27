package com.app.admin.sellah.controller.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MMM-dd" );
	public static boolean match(String str)
	{
		Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
		Matcher ms = ps.matcher(str);
	    boolean bs = ms.matches();
	    return bs;
	}

	private boolean isValidEmaillId(String email){

	    return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
	              + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
	              + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
	              + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
	     }
	
	public boolean userLoginvalidate(Context context, String username, String userpassword) {
		if(username.length()==0 || userpassword.length()==0 )
		{
			String message  = "Please fill all fields";
			showMessageOKCancel(context, message);
			return false;
		}
		
		else if (username.length()==0) {
			String message  = "Please fill username field";
			showMessageOKCancel(context, message);
			return false;
		} 
		
		
/*	else if(!match(username))
		{
			String message  = "Enter characters only in username field";
			showMessageOKCancel(context, message);
			return false;
		}*/

		
		else if (userpassword.length()==0) {
			String message  = "Please fill password field";
			showMessageOKCancel(context, message);
			return false;
		} 
		

		 else if( userpassword.length() < 5)
			{
			
			 String message  = "The password you've entered is incorrect";
				showMessageOKCancel(context, message);
				return false;
			
			}

		else
		{
		return true;
		}	
	}
	


	public  boolean RegisterValidate(Context context, String username, String firstName, String lastName, String email  , String birthday,String pass,String Retypepass)
	{

		if(username.length()==0 && firstName.length()==0 && lastName.length()==0 && email.length()==0 && birthday.length()==0 &&  pass.length()==0 && Retypepass.length()==0 )
		{
			String message  = "Please fill all fields";
			showMessageOKCancel(context, message);
			return false;

		}

		else if (username.length()==0) {
			String message  = "Please fill username field";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (firstName.length()==0) {
			String message  = "Please fill firstname field";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (lastName.length()==0) {
			String message  = "Please fill lastname field";
			showMessageOKCancel(context, message);
			return false;
		}

/*		else if(!match(Name))
		{
			String message  = "Enter characters only in username field";
			showMessageOKCancel(context, message);
			return false;
		}*/

		else if (email.length()==0) {
			String message  = "Please fill email field";
			showMessageOKCancel(context, message);
			return false;
		}


		else if(!isValidEmaillId(email.trim()))
		{

			String message  = "Please enter a valid email address";
			showMessageOKCancel(context, message);
			return false;

		}
		else if (birthday.length()==0) {
			String message  = "Please fill birthday field";
			showMessageOKCancel(context, message);
			return false;
		}

		/*else if(!isValidEmaillId(paypalemail.trim()))
		{

			String message  = "Please enter a valid paypal email address";
			showMessageOKCancel(context, message);
			return false;

		}*/



		else if(pass.length()<5 || pass.length()>40)
		{
			String message  ="Please enter password between 5 to 40 digits long.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (Retypepass.length()==0) {
			String message  = "Please fill re-type password field.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(!pass.equals(Retypepass))
		{
			String message  ="Password not matched ! ";
			showMessageOKCancel(context, message);
			return false;
		}
		else
			return true;
	}

	public  boolean stripevalid(Context context, String first,  String last, String dob,String pid,  String address, String country,String state,String city,  String postalcode, String currency,String acc,String routing)
	{

		if(first.length()==0 && last.length()==0 && dob.length()==0 &&  pid.length()==0  && address.length()==0 &&  country.length()==0 && state.length()==0 && city.length()==0 &&  postalcode.length()==0 && currency.length()==0 && acc.length()==0 &&  routing.length()==0 )
		{
			String message  = "Please fill all fields";
			showMessageOKCancel(context, message);
			return false;

		}

		else if (first.length()==0) {
			String message  = "Please enter first name.";
			showMessageOKCancel(context, message);
			return false;
		}


		else if (last.length()==0) {
			String message  = "Please enter last name.";
			showMessageOKCancel(context, message);
			return false;
		}


		else if (dob.length()==0) {
			String message  = "Please enter date of birth";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (pid.length()==0) {
			String message  = "Please enter personal ID";
			showMessageOKCancel(context, message);
			return false;
		}



		else if (address.length()==0) {
			String message  = "Please enter address";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (country.length()==0) {
			String message  = "Please enter country";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (state.length()==0) {
			String message  = "Please enter state";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (city.length()==0) {
			String message  = "Please enter city";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (postalcode.length()==0) {
			String message  = "Please enter postal code";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (currency.length()==0) {
			String message  = "Please enter currency";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (acc.length()==0) {
			String message  = "Please enter account number";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (routing.length()==0) {
			String message  = "Please enter routing number";
			showMessageOKCancel(context, message);
			return false;
		}
		else
			return true;
	}
// reset pass validation

	public  boolean resetpassvalid(Context context, String old  , String pass,String Retypepass)
	{

		if( old.length()==0 &&  pass.length()==0 && Retypepass.length()==0 )
		{
			String message  = "Please fill all fields";
			showMessageOKCancel(context, message);
			return false;

		}

		else if (old.length()==0) {
			String message  = "Please fill old password field";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(pass.length()<5 || pass.length()>40)
		{
			String message  ="Please enter password between 5 to 40 digits long.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (Retypepass.length()==0) {
			String message  = "Please fill re-type password field.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(!pass.equals(Retypepass))
		{
			String message  ="Password not matched ! ";
			showMessageOKCancel(context, message);
			return false;
		}
		else
			return true;
	}





	public  boolean business_validate(Context context, String username, String firstName, String lastName, String email  , String pass,String Retypepass)
	{

		if(username.length()==0 && firstName.length()==0 && lastName.length()==0 && email.length()==0 &&  pass.length()==0 && Retypepass.length()==0 )
		{
			String message  = "Please fill all fields";
			showMessageOKCancel(context, message);
			return false;

		}

		else if (username.length()==0) {
			String message  = "Please fill username field";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (firstName.length()==0) {
			String message  = "Please fill firstname field";
			showMessageOKCancel(context, message);
			return false;
		}
		else if (lastName.length()==0) {
			String message  = "Please fill lastname field";
			showMessageOKCancel(context, message);
			return false;
		}



		else if (email.length()==0) {
			String message  = "Please fill email field";
			showMessageOKCancel(context, message);
			return false;
		}


		else if(!isValidEmaillId(email.trim()))
		{

			String message  = "Please enter a valid email address";
			showMessageOKCancel(context, message);
			return false;

		}



		else if(pass.length()<5 || pass.length()>40)
		{
			String message  ="Please enter password between 5 to 40 digits long.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (Retypepass.length()==0) {
			String message  = "Please fill re-type password field.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(!pass.equals(Retypepass))
		{
			String message  ="Password not matched ! ";
			showMessageOKCancel(context, message);
			return false;
		}
		else
			return true;
	}


	public boolean profilevalidate(Context context, String email, String phone  , String password , String re_password/*,
	String website ,  String carrier , String country , String aboutme , String password , String re_password*/)
	{
		if(email.equals(""))
		{

			String message  = "Please fill all fields.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(!isValidEmaillId(email.trim()))
		{
			String message  = "Please enter a valid email address.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(phone.length()<9 || phone.length()>21)
		{
			String message  ="Please enter phone number between 9 to 21 digits long.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(!password.equals(re_password))
		{
			String message  ="Password not matched ! ";
			showMessageOKCancel(context, message);
			return false;
		}


		else
		{
			return true;
		}


	}
	public boolean post_desc(Context context, String title,String desc,String key)
	{

		if(title.length()==0 ){

			String message  = "Please fill email field.";
			showMessageOKCancel(context, message);
			return false;

		}

		else if (desc.length()==0){

			String message  = "Please fill email field.";
			showMessageOKCancel(context, message);
			return false;

		}
		else if (key.length()==0){

			String message  = "Please fill email field.";
			showMessageOKCancel(context, message);
			return false;

		}
		else {
			return true;
		}



	}
	public boolean post_detail(Context context, String title)
	{

		if(title.length()==0 ){

			String message  = "Please fill price field.";
			showMessageOKCancel(context, message);
			return false;

		}


		else {
			return true;
		}



	}

	public boolean product_transaction(Context context, String payment,String delivery,String currency,String option)
	{

		if(payment.equalsIgnoreCase("Payment Method")){

			String message  = "Please select mode of payment!";
			showtoast(context, message);
			return false;

		}

		if(delivery.equalsIgnoreCase("Select delivery mode")){

			String message  = "Please select mode of delivery!";
			showtoast(context, message);
			return false;

		}

		if(currency.equalsIgnoreCase("Select currency")){

		String message  = "Please select your currency!";
		showtoast(context, message);
		return false;

	}

		if(option.equalsIgnoreCase("Select option")){

			String message  = "Please select an feature option!";
			showtoast(context, message);
			return false;

		}


		else {
			return true;
		}



	}
	// private message validations

	public boolean productinfo_validate(Context context, String name,String category,String sub_Cat,String price,String type,String condition,String quantity)
	{

		if(name.length()==0 ){

			String message  = "Please enter Product Name!";
			showtoast(context, message);
			return false;

		}

		if(category.equalsIgnoreCase("Select Category") ){

			String message  = "Please select an Category!";
			showtoast(context, message);
			return false;

		}

		if(sub_Cat.equalsIgnoreCase("Select Sub-Category") ){

			String message  = "Please select an Sub-Category!";
			showtoast(context, message);
			return false;

		}

		if(price.length()==0 || price.equalsIgnoreCase("0.00") || price.equalsIgnoreCase("0")){

			String message  = "Please enter Product Price!";
			showtoast(context, message);
			return false;

		}
		if(type.equalsIgnoreCase("Selecy Type") ){

			String message  = "Please select Product Type!";
			showtoast(context, message);
			return false;

		}

		if(condition.equalsIgnoreCase("Select Condition") ){

			String message  = "Please select Product Condition!";
			showtoast(context, message);
			return false;

		}


		if(quantity.length()==0 ){

			String message  = "Please enter Product Quantity!";
			showtoast(context, message);
			return false;

		}




		else {
			return true;
		}



	}


	public boolean forgotvalidate(Context context, String email1)
	{
		
		if(email1.length()==0 ){
			
			String message  = "Please fill email field.";
			showMessageOKCancel(context, message);
			return false;
			
		}
		
		  else if(!isValidEmaillId(email1.trim()))
		  {
		   
		   String message  = "Please enter a valid email address.";
		   showMessageOKCancel(context, message);
		   return false;

		   }
		 else {
				return true;
		}
		
	
		
	}

	
	
	public boolean listingDescription(Context context, String title) {
	
		
		if(title.length()==0)
		{
			String message  = "Title field is mandatory.";
			showMessageOKCancel(context, message);
			return false;
		}

		else
		return true;
}



	public boolean listingDetail(Context context, String phone) {


		if(phone.length()==0)
		{
			String message  = "Phone field is mandatory.";
			showMessageOKCancel(context, message);
			return false;
		}
		else if(phone.length()<9 || phone.length()>21)
		{
			String message  ="Please enter phone number between 9 to 21 digits long";
			showMessageOKCancel(context, message);
			return false;
		}

		else
			return true;
	}
	public boolean forget_password(Context context, String phone) {


		if(phone.length()==0)
		{
			String message  = "Phone field is mandatory.";
			showMessageOKCancel(context, message);
			return false;
		}
		else if(phone.length()<9 || phone.length()>21)
		{
			String message  ="Please enter phone number between 9 to 21 digits long";
			showMessageOKCancel(context, message);
			return false;
		}

		else
			return true;
	}


	public boolean compose_Message(Context context, String subject , String messagetxt , String checkuser)
	{

		if(subject.length()==0 && messagetxt.length()==0)
		{
			String message  = "All fields are mandatory";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(checkuser.length()==0)
		{
			String message  = "Enter enter member username";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(subject.length()==0)
		{
			String message  = "Subject field is mandatory.";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(messagetxt.length()==0)
		{
			String message  = "message field is mandatory.";
			showMessageOKCancel(context, message);
			return false;
		}

		else
			return true;
	}

	public boolean contactAuthor(Context context, String phone , String name , String email , String Message)
	{

		//Toast.makeText(context , name , Toast.LENGTH_SHORT).show();
		if(name.length()==0 && email.length()==0 && phone.length()==0 && Message.length()==0 )
		{
			String message  = "Please fill all fields";
			showMessageOKCancel(context, message);
			return false;

		}

		else if (name.length()==0)
		{
			String message  = "Please fill name field";
			showMessageOKCancel(context, message);
			return false;
		}

		else if(!match(name))
		{
			String message  = "Enter characters only in name field";
			showMessageOKCancel(context, message);
			return false;
		}

		else if (email.length()==0) {
			String message  = "Email field is mandatory";
			showMessageOKCancel(context, message);
			return false;
		}


		else if(!isValidEmaillId(email.trim()))
		{

			String message  = "Please enter a valid email address";
			showMessageOKCancel(context, message);
			return false;

		}

		else if(phone.length()!=0)
		{

			if(phone.length()<9 || phone.length()>21)
		{
			String message  ="Please enter phone number between 9 to 21 digits long";
			showMessageOKCancel(context, message);
			return false;

		}
		return true;

		}


		else if(Message.length()==0)
		{
			String message  = "Message field is mandatory";
			showMessageOKCancel(context, message);
			return false;
		}

		else
			return true;
	}


	private void showtoast(Context context, String message) {
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}
		private void showMessageOKCancel(Context context, String message) {
		new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK).
		setTitle("Alert !")
		.setMessage(message)
		 .setPositiveButton("Ok", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			
			}
		}).
		create().show();
	}


	
}
