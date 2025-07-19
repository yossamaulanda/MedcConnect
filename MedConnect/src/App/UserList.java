package App;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

import App.UserData;

public class UserList {
    
    private static ArrayList<UserData> userList = new ArrayList<>();
    private static UserData loginAccount;
    
    public static ArrayList<UserData> getUserList() {
        return userList;
    }
    
    public static ArrayList<UserData> loadUser() {
        XStream xStream = new XStream(new StaxDriver());
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.alias("user", UserData.class);
        
        try (FileInputStream fileInput = new FileInputStream("users.xml")) {
            StringBuilder s = new StringBuilder();
            int isi;
            while ((isi = fileInput.read()) != -1) {
                s.append((char) isi);
            }
            
            // Add null check and proper casting
            Object result = xStream.fromXML(s.toString());
            if (result instanceof ArrayList) {
                userList = (ArrayList<UserData>) result;
            } else {
                System.out.println("Invalid XML format: Expected ArrayList");
                userList = new ArrayList<>();
            }
            
        } catch (IOException e) {
            System.out.println("File I/O error: " + e.getMessage());
            userList = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("XML parsing error: " + e.getMessage());
            userList = new ArrayList<>();
        }
        
        return userList;
    }
    
    public static void setLoginAccount(UserData loginAccount) {
        UserList.loginAccount = loginAccount;
    }
    
    public static UserData getLoginAccount() {
        return loginAccount;
    }
}