package ts.phoneintercept;


import java.util.Date;

public class MySMS {

    private String body;
    private String type;
    private String address;
    private String date;
    private Date time;
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public  String getDate() {
        return date;
    }


    /**
     *
     * @param body
     * @param address
     * @param date
     */
    public MySMS(String body, String address, String date) {
        super();
        this.body = body;
        //this.type = type;
        this.address = address;
        this.date = date;
    }




}