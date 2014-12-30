package test;

import java.io.UnsupportedEncodingException;


public class test5 {
	public static void main(String[] args){  
	    
		  try {  
		     
		         test5 convert = new test5();  
		         byte [] fullByte = convert.gbk2utf8("补零");  
		         String fullStr = new String(fullByte, "UTF-8");  
		         System.out.println("string from GBK to UTF-8 byte:  " + fullStr);  
		  
		  
		     } catch (Exception e) {  
		      e.printStackTrace();  
		     }  
		 }  
		   
		 public byte[] gbk2utf8(String chenese){  
		  char c[] = chenese.toCharArray();  
		        byte [] fullByte =new byte[3*c.length];  
		        for(int i=0; i<c.length; i++){  
		         int m = (int)c[i];  
		         String word = Integer.toBinaryString(m);  
//		         System.out.println(word);  
		           
		         StringBuffer sb = new StringBuffer();  
		         int len = 16 - word.length();  
		         //补零  
		         for(int j=0; j<len; j++){  
		          sb.append("0");  
		         }  
		         sb.append(word);  
		         sb.insert(0, "1110");  
		         sb.insert(8, "10");  
		         sb.insert(16, "10");  
		           
//		         System.out.println(sb.toString());  
		           
		         String s1 = sb.substring(0,8);            
		         String s2 = sb.substring(8,16);            
		         String s3 = sb.substring(16);  
		           
		         byte b0 = Integer.valueOf(s1, 2).byteValue();  
		         byte b1 = Integer.valueOf(s2, 2).byteValue();  
		         byte b2 = Integer.valueOf(s3, 2).byteValue();  
		         byte[] bf = new byte[3];  
		         bf[0] = b0;  
		         fullByte[i*3] = bf[0];  
		         bf[1] = b1;  
		         fullByte[i*3+1] = bf[1];  
		         bf[2] = b2;  
		         fullByte[i*3+2] = bf[2];  
		           
		        }  
		        return fullByte;  
		 }  
		 
		 public static String getUTF8StringFromGBKString(String gbkStr) {  
		        try {  
		            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");  
		        } catch (UnsupportedEncodingException e) {  
		            throw new InternalError();  
		        }  
		    } 
		 
		 public static byte[] getUTF8BytesFromGBKString(String gbkStr) {  
		        int n = gbkStr.length();  
		        byte[] utfBytes = new byte[3 * n];  
		        int k = 0;  
		        for (int i = 0; i < n; i++) {  
		            int m = gbkStr.charAt(i);  
		            if (m < 128 && m >= 0) {  
		                utfBytes[k++] = (byte) m;  
		                continue;  
		            }  
		            utfBytes[k++] = (byte) (0xe0 | (m >> 12));  
		            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));  
		            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));  
		        }  
		        if (k < utfBytes.length) {  
		            byte[] tmp = new byte[k];  
		            System.arraycopy(utfBytes, 0, tmp, 0, k);  
		            return tmp;  
		        }  
		        return utfBytes;  
		    }  

}
