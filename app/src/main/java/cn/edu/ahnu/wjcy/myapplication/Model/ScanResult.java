package cn.edu.ahnu.wjcy.myapplication.Model;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/1 21:29，文件位于cn.edu.ahnu.wjcy.myapplication.Model
 * 文件作用：商品扫描结果
 */

public class ScanResult {
    private String itemName;
    private float itemPrice;//定价
    private float itemSalePrice;//折后价
    private String itemBarcode;//条形码
    private String itemId;
    private int itemNum;
    private int itemWeight;//重量
    private int itemSaleDiscount;//折扣
    private long itemSaleDiscountStart;//折扣开始时间
    private long itemSaleDiscountEnd;//折扣到期时间
    private boolean discount;//是否在折扣时间内

    public ScanResult(String itemId,String itemBarcode,String itemName, float itemPrice,int itemWeight,int itemSaleDiscount,long itemSaleDiscountStart,long itemSaleDiscountEnd) {
        this.itemId = itemId;
        this.itemBarcode = itemBarcode;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemNum = 1;
        this.itemWeight = itemWeight;
        this.itemSaleDiscount = itemSaleDiscount;
        this.itemSaleDiscountStart = itemSaleDiscountStart;
        this.itemSaleDiscountEnd = itemSaleDiscountEnd;

        long currentTime = System.currentTimeMillis();
        if(itemSaleDiscountStart<=currentTime&&currentTime<=itemSaleDiscountEnd){
            this.discount = true;
            this.itemSalePrice = (float) (itemPrice*itemSaleDiscount*0.01);
        }else{
            this.discount = false;
            this.itemSalePrice = this.itemPrice;
        }
        this.itemSalePrice = (float) (Math.round(this.itemSalePrice * 100)) / 100;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public int getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(int itemWeight) {
        this.itemWeight = itemWeight;
    }

    public int getItemSaleDiscount() {
        return itemSaleDiscount;
    }

    public void setItemSaleDiscount(int itemSaleDiscount) {
        this.itemSaleDiscount = itemSaleDiscount;
    }

    public long getItemSaleDiscountStart() {
        return itemSaleDiscountStart;
    }

    public void setItemSaleDiscountStart(long itemSaleDiscountStart) {
        this.itemSaleDiscountStart = itemSaleDiscountStart;
    }

    public long getItemSaleDiscountEnd() {
        return itemSaleDiscountEnd;
    }

    public void setItemSaleDiscountEnd(long itemSaleDiscountEnd) {
        this.itemSaleDiscountEnd = itemSaleDiscountEnd;
    }

    public float getItemSalePrice() {
        return itemSalePrice;
    }

    public void setItemSalePrice(float itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public ScanResult() {
    }
}
