package yanbo.utils;


import yanbo.utils.base.mvp.BaseBean;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2018/10/8
 */
public class UserInfoBean extends BaseBean {
    /**
     * user_id : 18
     * user_icon :
     * user_nickname : 1
     * user_sex : 1
     * user_score : 0
     * user_money : 0.00
     * user_vip : 0
     * user_phone : 15900001100
     * user_birthday :
     */

    private String user_id;
    private String user_icon;
    private String user_nickname;
    private int user_sex;
    private String user_score;
    private String user_money;
    private String user_vip;
    private String user_phone;
    private String user_birthday;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_score() {
        return user_score;
    }

    public void setUser_score(String user_score) {
        this.user_score = user_score;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getUser_vip() {
        return user_vip;
    }

    public void setUser_vip(String user_vip) {
        this.user_vip = user_vip;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }
}
