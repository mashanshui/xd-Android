package cn.edu.ahnu.wjcy.myapplication.Model;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/4 12:08，文件位于cn.edu.ahnu.wjcy.myapplication.Model
 * 文件作用：图书详情
 */

public class BookDetail {


    /**
     * code : SUCCESS
     * data : {"attributes":{"author":"本社                                                        ","title":"毛泽东文集-第八卷","plu_id":13,"price":28,"cover_url":"http://47.93.218.96:8080/beanWechat-rest/resources/NoPic.jpeg","publish_date":"1999-06-01","shelf":"马恩列斯毛邓著作及研究","publisher":"人民出版社二科"}}
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
         * attributes : {"author":"本社                                                        ","title":"毛泽东文集-第八卷","plu_id":13,"price":28,"cover_url":"http://47.93.218.96:8080/beanWechat-rest/resources/NoPic.jpeg","publish_date":"1999-06-01","shelf":"马恩列斯毛邓著作及研究","publisher":"人民出版社二科"}
         */

        private AttributesBean attributes;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public static class AttributesBean {
            /**
             * author : 本社
             * title : 毛泽东文集-第八卷
             * plu_id : 13
             * price : 28
             * cover_url : http://47.93.218.96:8080/beanWechat-rest/resources/NoPic.jpeg
             * publish_date : 1999-06-01
             * shelf : 马恩列斯毛邓著作及研究
             * publisher : 人民出版社二科
             */

            private String author;
            private String title;
            private int plu_id;
            private float price;
            private String cover_url;
            private String publish_date;
            private String shelf;
            private String publisher;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getPlu_id() {
                return plu_id;
            }

            public void setPlu_id(int plu_id) {
                this.plu_id = plu_id;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getPublish_date() {
                return publish_date;
            }

            public void setPublish_date(String publish_date) {
                this.publish_date = publish_date;
            }

            public String getShelf() {
                return shelf;
            }

            public void setShelf(String shelf) {
                this.shelf = shelf;
            }

            public String getPublisher() {
                return publisher;
            }

            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }
        }
    }
}
