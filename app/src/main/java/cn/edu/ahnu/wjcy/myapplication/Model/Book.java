package cn.edu.ahnu.wjcy.myapplication.Model;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/2 21:00，文件位于cn.edu.ahnu.wjcy.myapplication.Model
 * 文件作用：EventBus 基本事件
 */

public class Book {
    String price;
    String title;
    String author;
    String publish;
    String location;
    String imgUrl;

    public Book(String price, String title, String author, String publish, String location, String imgUrl) {
        this.price = price;
        this.title = title;
        this.author = author;
        this.publish = publish;
        this.location = location;
        this.imgUrl = imgUrl;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
