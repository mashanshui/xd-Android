package cn.edu.ahnu.wjcy.myapplication.Model.responBean;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/4 17:03，文件位于cn.edu.ahnu.wjcy.myapplication.Model.responBean
 * 文件作用：
 */

public class CheckMerberResult {

    /**
     * code : SUCCESS
     * data : {"memberInformation":{"city":null,"country":null,"groupid":0,"headimage":null,"id":"001435f41af64c29a54bd70821ae804c","merberid":null,"nickname":null,"openid":"oL8OJ09NPqm2NFDAUl20Tp7PUV1U","peopleid":null,"phonenum":"15055313525","province":null,"remark":null,"sex":0,"subscribeTime":0}}
     * message : 成功
     */

    private String code;
    private DataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * memberInformation : {"city":null,"country":null,"groupid":0,"headimage":null,"id":"001435f41af64c29a54bd70821ae804c","merberid":null,"nickname":null,"openid":"oL8OJ09NPqm2NFDAUl20Tp7PUV1U","peopleid":null,"phonenum":"15055313525","province":null,"remark":null,"sex":0,"subscribeTime":0}
         */

        private MemberInformationBean memberInformation;

        public MemberInformationBean getMemberInformation() {
            return memberInformation;
        }

        public void setMemberInformation(MemberInformationBean memberInformation) {
            this.memberInformation = memberInformation;
        }

        public static class MemberInformationBean {
            /**
             * city : null
             * country : null
             * groupid : 0
             * headimage : null
             * id : 001435f41af64c29a54bd70821ae804c
             * merberid : null
             * nickname : null
             * openid : oL8OJ09NPqm2NFDAUl20Tp7PUV1U
             * peopleid : null
             * phonenum : 15055313525
             * province : null
             * remark : null
             * sex : 0
             * subscribeTime : 0
             */

            private Object city;
            private Object country;
            private int groupid;
            private Object headimage;
            private String id;
            private Object merberid;
            private Object nickname;
            private String openid;
            private Object peopleid;
            private String phonenum;
            private Object province;
            private Object remark;
            private int sex;
            private int subscribeTime;

            public Object getCity() {
                return city;
            }

            public void setCity(Object city) {
                this.city = city;
            }

            public Object getCountry() {
                return country;
            }

            public void setCountry(Object country) {
                this.country = country;
            }

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public Object getHeadimage() {
                return headimage;
            }

            public void setHeadimage(Object headimage) {
                this.headimage = headimage;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Object getMerberid() {
                return merberid;
            }

            public void setMerberid(Object merberid) {
                this.merberid = merberid;
            }

            public Object getNickname() {
                return nickname;
            }

            public void setNickname(Object nickname) {
                this.nickname = nickname;
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public Object getPeopleid() {
                return peopleid;
            }

            public void setPeopleid(Object peopleid) {
                this.peopleid = peopleid;
            }

            public String getPhonenum() {
                return phonenum;
            }

            public void setPhonenum(String phonenum) {
                this.phonenum = phonenum;
            }

            public Object getProvince() {
                return province;
            }

            public void setProvince(Object province) {
                this.province = province;
            }

            public Object getRemark() {
                return remark;
            }

            public void setRemark(Object remark) {
                this.remark = remark;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getSubscribeTime() {
                return subscribeTime;
            }

            public void setSubscribeTime(int subscribeTime) {
                this.subscribeTime = subscribeTime;
            }
        }
    }
}
