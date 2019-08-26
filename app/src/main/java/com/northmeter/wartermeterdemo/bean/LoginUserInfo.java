package com.northmeter.wartermeterdemo.bean;

import org.litepal.crud.DataSupport;

import java.util.Arrays;

/**用于存入登陆的用户信息
 * created by lht on 2016/5/11 08:59
 */
public class LoginUserInfo extends DataSupport{
   private long id;
   private String userName;
   private String passWord;
   private String  isSave;

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getIsSave() {
      return isSave;
   }

   public void setSave(String save) {
      isSave = save;
   }

   public String getPassWord() {
      return passWord;
   }

   public void setPassWord(String passWord) {
      this.passWord = passWord;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   @Override
   public String toString() {
      return "LoginUserInfo{" +
              "id=" + id +
              ", userName='" + userName + '\'' +
              ", passWord='" + passWord + '\'' +
              ", isSave=" + isSave +
              '}';
   }
}
