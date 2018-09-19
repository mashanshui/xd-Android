package cn.edu.ahnu.wjcy.myapplication.util.requestNet;

/**
 * 登陆请求返回实体
 * Created by cbm on 2017-05-27.
 */

public class ReadRequestResult {

    /**
     * data : {"bookname":"C:\\Users\\Tao\\Desktop\\Project\\BookDistinguish\\static\\picture\\yizhixiaohua.png","url":"http://www.baidu.com"}
     * message : success
     */

    private DataBean data;
    private String message;

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
         * bookname : C:\Users\Tao\Desktop\Project\BookDistinguish\static\picture\yizhixiaohua.png
         * url : http://www.baidu.com
         */

        private String bookname;
        private String url;

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
