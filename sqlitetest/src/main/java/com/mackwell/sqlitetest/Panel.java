package com.mackwell.sqlitetest;

/**
 * Created by weiyuan zhu on 10/10/14.
 */
public class Panel {
    private int _id;
    private String _ip;
    private String _panelLocation;
    private boolean check;

    public Panel()
    {

    }

    public Panel(int id, String productName, String ip)
    {
        this._id = id;
        this._panelLocation = productName;
        this._ip = ip;
    }

    public Panel(String ip, String location)
    {
        this._panelLocation = location;
        this._ip = ip;
    }

    //getter and setters

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_panelLocation() {
        return _panelLocation;
    }

    public void set_panelLocation(String _panelLocation) {
        this._panelLocation = _panelLocation;
    }

    public String get_ip() {
        return _ip;
    }

    public void set_ip(String _ip) {
        this._ip = _ip;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
