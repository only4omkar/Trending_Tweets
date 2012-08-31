
package com.test;

import twitter4j.auth.AccessToken;
import twitter4j.conf.*;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.GeoLocation;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TwitterHelper extends TestActivity{
		static String a[][];
		static Status s;
		static final String TAG="TwitterHelper";
		static Status  check=null;
		public static final String PREFS_NAME = "TwitterTokens";
	    private AccessToken acc;
	    private TwitterException verifyCredentials(Twitter twitter,SharedPreferences prefs) {
	        // gets Twitter instance with default Credential
	    	String token=prefs.getString("token", null);
	    	String tokenSecret=prefs.getString("tokenSecret", null);
	    	System.out.println("Token "+token);
	    	System.out.println("TokenSEcret "+tokenSecret);
	    	if(token!=null&&tokenSecret!=null)
	    	{
	    	acc = new AccessToken(token,tokenSecret);
	    	}
	    
	    	twitter.setOAuthConsumer(TwitterInitialize.consumerKey,TwitterInitialize.consumerSecret);	
	    	twitter.setOAuthAccessToken(acc);
	    	//twitter=getAccessTokens(twitter);
	    	TwitterException e=null;
	        try {   
	             twitter.getAuthorization(); 
	             
	              Log.d("TwitterHelper","Succesfully verified credentials -- "+twitter.getRateLimitStatus());
	                return e;
	        
	        }
	        catch(TwitterException te)
	        {
	        	if(te.isCausedByNetworkIssue())
	        	{
	        		
	        		System.out.println("Caused by Network issue");
	        		
	        		
	        	}
	        		System.out.println("Error in verifying credentials ");
	        return te;
	        }
	        
	        
	        
	      
	        
	    }
	    public static void displayKeywords()
	    {
	    	
	    	for(int i=0;i<a.length;i++)
	    	{
	    		System.out.println(a[i]+" ");
	    	}
	    		
	    }
	    public  Status[] searchKeyword(String x,SharedPreferences prefs)throws TwitterException
	    {
	    	Log.d("SEARCH KEYWORD","Start Searching For keyword -->"+x);
	    	Twitter twitter=new TwitterFactory().getInstance();
	    	TwitterException te=verifyCredentials(twitter,prefs);
        	System.out.println(twitter.getRateLimitStatus());
             if(te!=null)
             {
            	// System.out.println("Could not verify the credentials ");
            	 if(te.isCausedByNetworkIssue())
            	// System.out.println("NETWORK ISSUE");
            	 throw te; 
             }
	    //	TwitterException te=this.verifyCredentials(twitter);
	    	//if(te!=null)
	    	{
	    	//	throw te;
	    	}
	    	Status[] final_tweet = new Status[10];
	    	
	    	try {
            	Query q=new Query(x);
            	q.setGeoCode(new GeoLocation(19.0177,72.8562),40,"km");
            	//q.maxId();
            	q.setLang("en");
            	q.resultType(Query.POPULAR);
            	q.setRpp(10);
            	//q.page(10);
            	System.out.println("Here");
            	QueryResult result=null;
            	try
            	{
                result = twitter.search(q);
            	}
            	catch(TwitterException e)
            	{
            		if(e.isErrorMessageAvailable())
            	Log.d("TwitterHelper,SearchKeyword",e.getErrorMessage());
            		else
            			Log.d("TwitterHelper,SearchKeyword","Could not search for keywords");		
            	return null;
            	}
                System.out.println("There");
                List<Tweet> tweets = result.getTweets();
                System.out.println(twitter.getRateLimitStatus());
                int i=0; 
                for (Tweet tweet : tweets) 
                 {
                	Status s=twitter.showStatus(tweet.getId());
                	System.out.println(s.getRateLimitStatus());
                	Status s1=s.getRetweetedStatus();
                	if(s1!=null)
                	{
                	System.out.println("Original  "+s1.getRetweetCount());
                	final_tweet[i++%10]=s1;
                	continue;
                	}
                	if(s.getRetweetCount()>20)
                	{
                    System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText());
                    final_tweet[i++%10]=s;
                     }
                 }
            } catch (TwitterException te1) {
                te1.printStackTrace();
                System.out.println("Failed to search tweets: " + te1.getMessage());
                System.exit(-1);
            }

	    	
	    	return final_tweet;
	    	/*	Log.d("SEARCH KEYWORD","Start Searching For keyword -->"+x);
	    		
	    		if(check!=null)
	    		{
	    			System.out.println(check);
	    			Pattern pattern = Pattern.compile(x,Pattern.CASE_INSENSITIVE);
	    			Matcher matcher = pattern.matcher(check.getText());
	    			if(matcher.find())
	    			{
	    				Log.d("SEARCH KEYWORD"," BYPASS SEARCH for keyword ="+x);
	    				setKeywordId(id,dbhandler);
	    				return check;
	    		}
	    		}
	    		if(x.charAt(0)=='@')
	    		{
	    			 answer=x; 
	    		
	    		}
	    		else
	    		{	
	    		Log.d("SEARCH KEYWORD"," START SEARCH");
	    			Query q=new Query(x);
	         	int pc=1;
	         	
	         	q.setGeoCode(new GeoLocation(19.0177,72.8562),40,"km");
	         	q.maxId(id);
	         	//q.setLang("English(US)");
	         	q.setRpp(100);
	         	q.page(pc);
	         	
	         	QueryResult result=null;
	         	try
	         	{
	             result = twitter.search(q);
	             
	         	}
	         	catch(TwitterException e)
	         	{
	         		throw e;
	         		
	         		
	         	}
	         	List<Tweet> tweets = result.getTweets();
	          /* For auto increment page count write code here . 
	           *  pc=pc+1;
	             result = twitter.search(q);
	             tweets = result.getTweets();
	             if(tweets.isEmpty())
	             {
	            	 System.out.println("List is getting empty");
	                 break;
	             }
	             
	         	QueryResult dummy;
	             
	         	try
	         	{
	         	dummy=twitter.search(new Query("151616agwagwag164"));
	         	} 
	         	catch(TwitterException e)
		         	{
		         		throw e;	
		         	}
	             List<Tweet> cache=dummy.getTweets();
	             cache.clear();
	             if(tweets.isEmpty())
	             {
	            	 System.out.println("List is getting empty");
	            	 
	                 return null;
	             }
	             String str,res="";
	             for (Tweet tweet : tweets) {
	                 if(tweet.getText().contains("RT"))
	                 {
	                	 
	                 cache.add(tweet);
	                // System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText());
	                 
	                 str=tweet.getText();
	                 int c=str.indexOf("@");
	                 int b=str.indexOf(":",c);
	                 int d=str.indexOf(" ",c);
	                 if(d<b)
	                	 b=d;
	                 if(b>120)
	                	 continue;
	                 try
	                 {
	                 res+=str.substring(c,b)+" ";
	                 }
	                 catch(Exception e)
	                 {
	                	 continue;
	                 }
	                 if(res.equals("@"))
	                	 continue;
	                 
	                        
	             }
	                 
	             }
	             
	            // System.out.println(res);
	           //  System.exit(0);
	             String imm[]=res.split(" ");
	             if(imm.length<=11)
	            	 return null;
	             
	              answer="";
	             int count=0;
	             for(int i=0;i<=imm.length/4;i++)
	             {
	            	 count=0;
	            	 String m=imm[i];
	           // 	 System.out.print("here");
	            	for(int j=i+1;j<imm.length;j++)
	            	{
	            		String n=imm[j];
	            		if(m.equalsIgnoreCase(n))
	            		{
	            //			System.out.print("iinside");
	            			count++;
	            		}
	            		if(count>=imm.length/4)
	            		{
	            			answer+=m;
	                        break;	
	            		}
	            		
	            	}
	            	System.out.print("in ");
	            	if(count>=imm.length/4)
	            		break;
	             }
	           //  System.exit(0);
	             if(count<imm.length/4)
	            	 return null;
	    		}
	    		
	    		Log.d("TwitterHelper SearchKeyword","Got user -->"+answer);
	             Paging pg=new Paging(1,50);
	             pg.maxId(id);
	             List<Status> statuses=null;
	             try
	             {statuses=twitter.getUserTimeline(answer,pg);
	             }
	             catch(TwitterException e)
	             {
	            	throw e;
	             }
	             for(Status s:statuses)
	             {
	            	// System.out.println("andar");
	            	 if(s.getRetweetCount()>2)
	            	 {
	            		// System.out.println("Inside");
	            		if(s.getText().toLowerCase().contains(x))		 
	            		 {	 
	            		 String st="@" + s.getUser().getScreenName() + " - " + s.getText();
	            		 Log.d("TwitterHelper SearchKeyword",st);
	            		 check=s;
	            		 setKeywordId(id,dbhandler);
	            		 return s;
	            		 
	            		 }
	            		else if(x.charAt(0)=='@'&&s.getRetweetCount()>15)
	            		{
	            			String st="@" + s.getUser().getScreenName() + " - " + s.getText();
		            		 Log.d("TwitterHelper SearchKeyword",st);
	            			setKeywordId(id,dbhandler);
	            			return s;
	            		}
	            	 }
	             }
	             
	               
	             setKeywordId(id,dbhandler);  
	    	return null;
	    	*/
	    	
	    }
	         public  String[][] getKeywords(long last_id,SharedPreferences prefs,long first_id)throws TwitterException
	         {
	        	 a=new String[50][50];
	        	 Log.d(TAG, "IN getKeywords");
	        	 Log.d(TAG, " Latest Keyword Id from database --> "+last_id);
	        	 Log.d(TAG, " Oldest Keyword Id from database --> "+first_id);
	        	 Twitter twitter=new TwitterFactory().getInstance();
	        	 TwitterException te=verifyCredentials(twitter,prefs);
	        	 System.out.println(twitter.getRateLimitStatus());
	             if(te!=null)
	             {
	            	// System.out.println("Could not verify the credentials ");
	            	 if(te.isCausedByNetworkIssue())
	            	// System.out.println("NETWORK ISSUE");
	            	 throw te; 
	             }
	            	 List<Status> statuses=null;;
	            	 Paging pg=new Paging();
	            	 
	            	 if(first_id!=0)
	            		 pg.setMaxId(first_id);
	            	 if(last_id!=0)
	            		 pg.setSinceId(last_id);
	            	 pg.setCount(20);
	            	 try
	            	 { 
	                statuses = twitter.getUserTimeline("@TrendsMumbai",pg);
	            	 }
	            	 catch(TwitterException e)
	            	 {
	            		throw e;
	            	 }
	            for (Status status : statuses) {
	            	String r="";
	            	//if(id==0)
	            	//	try
	            	//	{Thread.sleep(20);
	            	//	}
	            	//catch(Exception e)
	            	//{}
	            	System.out.println(status.getId());
	            	
	            	
	            	String s=status.getText();
	            	
	            	int j;
	            	for(int i=0;i<s.length();i++)
	            	{
	            	if(s.charAt(i)==(char)39)
	            	{
	            		j=s.indexOf((char)39,i+1);
	            		if(j!=-1)
	            		{
	            			try
	            			{
	            		r+=s.substring(i+1,j)+" ";
	            		}
	            			catch(IndexOutOfBoundsException e)
	            			{
	            				Log.d("TwitterHelper,SearchKeyword","Array index out of bounds");
	            				continue;
	            			}
	            		}
	            		else continue;
	            	i=j+1;
	            	}
	            	
	            	if(s.charAt(i)==(char)35)
	            	{
	            		int x=s.indexOf(' ',i+1);
	            		int y=s.indexOf(',',i+1);
	            		j=x;
	            		if(y!=-1)
	            		{
	            			j=y<x?y:x;
	            		}
	            		
	            		
	            		if(s.substring(i,j).equals("#Mumbai"))
	            		{
	            			continue;
	            		}
	            	r+=s.substring(i,j)+" ";
	            	
	            	
	            	
	            	i=j+1;
	            	}
	            	if(s.charAt(i)=='@')
	            	{
	            		int y=s.indexOf(' ',i+1);
	            		r+=s.substring(i,y)+" ";
	            	
	            	
	            	}
	            	
	            	
	            	
	            	}
	            	if(r==""||r.contains("___"))
	            		continue;
	            	String temp[]=r.split(" ");
	            	for(int i=0;i<a[0].length;i++)
	            	{
	            		if(a[0][i]==null||a[0][i]=="")
	            		{
	            		for(int k=0;k<temp.length;k++)
	            		{
	            		a[0][i]=temp[k];
	            		a[1][i]=status.getId()+"";
	            		Log.d(TAG,a[0][i]+" "+a[1][i]);
	                   i++;
	            	}
	            		break;
	            	}
	            	}
	            }
	      
	             return a;
	    }
	         
	    
	 /* private static Twitter getAccessTokens(Twitter twitter)
	    {
	    	
	    	try
	    	{
	    	 ConfigurationBuilder cb = new ConfigurationBuilder();
	         cb.setDebugEnabled(true).setOAuthConsumerKey("boud3TgKRj95NWWktuzzrg")
	                .setOAuthConsumerSecret("g5hSAOZupApHunj3DwC68bjPVybHlf7hP2EvXykpeM")
	                  .setOAuthAccessToken("41099130-5TtHPmNZ6nd9v1STmWjG6AQUyQDIblxKrKQdf8y6h")
	                .setOAuthAccessTokenSecret("KMr7SzcQGTT1w3nU8voajgmoUN1j0xTkrWb0w8CvwPw");
	         TwitterFactory tf = new TwitterFactory(cb.build());
	         twitter = tf.getInstance();
	    	}
	        catch(Exception e)
	        {
	        	System.out.println("Error in config");
	        }
	    	return twitter;
	    }
	  */
	   
	}

